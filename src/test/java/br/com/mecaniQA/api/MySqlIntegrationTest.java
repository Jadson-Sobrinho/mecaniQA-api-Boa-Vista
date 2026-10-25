package br.com.mecaniQA.api;

import br.com.mecaniQA.api.config.ConnectionFactory;
import br.com.mecaniQA.api.dto.*;
import br.com.mecaniQA.api.model.*;
import br.com.mecaniQA.api.repository.*;
import br.com.mecaniQA.api.service.*;
import br.com.mecaniQA.api.exception.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import java.math.BigDecimal;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;

   
                                                                           
                                                                                        
                                                                                               
   
@EnabledIfEnvironmentVariable(named="TEST_DB_URL",matches=".+")
class MySqlIntegrationTest {
    ConnectionFactory db;
    PecaRepository pecas;
    ServicoRepository servicos;
    PedidoPecasRepository pedidos;
    OrdemServicoRepository ordens;
    PedidoPecasService service;
    @BeforeEach void setup() throws Exception {
        String url=System.getenv("TEST_DB_URL");
        if (!url.matches("jdbc:mysql://[^/]+/mecaniqa_test(\\?.*)?"))
            throw new IllegalArgumentException("Use exclusivamente o banco mecaniqa_test.");
        db=ConnectionFactory.getInstance();
        if (!url.equals(db.getUrl()))
            throw new IllegalStateException("DB_URL da JVM de teste difere de TEST_DB_URL. Execute pelo Gradle (./gradlew test).");
        try(Connection c=db.getConnection(); Statement s=c.createStatement()) {
            String ddl=Files.readString(Path.of("database/schema.sql")).replaceAll("(?m)^--.*$", "");
            for(String sql:ddl.split(";")) if(!sql.isBlank()) s.execute(sql);
            for(String table:List.of("itens_pedido_peca","ordem_servico_pecas","ordem_servico_servicos","pedidos_pecas","ordens_servico","pecas","servicos"))
                s.executeUpdate("DELETE FROM "+table);
        }
        pecas=PecaRepository.getInstance(); servicos=ServicoRepository.getInstance();
        pedidos=PedidoPecasRepository.getInstance(); ordens=OrdemServicoRepository.getInstance();
        service=new PedidoPecasService();
    }
    Peca novaPeca() {return new Peca("Filtro","123","Marca",5,new BigDecimal("10.10"),new BigDecimal("20.20"),CategoriaPeca.MOTOR);}
    Peca peca() {return pecas.save(novaPeca());}
    @Test void pecaCrudAndDatesSurviveNewConnection() throws Exception {
        Peca p=peca(); assertTrue(p.getCodigo()>0); assertTrue(pecas.existsById(p.getCodigo()));
        var created=p.getDataCadastro();
        p.setNome("Filtro novo"); p=pecas.update(p);
        assertEquals(created,p.getDataCadastro()); assertEquals("Filtro novo",p.getNome());
        assertEquals(new BigDecimal("20.20"),p.getPrecoVenda());
        try(Connection c=db.getConnection(); PreparedStatement ps=c.prepareStatement("SELECT nome FROM pecas WHERE codigo=?")) {
            ps.setLong(1,p.getCodigo());
            try(ResultSet rs=ps.executeQuery()) { assertTrue(rs.next()); assertEquals(p.getNome(),rs.getString(1)); }
        }
        assertEquals(1,pecas.findAll().size());
        pecas.deleteById(p.getCodigo()); assertFalse(pecas.existsById(p.getCodigo()));
        assertTrue(pecas.findById(p.getCodigo()).isEmpty());
    }
    @Test void serviceCrud() {
        Servico s=servicos.save(new Servico("Troca",60,new BigDecimal("100.00")));
        assertEquals(60,servicos.findById(s.getCodigo()).orElseThrow().getTempoEstimado().intValue());
        var created=s.getDataCriacao(); assertTrue(servicos.existsById(s.getCodigo()));
        s.setNome("Troca nova"); s.setTempoEstimado(90); s=servicos.update(s);
        assertEquals(created,s.getDataCriacao()); assertEquals("Troca nova",s.getNome()); assertEquals(90,s.getTempoEstimado().intValue());
        assertEquals(1,servicos.findAll().size()); servicos.deleteById(s.getCodigo());
        assertTrue(servicos.findById(s.getCodigo()).isEmpty());
    }
    @Test void orderAssociationsSurviveRepositoryAndHttpStyleUpdate() {
        Peca p=peca(); Servico s=servicos.save(new Servico("Troca",60,BigDecimal.TEN));
        OrdemServico o=ordens.save(new OrdemServico.Builder().descricao("Revisão").pecas(List.of(p)).servicos(List.of(s)).build());
        assertTrue(ordens.existsById(o.getCodigo())); assertEquals(1,ordens.findAll().size());
        new OrdemServicoService().atualizar(o.getCodigo(),new OrdemServicoDTO(null,"Revisão anual",StatusOrdemServico.PAGO));
        o=ordens.findById(o.getCodigo()).orElseThrow();
        assertEquals(1,o.getPecas().size()); assertEquals(1,o.getServicos().size());
        assertThrows(ConflitoDadosException.class,()->servicos.deleteById(s.getCodigo()));
        ordens.deleteById(o.getCodigo()); assertFalse(ordens.existsById(o.getCodigo()));
        servicos.deleteById(s.getCodigo()); pecas.deleteById(p.getCodigo());
    }
    @Test void itemsAndStatusPersistAndReferencesAreProtected() {
        Peca p=peca(); long id=service.criar(new PedidoPecasDTO()).getCodigo();
        var dto=service.adicionarItem(id,new ItemPedidoPecaDTO(null,p.getCodigo(),2));
        assertNotNull(dto.getItens().get(0).getCodigo()); assertEquals(1,pedidos.findAll().size());
        assertThrows(ConflitoDadosException.class,()->pecas.deleteById(p.getCodigo()));
        PedidoPecasDTO status=new PedidoPecasDTO();status.setStatus(StatusPedidoPecas.PAGO);
        service.alterarStatus(id,status);
        assertEquals(StatusPedidoPecas.PAGO,pedidos.findById(id).orElseThrow().getStatus());
        assertThrows(DadosInvalidosException.class,()->service.adicionarItem(id,new ItemPedidoPecaDTO(null,p.getCodigo(),1)));
        pedidos.deleteById(id);assertFalse(pedidos.existsById(id));pecas.deleteById(p.getCodigo());
    }
    @Test void updatePedidoPreservesItemIdentityAndCanRemoveItems() {
        Peca p=peca(); long id=service.criar(new PedidoPecasDTO()).getCodigo();
        service.adicionarItem(id,new ItemPedidoPecaDTO(null,p.getCodigo(),2));
        PedidoPecas pedido=pedidos.findById(id).orElseThrow();
        Long itemId=pedido.getItens().get(0).getCodigo(); pedido.getItens().get(0).setQuantidade(3);
        pedido=pedidos.update(pedido);
        assertEquals(itemId,pedido.getItens().get(0).getCodigo());assertEquals(3,pedido.getItens().get(0).getQuantidade().intValue());
        pedido.setItens(new ArrayList<>());pedido=pedidos.update(pedido);assertTrue(pedido.getItens().isEmpty());
    }
    @Test void failedChildInsertRollsBackParent() {
        Peca missing=novaPeca();missing.setCodigo(Long.MAX_VALUE);
        PedidoPecas pedido=new PedidoPecas.Builder().adicionarItem(new ItemPedidoPeca.Builder().peca(missing).quantidade(1).build()).build();
        assertThrows(ConflitoDadosException.class,()->pedidos.save(pedido));
        assertTrue(pedidos.findAll().isEmpty());
    }
    @Test void missingEntitiesReturnNotFound() {
        assertThrows(RecursoNaoEncontradoException.class,()->new PecaService().buscar(Long.MAX_VALUE));
        assertThrows(RecursoNaoEncontradoException.class,()->pecas.deleteById(Long.MAX_VALUE));
        Peca missing=novaPeca();missing.setCodigo(Long.MAX_VALUE);
        assertThrows(RecursoNaoEncontradoException.class,()->pecas.update(missing));
    }
    @Test void concurrentAdditionWaitsForStatusTransactionAndRejectsPaidOrder() throws Exception {
        Peca p=peca();long id=service.criar(new PedidoPecasDTO()).getCodigo();
        ExecutorService pool=Executors.newSingleThreadExecutor();
        CountDownLatch started=new CountDownLatch(1);
        Future<?> task;
        try(Connection c=db.getConnection()) {
            c.setAutoCommit(false);
            try(PreparedStatement ps=c.prepareStatement("SELECT codigo FROM pedidos_pecas WHERE codigo=? FOR UPDATE")) {
                ps.setLong(1,id);try(ResultSet rs=ps.executeQuery()){assertTrue(rs.next());}
            }
            task=pool.submit(()-> {started.countDown(); return service.adicionarItem(id,new ItemPedidoPecaDTO(null,p.getCodigo(),1));});
            assertTrue(started.await(5,TimeUnit.SECONDS));
            assertThrows(TimeoutException.class,()->task.get(200,TimeUnit.MILLISECONDS));
            try(PreparedStatement ps=c.prepareStatement("UPDATE pedidos_pecas SET status='PAGO' WHERE codigo=?")) {ps.setLong(1,id);ps.executeUpdate();}
            c.commit();
            ExecutionException e=assertThrows(ExecutionException.class,()->task.get(5,TimeUnit.SECONDS));
            assertInstanceOf(DadosInvalidosException.class,e.getCause());
            assertTrue(pedidos.findById(id).orElseThrow().getItens().isEmpty());
        } finally {pool.shutdownNow();}
    }
}
