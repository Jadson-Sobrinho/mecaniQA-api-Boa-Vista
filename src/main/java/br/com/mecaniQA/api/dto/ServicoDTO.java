package br.com.mecaniQA.api.dto;

import java.math.BigDecimal;

public class ServicoDTO {

    private Long codigo;
    private String nome;
                                                                               
    private Integer tempoEstimado;
    private BigDecimal custoTabelado;

    public ServicoDTO() {
    }

    public ServicoDTO(
            Long codigo,
            String nome,
            Integer tempoEstimado,
            BigDecimal custoTabelado) {

        this.codigo = codigo;
        this.nome = nome;
        this.tempoEstimado = tempoEstimado;
        this.custoTabelado = custoTabelado;
    }

    public Long getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public Integer getTempoEstimado() {
        return tempoEstimado;
    }

    public BigDecimal getCustoTabelado() {
        return custoTabelado;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTempoEstimado(Integer tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }

    public void setCustoTabelado(BigDecimal custoTabelado) {
        this.custoTabelado = custoTabelado;
    }
}