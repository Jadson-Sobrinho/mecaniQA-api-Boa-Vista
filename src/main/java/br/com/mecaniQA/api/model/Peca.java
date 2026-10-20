package br.com.mecaniQA.api.model;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Peca {



    private Long codigo;
    private String nome;
    private String codigoBarras;
    private String fornecedorMarca;
    private int quantidadeEstoque;
    private BigDecimal precoCusto;
    private BigDecimal precoVenda;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataUltimaAtualizacao;
    private CategoriaPeca categoria;
    private String tamanho;
    private String cor;

    public Peca(
            String nome,
            String codigoBarras,
            String fornecedorMarca,
            int quantidadeEstoque,
            BigDecimal precoCusto,
            BigDecimal precoVenda,
            CategoriaPeca categoria
    ) {

        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.fornecedorMarca = fornecedorMarca;
        this.quantidadeEstoque = quantidadeEstoque;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.categoria = categoria;
        this.dataCadastro = LocalDateTime.now();
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

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
        atualizarData();
    }

    public String getFornecedorMarca() {
        return fornecedorMarca;
    }

    public void setFornecedorMarca(String fornecedorMarca) {
        this.fornecedorMarca = fornecedorMarca;
        atualizarData();
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
        atualizarData();
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
        atualizarData();
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
        atualizarData();
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public LocalDateTime getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public CategoriaPeca getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaPeca categoria) {
        this.categoria = categoria;
        atualizarData();
    }

    public String getTamanho() {
        return tamanho;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
        atualizarData();
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
        atualizarData();
    }

    private void atualizarData() {
        this.dataUltimaAtualizacao = LocalDateTime.now();
    }
    public void setCodigo(Long codigo) { this.codigo = codigo; }
    public void restaurarDatas(LocalDateTime criacao, LocalDateTime atualizacao) {
        this.dataCadastro = criacao;
        this.dataUltimaAtualizacao = atualizacao;
    }
}
