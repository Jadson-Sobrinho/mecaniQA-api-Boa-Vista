package br.com.mecaniQA.api.mapper;

import java.sql.*;
import br.com.mecaniQA.api.model.*;

public final class ServicoResultSetMapper {
    private ServicoResultSetMapper() {}
    public static Servico map(ResultSet rs) throws SQLException {
        Servico s = new Servico(rs.getString("nome"), rs.getInt("tempo_estimado_minutos"), rs.getBigDecimal("custo_tabelado"));
        s.setCodigo(rs.getLong("codigo"));
        s.restaurarDatas(rs.getTimestamp("data_criacao").toLocalDateTime(),
            rs.getTimestamp("data_ultima_atualizacao").toLocalDateTime());
        return s;
    }
}
