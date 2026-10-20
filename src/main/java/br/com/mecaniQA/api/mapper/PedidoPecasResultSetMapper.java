package br.com.mecaniQA.api.mapper;

import java.sql.*;
import br.com.mecaniQA.api.model.*;

public final class PedidoPecasResultSetMapper {
    private PedidoPecasResultSetMapper() {}
    public static PedidoPecas map(ResultSet rs) throws SQLException { return new PedidoPecas.Builder().codigo(rs.getLong("codigo"))
            .status(StatusPedidoPecas.valueOf(rs.getString("status"))).build(); }
}
