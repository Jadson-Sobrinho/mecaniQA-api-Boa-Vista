package br.com.mecaniQA.api.model;

public class ItemPedidoPeca {

    private Long codigo;
    private PedidoPecas pedido;
    private Peca peca;
    private Integer quantidade;

    public ItemPedidoPeca() {
    }

    public ItemPedidoPeca(Long codigo, PedidoPecas pedido,
            Peca peca, Integer quantidade) {

        this.codigo = codigo;
        this.pedido = pedido;
        this.peca = peca;
        this.quantidade = quantidade;
    }

    public Long getCodigo() {
        return codigo;
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

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
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