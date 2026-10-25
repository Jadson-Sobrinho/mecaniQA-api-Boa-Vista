package br.com.mecaniQA.api.mapper;

import java.sql.*;
import br.com.mecaniQA.api.model.*;

public final class OrdemServicoResultSetMapper {
    private OrdemServicoResultSetMapper() {}
    public static OrdemServico map(ResultSet rs) throws SQLException { return new OrdemServico.Builder().codigo(rs.getLong("codigo")).descricao(rs.getString("descricao"))
            .status(StatusOrdemServico.valueOf(rs.getString("status"))).build(); }
}
