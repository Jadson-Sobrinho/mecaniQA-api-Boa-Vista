package br.com.mecaniQA.api.repository;

import java.util.*;
import br.com.mecaniQA.api.config.ConnectionFactory;
import br.com.mecaniQA.api.model.Peca;
import br.com.mecaniQA.api.mapper.PecaResultSetMapper;

public class PecaRepository extends JdbcSupport implements Repository<Peca, Long> {
                                                                                                 
    private static PecaRepository instance;

                                                                                        
    private PecaRepository() { super(ConnectionFactory.getInstance()); }

                                                
    public static synchronized PecaRepository getInstance() {
        if (instance == null) {
            instance = new PecaRepository();
        }
        return instance;
    }

    @Override public Peca save(Peca e) {
        return db.transaction(() -> {
            long id = insert("INSERT INTO pecas (nome,codigo_barras,fornecedor_marca,quantidade_estoque,preco_custo,preco_venda,categoria,tamanho,cor,data_cadastro,data_ultima_atualizacao) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
                e.getNome(),e.getCodigoBarras(),e.getFornecedorMarca(),e.getQuantidadeEstoque(),e.getPrecoCusto(),e.getPrecoVenda(),e.getCategoria(),e.getTamanho(),e.getCor(),e.getDataCadastro(),e.getDataUltimaAtualizacao());
            e.setCodigo(id);
            return findById(id).orElseThrow();
        });
    }
    @Override public Optional<Peca> findById(Long id) {
        return query("SELECT * FROM pecas WHERE codigo=?", PecaResultSetMapper::map, id).stream().findFirst();
    }
    @Override public List<Peca> findAll() {
        return query("SELECT * FROM pecas ORDER BY codigo", PecaResultSetMapper::map);
    }
    @Override public Peca update(Peca e) {
        return db.transaction(() -> {
            requireAffected(execute("UPDATE pecas SET nome=?,codigo_barras=?,fornecedor_marca=?,quantidade_estoque=?,preco_custo=?,preco_venda=?,categoria=?,tamanho=?,cor=?,data_ultima_atualizacao=? WHERE codigo=?",
                e.getNome(),e.getCodigoBarras(),e.getFornecedorMarca(),e.getQuantidadeEstoque(),e.getPrecoCusto(),e.getPrecoVenda(),e.getCategoria(),e.getTamanho(),e.getCor(),java.time.LocalDateTime.now(),e.getCodigo()));
            return findById(e.getCodigo()).orElseThrow();
        });
    }
    @Override public void deleteById(Long id) { requireAffected(execute("DELETE FROM pecas WHERE codigo=?", id)); }
    @Override public boolean existsById(Long id) {
        return !query("SELECT codigo FROM pecas WHERE codigo=?", rs -> rs.getLong(1), id).isEmpty();
    }
}
