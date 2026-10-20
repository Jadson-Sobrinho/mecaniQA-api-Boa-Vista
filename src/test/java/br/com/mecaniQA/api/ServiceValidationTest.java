package br.com.mecaniQA.api;

import br.com.mecaniQA.api.dto.*;
import br.com.mecaniQA.api.model.*;
import br.com.mecaniQA.api.service.*;
import br.com.mecaniQA.api.exception.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ServiceValidationTest {
    private PecaDTO peca() {
        return new PecaDTO(null,"Filtro","123","Marca",1,new BigDecimal("10.10"),new BigDecimal("20.20"),CategoriaPeca.MOTOR,null,null);
    }
    @Test void rejectsMissingStock() {
        PecaDTO dto=peca(); dto.setQuantidadeEstoque(null);
        assertThrows(DadosInvalidosException.class,()->new PecaService().criar(dto));
    }
    @Test void rejectsNegativePrice() {
        PecaDTO dto=peca(); dto.setPrecoCusto(new BigDecimal("-0.01"));
        assertThrows(DadosInvalidosException.class,()->new PecaService().criar(dto));
    }
    @Test void rejectsMissingBarcode() {
        PecaDTO dto=peca(); dto.setCodigoBarras(" ");
        assertThrows(DadosInvalidosException.class,()->new PecaService().criar(dto));
    }
    @Test void rejectsFractionBeyondCents() {
        PecaDTO dto=peca(); dto.setPrecoVenda(new BigDecimal("1.001"));
        assertThrows(DadosInvalidosException.class,()->new PecaService().criar(dto));
    }
    @Test void rejectsMissingServiceCost() {
        ServicoDTO dto=new ServicoDTO(null,"Troca",60,null);
        assertThrows(DadosInvalidosException.class,()->new ServicoService().criar(dto));
    }
    @Test void rejectsMissingOrNonPositiveServiceDuration() {
        ServicoDTO semTempo=new ServicoDTO(null,"Troca",null,BigDecimal.TEN);
        ServicoDTO zero=new ServicoDTO(null,"Troca",0,BigDecimal.TEN);
        ServicoDTO negativo=new ServicoDTO(null,"Troca",-15,BigDecimal.TEN);
        assertThrows(DadosInvalidosException.class,()->new ServicoService().criar(semTempo));
        assertThrows(DadosInvalidosException.class,()->new ServicoService().criar(zero));
        assertThrows(DadosInvalidosException.class,()->new ServicoService().criar(negativo));
    }
    @Test void rejectsInvalidIdsAndItemQuantity() {
        ItemPedidoPecaDTO item=new ItemPedidoPecaDTO(null,1L,0);
        assertThrows(DadosInvalidosException.class,()->new PedidoPecasService().adicionarItem(1L,item));
        assertThrows(DadosInvalidosException.class,()->new PecaService().buscar(0L));
    }
    @Test void newModelsHaveNoMemoryGeneratedIds() {
        Peca p=new Peca("Filtro","123","Marca",1,BigDecimal.ONE,BigDecimal.TEN,CategoriaPeca.MOTOR);
        Servico s=new Servico("Troca",60,BigDecimal.TEN);
        assertNull(p.getCodigo()); assertNull(s.getCodigo());
    }
}
