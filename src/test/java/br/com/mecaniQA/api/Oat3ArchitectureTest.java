package br.com.mecaniQA.api;

import br.com.mecaniQA.api.config.ConnectionFactory;
import br.com.mecaniQA.api.repository.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.*;
import java.nio.file.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class Oat3ArchitectureTest {
    private static final Path BASE = Path.of("src/main/java/br/com/mecaniQA/api");

    private static List<Path> javaFiles(Path dir) throws Exception {
        try (var files = Files.walk(dir)) {
            return files.filter(p -> p.toString().endsWith(".java")).toList();
        }
    }

    @Test void controllersAndDtoMappersDoNotAccessRepositories() throws Exception {
        for (String dir : new String[]{"controller", "mapper"}) {
            for (Path path : javaFiles(BASE.resolve(dir))) {
                assertFalse(Files.readString(path).contains("api.repository"), path.toString());
            }
        }
    }

    @Test void professorContractIsPresent() throws Exception {
        Class<?> contract = Class.forName("br.com.mecaniQA.api.repository.Repository");
        assertEquals(6, contract.getDeclaredMethods().length);
        for (String name : new String[]{"Peca", "Servico", "PedidoPecas", "OrdemServico"}) {
            assertTrue(contract.isAssignableFrom(Class.forName("br.com.mecaniQA.api.repository." + name + "Repository")));
        }
    }

                                                                                                 
    @Test void repositoriesAreManualSingletons() throws Exception {
        List<Class<?>> classes = List.of(PecaRepository.class, ServicoRepository.class,
            PedidoPecasRepository.class, OrdemServicoRepository.class, ConnectionFactory.class);
        for (Class<?> type : classes) {
            for (Constructor<?> c : type.getDeclaredConstructors()) {
                assertTrue(Modifier.isPrivate(c.getModifiers()), type.getSimpleName() + " deve ter construtor private");
            }
            boolean staticInstance = false;
            for (Field f : type.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers()) && f.getType().equals(type)) staticInstance = true;
            }
            assertTrue(staticInstance, type.getSimpleName() + " deve guardar a instância em atributo static");
            Method getInstance = type.getMethod("getInstance");
            assertTrue(Modifier.isStatic(getInstance.getModifiers()));
            assertSame(getInstance.invoke(null), getInstance.invoke(null), type.getSimpleName() + ".getInstance() deve devolver a mesma instância");
        }
    }

                                                                                                                    
    @Test void forbiddenFrameworkFeaturesAreAbsent() throws Exception {
        String[] forbidden = {
            "@Autowired", "@Value", "@Service", "@Component", "@Inject", "@Bean", "@Configuration",
            "stereotype.Repository", "lombok", "jakarta.persistence", "javax.persistence", "hibernate",
            "springframework.data", "JdbcTemplate", "@ExceptionHandler", "@ControllerAdvice", "@RestControllerAdvice",
            " record "
        };
        for (Path path : javaFiles(BASE)) {
            String code = Files.readString(path);
            for (String token : forbidden) {
                assertFalse(code.contains(token), path + " contém " + token.trim());
            }
        }
    }

                                                                                               
    @Test void controllersHaveOnlyNoArgConstructor() throws Exception {
        for (Path path : javaFiles(BASE.resolve("controller"))) {
            String name = path.getFileName().toString().replace(".java", "");
            Class<?> type = Class.forName("br.com.mecaniQA.api.controller." + name);
            Constructor<?>[] constructors = type.getDeclaredConstructors();
            assertEquals(1, constructors.length, name);
            assertEquals(0, constructors[0].getParameterCount(), name + " não deve receber dependências pelo construtor");
        }
    }
}
