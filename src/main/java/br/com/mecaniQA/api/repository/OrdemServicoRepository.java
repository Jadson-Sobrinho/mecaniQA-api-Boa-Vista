package br.com.mecaniQA.api.repository;

import java.util.*;
import br.com.mecaniQA.api.config.ConnectionFactory;
import br.com.mecaniQA.api.model.*;
import br.com.mecaniQA.api.mapper.*;

public class OrdemServicoRepository extends JdbcSupport implements Repository<OrdemServico, Long> {
                                                                                                 
    private static OrdemServicoRepository instance;

                                                                                                
    private OrdemServicoRepository() { super(ConnectionFactory.getInstance()); }

                                                
    public static synchronized OrdemServicoRepository getInstance() {
        if (instance == null) {
            instance = new OrdemServicoRepository();
        }
        return instance;
    }

    @Override public OrdemServico save(OrdemServico e) {
        return db.transaction(() -> {
            e.setCodigo(insert("INSERT INTO ordens_servico(descricao,status) VALUES (?,?)",e.getDescricao(),e.getStatus()));
            saveLinks(e);
            return findById(e.getCodigo()).orElseThrow();
        });
    }
    private void saveLinks(OrdemServico e) {
        for (Peca p : e.getPecas()) execute("INSERT INTO ordem_servico_pecas(ordem_codigo,peca_codigo) VALUES (?,?)",e.getCodigo(),p.getCodigo());
        for (Servico s : e.getServicos()) execute("INSERT INTO ordem_servico_servicos(ordem_codigo,servico_codigo) VALUES (?,?)",e.getCodigo(),s.getCodigo());
    }
    private OrdemServico withLinks(OrdemServico e) {
        e.setPecas(query("SELECT p.* FROM pecas p JOIN ordem_servico_pecas op ON op.peca_codigo=p.codigo WHERE op.ordem_codigo=? ORDER BY p.codigo",
            PecaResultSetMapper::map,e.getCodigo()));
        e.setServicos(query("SELECT s.* FROM servicos s JOIN ordem_servico_servicos os ON os.servico_codigo=s.codigo WHERE os.ordem_codigo=? ORDER BY s.codigo",
            ServicoResultSetMapper::map,e.getCodigo()));
        return e;
    }
    @Override public Optional<OrdemServico> findById(Long id) {
        return db.transaction(() -> query("SELECT * FROM ordens_servico WHERE codigo=?",OrdemServicoResultSetMapper::map,id)
            .stream().findFirst().map(this::withLinks));
    }
    public Optional<OrdemServico> findByIdForUpdate(Long id) {
        return query("SELECT * FROM ordens_servico WHERE codigo=? FOR UPDATE",OrdemServicoResultSetMapper::map,id)
            .stream().findFirst().map(this::withLinks);
    }
    @Override public List<OrdemServico> findAll() {
        return db.transaction(() -> query("SELECT * FROM ordens_servico ORDER BY codigo",OrdemServicoResultSetMapper::map)
            .stream().map(this::withLinks).toList());
    }
    @Override public OrdemServico update(OrdemServico e) {
        return db.transaction(() -> {
            requireAffected(execute("UPDATE ordens_servico SET descricao=?,status=? WHERE codigo=?",e.getDescricao(),e.getStatus(),e.getCodigo()));
            execute("DELETE FROM ordem_servico_pecas WHERE ordem_codigo=?",e.getCodigo());
            execute("DELETE FROM ordem_servico_servicos WHERE ordem_codigo=?",e.getCodigo());
            saveLinks(e);
            return findById(e.getCodigo()).orElseThrow();
        });
    }
    @Override public void deleteById(Long id) { requireAffected(execute("DELETE FROM ordens_servico WHERE codigo=?",id)); }
    @Override public boolean existsById(Long id) {
        return !query("SELECT codigo FROM ordens_servico WHERE codigo=?",rs -> rs.getLong(1),id).isEmpty();
    }
}
