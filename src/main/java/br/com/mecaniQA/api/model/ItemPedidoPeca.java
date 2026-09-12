package com.mecaniqa.model;

public class ItemPedidoPeca {

    private Long id;
    private PedidoPecas pedido;
    private Peca peca;
    private Integer quantidade;

    public ItemPedidoPeca() {
    }

    public ItemPedidoPeca(Long id, PedidoPecas pedido,
        Peca peca, Integer quantidade) {
        this.id = id;
        this.pedido = pedido;
        this.peca = peca;
        this.quantidade = quantidade;
    }


    public Long getId() {
        return id;
    }

    public PedidoPecas getPedido() {
        return pedido;
    }

    public Peca getPeca() {
        return peca;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPedido(PedidoPecas pedido) {
        this.pedido = pedido;
    }

    public void setPeca(Peca peca) {
        this.peca = peca;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}