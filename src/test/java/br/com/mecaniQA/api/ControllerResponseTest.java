package br.com.mecaniQA.api;

import br.com.mecaniQA.api.controller.PecaController;
import br.com.mecaniQA.api.dto.ErroDTO;
import br.com.mecaniQA.api.dto.PecaDTO;
import br.com.mecaniQA.api.service.PecaService;
import br.com.mecaniQA.api.exception.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

class ControllerResponseTest {

       
                                                                              
                                                                                      
       
    private static PecaController controllerCom(PecaService substituto) throws Exception {
        PecaController controller = new PecaController();
        Field campo = PecaController.class.getDeclaredField("service");
        campo.setAccessible(true);
        campo.set(controller, substituto);
        return controller;
    }

    @Test void invalidIdIs400() {
        var response = new PecaController().buscar(0L);
        assertEquals(400, response.getStatusCode().value());
        assertInstanceOf(ErroDTO.class, response.getBody());
        assertEquals("Código deve ser positivo.", ((ErroDTO) response.getBody()).getMensagem());
    }
    @Test void customExceptionsAreTranslatedLocally() throws Exception {
        RuntimeException[] errors={new RecursoNaoEncontradoException("Ausente"),new ConflitoDadosException("Vinculado"),new PersistenciaException("SQL confidencial")};
        int[] status={404,409,500};
        for(int i=0;i<errors.length;i++) {
            final RuntimeException error=errors[i];
            PecaService service=new PecaService() {
                @Override public PecaDTO buscar(Long id) {throw error;}
            };
            var response=controllerCom(service).buscar(1L);
            assertEquals(status[i],response.getStatusCode().value());
            String mensagem=((ErroDTO) response.getBody()).getMensagem();
            assertNotNull(mensagem);
            assertFalse(mensagem.contains("SQL confidencial"));
        }
    }
    @Test void successfulCreateAndDeleteUse201And204() throws Exception {
        PecaService service=new PecaService() {
            @Override public PecaDTO criar(PecaDTO dto) {return dto;}
            @Override public void excluir(Long id) {}
        };
        PecaController controller=controllerCom(service);
        assertEquals(201,controller.criar(new PecaDTO()).getStatusCode().value());
        assertEquals(204,controller.excluir(1L).getStatusCode().value());
    }
}
