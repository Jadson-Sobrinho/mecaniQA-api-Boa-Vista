package br.com.mecaniQA.api.mapper;

import java.sql.*;
import br.com.mecaniQA.api.model.*;

public final class PecaResultSetMapper {
    private PecaResultSetMapper() {}
    public static Peca map(ResultSet rs) throws SQLException {
        Peca p = new Peca(rs.getString("nome"), rs.getString("codigo_barras"),
            rs.getString("fornecedor_marca"), rs.getInt("quantidade_estoque"),
            rs.getBigDecimal("preco_custo"), rs.getBigDecimal("preco_venda"),
            CategoriaPeca.valueOf(rs.getString("categoria")));
        p.setCodigo(rs.getLong("codigo")); p.setTamanho(rs.getString("tamanho")); p.setCor(rs.getString("cor"));
        p.restaurarDatas(rs.getTimestamp("data_cadastro").toLocalDateTime(),
            rs.getTimestamp("data_ultima_atualizacao").toLocalDateTime());
        return p;
    }
}
