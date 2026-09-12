package br.com.mecaniQA.api.model;

import java.util.ArrayList;
import java.util.List;

public class OrdemServico {

    private Long codigo;
    private String descricao;
    private StatusOrdemServico status;
    private List<Peca> pecas;
    private List<Servico> servicos;

    private OrdemServico(Builder builder) {
        this.codigo = builder.codigo;
        this.descricao = builder.descricao;
        this.status = builder.status;
        this.pecas = builder.pecas;
        this.servicos = builder.servicos;
    }

    public static class Builder {

        private Long codigo;
        private String descricao;
        private StatusOrdemServico status = StatusOrdemServico.ABERTO;
        private List<Peca> pecas = new ArrayList<>();
        private List<Servico> servicos = new ArrayList<>();

        public Builder codigo(Long codigo) {
            this.codigo = codigo;
            return this;
        }

        public Builder descricao(String descricao) {
            this.descricao = descricao;
            return this;
        }

        public Builder status(StatusOrdemServico status) {
            this.status = status;
            return this;
        }

        public Builder pecas(List<Peca> pecas) {
            this.pecas = pecas;
            return this;
        }

        public Builder servicos(List<Servico> servicos) {
            this.servicos = servicos;
            return this;
        }

        public OrdemServico build() {
            return new OrdemServico(this);
        }
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

    public List<Peca> getPecas() {
        return pecas;
    }

    public List<Servico> getServicos() {
        return servicos;
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

    public void setPecas(List<Peca> pecas) {
        this.pecas = pecas;
    }

    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }
}