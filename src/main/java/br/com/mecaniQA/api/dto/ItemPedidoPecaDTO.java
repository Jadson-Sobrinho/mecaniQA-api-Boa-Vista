package br.com.mecaniQA.api.dto;

public class ItemPedidoPecaDTO {

    private Long codigo;
    private Long pecaCodigo;
    private Integer quantidade;

    public ItemPedidoPecaDTO() {
    }

    public ItemPedidoPecaDTO(
            Long codigo,
            Long pecaCodigo,
            Integer quantidade) {

        this.codigo = codigo;
        this.pecaCodigo = pecaCodigo;
        this.quantidade = quantidade;
    }

    public Long getCodigo() {
        return codigo;
    }

    public Long getPecaCodigo() {
        return pecaCodigo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public void setPecaCodigo(Long pecaCodigo) {
        this.pecaCodigo = pecaCodigo;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}