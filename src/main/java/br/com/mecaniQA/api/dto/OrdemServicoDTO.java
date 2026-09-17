package br.com.mecaniQA.api.dto;

import br.com.mecaniQA.api.model.StatusOrdemServico;

public class OrdemServicoDTO {

    private Long codigo;
    private String descricao;
    private StatusOrdemServico status;

    public OrdemServicoDTO() {
    }

    public OrdemServicoDTO(Long codigo, String descricao,
            StatusOrdemServico status) {

        this.codigo = codigo;
        this.descricao = descricao;
        this.status = status;
    }

    public Long getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public StatusOrdemServico getStatus() {
        return status;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setStatus(StatusOrdemServico status) {
        this.status = status;
    }
}