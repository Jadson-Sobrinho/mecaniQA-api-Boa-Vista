package br.com.mecaniQA.api.dto;

public class ServicoDTO {

    private Long codigo;
    private String nome;
    private String tempoEstimado;
    private double custoTabelado;

    public ServicoDTO() {
    }

    public ServicoDTO(
            Long codigo,
            String nome,
            String tempoEstimado,
            double custoTabelado) {

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

    public String getTempoEstimado() {
        return tempoEstimado;
    }

    public double getCustoTabelado() {
        return custoTabelado;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTempoEstimado(String tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
    }

    public void setCustoTabelado(double custoTabelado) {
        this.custoTabelado = custoTabelado;
    }
}