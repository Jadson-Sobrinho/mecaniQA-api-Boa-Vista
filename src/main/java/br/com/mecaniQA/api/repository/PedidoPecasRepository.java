package br.com.mecaniQA.api.repository;

import br.com.mecaniQA.api.model.PedidoPecas;

import java.util.ArrayList;
import java.util.List;

public class PedidoPecasRepository {

    private static PedidoPecasRepository instance;

    private final List<PedidoPecas> pedidos =
            new ArrayList<>();

    private Long proximoCodigo = 1L;

    private PedidoPecasRepository() {
    }

    public static PedidoPecasRepository getInstance() {

        if (instance == null) {
            instance = new PedidoPecasRepository();
        }

        return instance;
    }

    public PedidoPecas salvar(PedidoPecas pedido) {

        pedido.setCodigo(proximoCodigo++);

        pedidos.add(pedido);

        return pedido;
    }

    public List<PedidoPecas> listar() {

        return new ArrayList<>(pedidos);
    }

    public PedidoPecas buscarPorCodigo(Long codigo) {

        for (PedidoPecas pedido : pedidos) {

            if (pedido.getCodigo().equals(codigo)) {
                return pedido;
            }
        }

        return null;
    }

    public boolean atualizar(PedidoPecas pedido) {

        PedidoPecas existente =
                buscarPorCodigo(pedido.getCodigo());

        if (existente == null) {
            return false;
        }

        existente.setStatus(pedido.getStatus());
        existente.setItens(pedido.getItens());

        return true;
    }

    public boolean deletar(Long codigo) {

        PedidoPecas pedido =
                buscarPorCodigo(codigo);

        if (pedido == null) {
            return false;
        }

        pedidos.remove(pedido);

        return true;
    }
}