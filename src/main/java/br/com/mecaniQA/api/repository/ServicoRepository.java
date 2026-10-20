package br.com.mecaniQA.api.repository;

import java.util.*;
import br.com.mecaniQA.api.config.ConnectionFactory;
import br.com.mecaniQA.api.model.Servico;
import br.com.mecaniQA.api.mapper.ServicoResultSetMapper;

public class ServicoRepository extends JdbcSupport implements Repository<Servico, Long> {
                                                                                                 
    private static ServicoRepository instance;

                                                                                           
    private ServicoRepository() { super(ConnectionFactory.getInstance()); }

                                                
    public static synchronized ServicoRepository getInstance() {
        if (instance == null) {
            instance = new ServicoRepository();
        }
        return instance;
    }

    @Override public Servico save(Servico e) {
        return db.transaction(() -> {
            long id = insert("INSERT INTO servicos (nome,tempo_estimado_minutos,custo_tabelado,data_criacao,data_ultima_atualizacao) VALUES (?,?,?,?,?)",
                e.getNome(),e.getTempoEstimado(),e.getCustoTabelado(),e.getDataCriacao(),e.getDataUltimaAtualizacao());
            e.setCodigo(id);
            return findById(id).orElseThrow();
        });
    }
    @Override public Optional<Servico> findById(Long id) {
        return query("SELECT * FROM servicos WHERE codigo=?", ServicoResultSetMapper::map, id).stream().findFirst();
    }
    @Override public List<Servico> findAll() {
        return query("SELECT * FROM servicos ORDER BY codigo", ServicoResultSetMapper::map);
    }
    @Override public Servico update(Servico e) {
        return db.transaction(() -> {
            requireAffected(execute("UPDATE servicos SET nome=?,tempo_estimado_minutos=?,custo_tabelado=?,data_ultima_atualizacao=? WHERE codigo=?",
                e.getNome(),e.getTempoEstimado(),e.getCustoTabelado(),java.time.LocalDateTime.now(),e.getCodigo()));
            return findById(e.getCodigo()).orElseThrow();
        });
    }
    @Override public void deleteById(Long id) { requireAffected(execute("DELETE FROM servicos WHERE codigo=?", id)); }
    @Override public boolean existsById(Long id) {
        return !query("SELECT codigo FROM servicos WHERE codigo=?", rs -> rs.getLong(1), id).isEmpty();
    }
}
