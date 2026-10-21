package br.com.mecaniQA.api.repository;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JdbcSupportTemplateMethodTest {

    @Test
    void jdbcSupportDeclaresFinalTemplateMethod() throws Exception {
        assertTrue(Modifier.isAbstract(JdbcSupport.class.getModifiers()));

        Method template = JdbcSupport.class.getDeclaredMethod(
                "executarTemplate", String.class, JdbcSupport.JdbcOperation.class);

        assertTrue(Modifier.isProtected(template.getModifiers()));
        assertTrue(Modifier.isFinal(template.getModifiers()),
                "O Template Method deve impedir que subclasses alterem a sequência padrão.");
    }

    @Test
    void lifecycleHooksAreProtectedAndOverridable() throws Exception {
        Method before = JdbcSupport.class.getDeclaredMethod(
                "beforeOperation", java.sql.Connection.class, String.class);
        Method after = JdbcSupport.class.getDeclaredMethod(
                "afterOperation", java.sql.Connection.class, String.class);

        assertTrue(Modifier.isProtected(before.getModifiers()));
        assertFalse(Modifier.isFinal(before.getModifiers()));
        assertTrue(Modifier.isProtected(after.getModifiers()));
        assertFalse(Modifier.isFinal(after.getModifiers()));
    }

    @Test
    void allConcreteRepositoriesInheritTheTemplateMethodBase() {
        List<Class<?>> repositories = List.of(
                PecaRepository.class,
                ServicoRepository.class,
                OrdemServicoRepository.class,
                PedidoPecasRepository.class
        );

        for (Class<?> repository : repositories) {
            assertEquals(JdbcSupport.class, repository.getSuperclass(),
                    repository.getSimpleName() + " deve herdar de JdbcSupport.");
        }
    }

    @Test
    void jdbcHelpersDelegateToTheTemplateMethod() throws Exception {
        Path sourcePath = Path.of(
                "src/main/java/br/com/mecaniQA/api/repository/JdbcSupport.java");
        String source = Files.readString(sourcePath);

        assertTrue(source.contains("return executarTemplate(sql, connection ->"),
                "As operações JDBC devem centralizar sua execução no Template Method.");
        assertTrue(source.contains("beforeOperation(connection, sql);"));
        assertTrue(source.contains("afterOperation(connection, sql);"));
        assertTrue(source.contains("finally"),
                "A etapa final deve executar mesmo quando a operação falhar.");
    }
}
