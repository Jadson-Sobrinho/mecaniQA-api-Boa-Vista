package br.com.mecaniQA.api.repository;

import java.util.*;
import br.com.mecaniQA.api.config.ConnectionFactory;
import br.com.mecaniQA.api.model.*;
import br.com.mecaniQA.api.mapper.*;
import br.com.mecaniQA.api.exception.DadosInvalidosException;

public class PedidoPecasRepository extends JdbcSupport implements Repository<PedidoPecas, Long> {
                                                                                                 
    private static PedidoPecasRepository instance;

                                                                                               
    private PedidoPecasRepository() { super(ConnectionFactory.getInstance()); }

                                                
    public static synchronized PedidoPecasRepository getInstance() {
        if (instance == null) {
            instance = new PedidoPecasRepository();
        }
        return instance;
    }

    @Override public PedidoPecas save(PedidoPecas e) {
        return db.transaction(() -> {
            long id = insert("INSERT INTO pedidos_pecas(status) VALUES (?)", e.getStatus());
            e.setCodigo(id);
            for (ItemPedidoPeca item : e.getItens()) insertItem(id, item);
            return findById(id).orElseThrow();
        });
    }
    @Override public Optional<PedidoPecas> findById(Long id) {
        return db.transaction(() -> load("SELECT * FROM pedidos_pecas WHERE codigo=?", id));
    }
                                                                                   
    public Optional<PedidoPecas> findByIdForUpdate(Long id) {
        return load("SELECT * FROM pedidos_pecas WHERE codigo=? FOR UPDATE", id);
    }
    private Optional<PedidoPecas> load(String sql, Long id) {
        return query(sql, PedidoPecasResultSetMapper::map, id).stream().findFirst().map(this::withItems);
    }
    private PedidoPecas withItems(PedidoPecas pedido) {
        List<ItemPedidoPeca> items = query("SELECT i.codigo AS item_codigo,i.quantidade,p.* " +
            "FROM itens_pedido_peca i JOIN pecas p ON p.codigo=i.peca_codigo " +
            "WHERE i.pedido_codigo=? ORDER BY i.codigo", ItemPedidoPecaResultSetMapper::map, pedido.getCodigo());
        for (ItemPedidoPeca item : items) item.setPedido(pedido);
        pedido.setItens(items);
        return pedido;
    }
    @Override public List<PedidoPecas> findAll() {
        return db.transaction(() -> query("SELECT * FROM pedidos_pecas ORDER BY codigo", PedidoPecasResultSetMapper::map)
            .stream().map(this::withItems).toList());
    }
    @Override public PedidoPecas update(PedidoPecas e) {
        return db.transaction(() -> {
            requireAffected(execute("UPDATE pedidos_pecas SET status=? WHERE codigo=?", e.getStatus(),e.getCodigo()));
            List<Long> oldIds = query("SELECT codigo FROM itens_pedido_peca WHERE pedido_codigo=?", rs -> rs.getLong(1), e.getCodigo());
            Set<Long> retained = new HashSet<>();
            for (ItemPedidoPeca item : e.getItens()) {
                if (item.getCodigo() == null) insertItem(e.getCodigo(),item);
                else {
                    if (!retained.add(item.getCodigo())) throw new DadosInvalidosException("Item duplicado.");
                    requireAffected(execute("UPDATE itens_pedido_peca SET peca_codigo=?,quantidade=? WHERE codigo=? AND pedido_codigo=?",
                        item.getPeca().getCodigo(),item.getQuantidade(),item.getCodigo(),e.getCodigo()));
                }
            }
            for (Long id : oldIds) if (!retained.contains(id)) execute("DELETE FROM itens_pedido_peca WHERE codigo=?",id);
            return findById(e.getCodigo()).orElseThrow();
        });
    }
    public void insertItem(Long pedidoId, ItemPedidoPeca item) {
        long id = insert("INSERT INTO itens_pedido_peca(pedido_codigo,peca_codigo,quantidade) VALUES (?,?,?)",
            pedidoId,item.getPeca().getCodigo(),item.getQuantidade());
        item.setCodigo(id);
    }
    public void updateStatus(Long id, StatusPedidoPecas status) {
        requireAffected(execute("UPDATE pedidos_pecas SET status=? WHERE codigo=?",status,id));
    }
    @Override public void deleteById(Long id) { requireAffected(execute("DELETE FROM pedidos_pecas WHERE codigo=?",id)); }
    @Override public boolean existsById(Long id) {
        return !query("SELECT codigo FROM pedidos_pecas WHERE codigo=?",rs -> rs.getLong(1),id).isEmpty();
    }
}
