package br.com.mecaniQA.api.repository;

import java.sql.*;
import java.util.*;
import br.com.mecaniQA.api.config.ConnectionFactory;
import br.com.mecaniQA.api.exception.RecursoNaoEncontradoException;

                                                                                   
public abstract class JdbcSupport {
    protected final ConnectionFactory db;
    protected JdbcSupport(ConnectionFactory db) { this.db = db; }
    @FunctionalInterface protected interface Mapper<T> { T map(ResultSet rs) throws SQLException; }
    protected static void bind(PreparedStatement ps, Object... args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            Object value = args[i];
            if (value instanceof Enum<?> e) value = e.name();
            if (value instanceof java.time.LocalDateTime date) value = Timestamp.valueOf(date);
            ps.setObject(i + 1, value);
        }
    }
    protected <T> List<T> query(String sql, Mapper<T> mapper, Object... args) {
        return db.execute(c -> {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                bind(ps, args);
                try (ResultSet rs = ps.executeQuery()) {
                    List<T> result = new ArrayList<>();
                    while (rs.next()) result.add(mapper.map(rs));
                    return result;
                }
            }
        });
    }
    protected int execute(String sql, Object... args) {
        return db.execute(c -> {
            try (PreparedStatement ps = c.prepareStatement(sql)) { bind(ps,args); return ps.executeUpdate(); }
        });
    }
    protected long insert(String sql, Object... args) {
        return db.execute(c -> {
            try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                bind(ps,args); ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("Chave não retornada pelo banco.");
                    return rs.getLong(1);
                }
            }
        });
    }
    protected void requireAffected(int count) {
        if (count == 0) throw new RecursoNaoEncontradoException("Registro não encontrado.");
    }
}
