package br.com.mecaniQA.api.model;

import java.util.ArrayList;
import java.util.List;

public class PedidoPecas {

    private Long codigo;
    private StatusPedidoPecas status;
    private List<ItemPedidoPeca> itens;

    public PedidoPecas() {
        this.itens = new ArrayList<>();
    }

    public PedidoPecas(Long codigo, StatusPedidoPecas status,
        List<ItemPedidoPeca> itens) {
        this.codigo = codigo;
        this.status = status;
        this.itens = itens;
    }

    public void adicionarItem(ItemPedidoPeca item) {

        if (item == null) {
            return;
        }

        if (!itens.contains(item)) {
            itens.add(item);
        }

        item.setPedido(this);
    }

    public void removerItem(ItemPedidoPeca item) {

        if (item == null) {
            return;
        }

        itens.remove(item);

        if (item.getPedido() == this) {
            item.setPedido(null);
        }



        public Long getCodigo() {
            return codigo;
        }

        public StatusPedidoPecas getStatus() {
            return status;
        }

        public List<ItemPedidoPeca> getItens() {
            return itens;
        }

        

        public void setCodigo(Long codigo) {
            this.codigo = codigo;
        }

        public void setStatus(StatusPedidoPecas status) {
            this.status = status;
        }

        public void setItens(List<ItemPedidoPeca> itens) {
            this.itens = itens;
        }
    }
}