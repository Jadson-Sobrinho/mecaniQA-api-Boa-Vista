
package br.com.mecaniQA.api.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.com.mecaniQA.api.config.ConnectionFactory;
import br.com.mecaniQA.api.exception.RecursoNaoEncontradoException;

public abstract class JdbcSupport {

    protected final ConnectionFactory db;

    protected JdbcSupport(ConnectionFactory db) {
        this.db = db;
    }

    @FunctionalInterface
    protected interface Mapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    @FunctionalInterface
    protected interface JdbcOperation<T> {
        T execute(Connection connection) throws SQLException;
    }

    protected final <T> T executarTemplate(
            String sql,
            JdbcOperation<T> operacao) {

        return db.execute(connection -> {
            try {
                beforeOperation(connection, sql);
                return operacao.execute(connection);
            } finally {
                afterOperation(connection, sql);
            }
        });
    }

    protected void beforeOperation(
            Connection connection,
            String sql) throws SQLException {
    }

    protected void afterOperation(
            Connection connection,
            String sql) {
    }

    protected static void bind(
            PreparedStatement ps,
            Object... args) throws SQLException {

        for (int i = 0; i < args.length; i++) {
            Object value = args[i];

            if (value instanceof Enum<?> e) {
                value = e.name();
            }

            if (value instanceof java.time.LocalDateTime date) {
                value = Timestamp.valueOf(date);
            }

            ps.setObject(i + 1, value);
        }
    }

    protected final <T> List<T> query(
            String sql,
            Mapper<T> mapper,
            Object... args) {

        return executarTemplate(sql, connection -> {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                bind(ps, args);

                try (ResultSet rs = ps.executeQuery()) {
                    List<T> result = new ArrayList<>();

                    while (rs.next()) {
                        result.add(mapper.map(rs));
                    }

                    return result;
                }
            }
        });
    }

    protected final int execute(String sql, Object... args) {
        return executarTemplate(sql, connection -> {
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                bind(ps, args);
                return ps.executeUpdate();
            }
        });
    }

    protected final long insert(String sql, Object... args) {
        return executarTemplate(sql, connection -> {
            try (PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS)) {

                bind(ps, args);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new SQLException(
                                "Chave não retornada pelo banco.");
                    }

                    return rs.getLong(1);
                }
            }
        });
    }

    protected final void requireAffected(int count) {
        if (count == 0) {
            throw new RecursoNaoEncontradoException(
                    "Registro não encontrado.");
        }
    }
}
