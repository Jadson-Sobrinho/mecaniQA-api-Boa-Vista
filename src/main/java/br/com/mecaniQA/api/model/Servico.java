package br.com.mecaniQA.api.model;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Servico {



    private Long codigo;
    private String nome;
                                                 
    private Integer tempoEstimado;
    private BigDecimal custoTabelado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAtualizacao;

    public Servico(
            String nome,
            Integer tempoEstimado,
            BigDecimal custoTabelado
    ) {

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

    public Integer getTempoEstimado() {
        return tempoEstimado;
    }

    public void setTempoEstimado(Integer tempoEstimado) {
        this.tempoEstimado = tempoEstimado;
        atualizarData();
    }

    public BigDecimal getCustoTabelado() {
        return custoTabelado;
    }

    public void setCustoTabelado(BigDecimal custoTabelado) {
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
    public void setCodigo(Long codigo) { this.codigo = codigo; }
    public void restaurarDatas(LocalDateTime criacao, LocalDateTime atualizacao) {
        this.dataCriacao = criacao;
        this.dataUltimaAtualizacao = atualizacao;
    }
}
