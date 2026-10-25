package br.com.mecaniQA.api.dto;

import br.com.mecaniQA.api.model.CategoriaPeca;

import java.math.BigDecimal;

public class PecaDTO {

    private Long codigo;
    private String nome;
    private String codigoBarras;
    private String fornecedorMarca;
    private Integer quantidadeEstoque;
    private BigDecimal precoCusto;
    private BigDecimal precoVenda;
    private CategoriaPeca categoria;
    private String tamanho;
    private String cor;

    public PecaDTO() {
    }

    public PecaDTO(
            Long codigo,
            String nome,
            String codigoBarras,
            String fornecedorMarca,
            Integer quantidadeEstoque,
            BigDecimal precoCusto,
            BigDecimal precoVenda,
            CategoriaPeca categoria,
            String tamanho,
            String cor) {

        this.codigo = codigo;
        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.fornecedorMarca = fornecedorMarca;
        this.quantidadeEstoque = quantidadeEstoque;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.categoria = categoria;
        this.tamanho = tamanho;
        this.cor = cor;
    }

    public Long getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public String getFornecedorMarca() {
        return fornecedorMarca;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public CategoriaPeca getCategoria() {
        return categoria;
    }

    public String getTamanho() {
        return tamanho;
    }

    public String getCor() {
        return cor;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public void setFornecedorMarca(String fornecedorMarca) {
        this.fornecedorMarca = fornecedorMarca;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public void setCategoria(CategoriaPeca categoria) {
        this.categoria = categoria;
    }

    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
}