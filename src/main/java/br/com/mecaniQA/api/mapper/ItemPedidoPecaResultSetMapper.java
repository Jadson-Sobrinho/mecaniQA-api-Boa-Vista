package br.com.mecaniQA.api.mapper;

import java.sql.*;
import br.com.mecaniQA.api.model.*;

public final class ItemPedidoPecaResultSetMapper {
    private ItemPedidoPecaResultSetMapper() {}
    public static ItemPedidoPeca map(ResultSet rs) throws SQLException { return new ItemPedidoPeca.Builder().codigo(rs.getLong("item_codigo"))
            .quantidade(rs.getInt("quantidade")).peca(PecaResultSetMapper.map(rs)).build(); }
}
