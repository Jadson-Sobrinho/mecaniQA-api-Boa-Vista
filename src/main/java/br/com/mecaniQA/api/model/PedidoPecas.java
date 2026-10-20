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

    public PedidoPecas(
            Long codigo,
            StatusPedidoPecas status,
            List<ItemPedidoPeca> itens) {

        this.codigo = codigo;
        this.status = status;
        this.itens = itens != null
                ? itens
                : new ArrayList<>();
    }

                                
              
                                

    public static class Builder {

        private Long codigo;
        private StatusPedidoPecas status =
                StatusPedidoPecas.EM_ORCAMENTO;

        private List<ItemPedidoPeca> itens =
                new ArrayList<>();

        public Builder codigo(Long codigo) {
            this.codigo = codigo;
            return this;
        }

        public Builder status(StatusPedidoPecas status) {

            if (status != null) {
                this.status = status;
            }

            return this;
        }

        public Builder itens(List<ItemPedidoPeca> itens) {

            if (itens != null) {
                this.itens = itens;
            }

            return this;
        }

        public Builder adicionarItem(ItemPedidoPeca item) {

            if (item != null) {
                this.itens.add(item);
            }

            return this;
        }

        public PedidoPecas build() {

            PedidoPecas pedido = new PedidoPecas(
                    codigo,
                    status,
                    itens
            );

            for (ItemPedidoPeca item : itens) {
                if (item != null) {
                    item.setPedido(pedido);
                }
            }

            return pedido;
        }
    }

                                
            
                                

    public boolean adicionarItem(ItemPedidoPeca item) {

        if (item == null) {
            return false;
        }

        if (!itens.contains(item)) {
            itens.add(item);
        }

        item.setPedido(this);

        return true;
    }

    public boolean removerItem(ItemPedidoPeca item) {

        if (item == null) {
            return false;
        }

        if (!itens.remove(item)) {
            return false;
        }

        if (item.getPedido() == this) {
            item.setPedido(null);
        }

        return true;
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