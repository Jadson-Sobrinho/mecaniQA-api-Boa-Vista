package br.com.mecaniQA.api.service;

import java.math.BigDecimal;
import br.com.mecaniQA.api.exception.DadosInvalidosException;

final class Validacao {
    private Validacao() {}
    static void id(Long id) { if (id == null || id <= 0) throw new DadosInvalidosException("Código deve ser positivo."); }
    static void required(Object v, String field) { if (v == null) throw new DadosInvalidosException(field + " é obrigatório."); }
    static void text(String v, String field, int max) {
        if (v == null || v.isBlank() || v.length() > max)
            throw new DadosInvalidosException(field + " é obrigatório e deve ter até " + max + " caracteres.");
    }
    static void optional(String v, String field, int max) {
        if (v != null && v.length() > max) throw new DadosInvalidosException(field + " deve ter até " + max + " caracteres.");
    }
    static void money(BigDecimal v, String field) {
        if (v == null || v.signum() < 0 || v.compareTo(new BigDecimal("9999999999.99")) > 0 || v.stripTrailingZeros().scale() > 2)
            throw new DadosInvalidosException(field + " deve ser informado, não negativo e ter até duas casas decimais (máximo 9999999999.99).");
    }
}
