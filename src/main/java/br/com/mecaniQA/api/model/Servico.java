package br.com.mecaniQA.api.model;
import java.time.LocalDateTime;

public class Servico {

    private static long proximoCodigo = 1L;

    private Long codigo;
    private String nome;
    private String tempoEstimado;
    private double custoTabelado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAtualizacao;

    public Servico(
            String nome,
            String tempoEstimado,
            double custoTabelado
    ) {
        this.codigo = proximoCodigo++;
        this.nome = nome;
        this.tempoEstimado = tempoEstimado;
        this.custoTabelado = custoTabelado;
        this.dataCriacao = LocalDateTime.now();
        this.dataUltimaAtualizacao = LocalDateTime.now();
    }

    public Long getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        atualizarData();
    }

    public String getTempoEstimado() {
        return tempoEstimado;
    }

    public void setTempoEstimado(String tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
        atualizarData();
    }

    public double getCustoTabelado() {
        return custoTabelado;
    }

    public void setCustoTabelado(double custoTabelado) {
        this.custoTabelado = custoTabelado;
        atualizarData();
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    private void atualizarData() {
        this.dataUltimaAtualizacao = LocalDateTime.now();
    }
}
