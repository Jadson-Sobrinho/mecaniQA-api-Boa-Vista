package br.com.mecaniQA.api.dto;

import br.com.mecaniQA.api.model.StatusPedidoPecas;

import java.util.ArrayList;
import java.util.List;

public class PedidoPecasDTO {

    private Long codigo;
    private StatusPedidoPecas status;
    private List<ItemPedidoPecaDTO> itens;

    public PedidoPecasDTO() {
        this.itens = new ArrayList<>();
    }

    public PedidoPecasDTO(
            Long codigo,
            StatusPedidoPecas status,
            List<ItemPedidoPecaDTO> itens) {

        this.codigo = codigo;
        this.status = status;
        this.itens = itens;
    }

    public Long getCodigo() {
        return codigo;
    }

    public StatusPedidoPecas getStatus() {
        return status;
    }

    public List<ItemPedidoPecaDTO> getItens() {
        return itens;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public void setStatus(StatusPedidoPecas status) {
        this.status = status;
    }

    public void setItens(List<ItemPedidoPecaDTO> itens) {
        this.itens = itens;
    }
}