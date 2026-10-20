package br.com.mecaniQA.api.config;

import java.sql.*;
import java.util.function.Supplier;
import br.com.mecaniQA.api.exception.*;

   
                                                                                  
  
                                                                                                
                                                                                                
                                                                                          
                                                                                   
   
public class ConnectionFactory {
    private static final String URL_PADRAO =
        "jdbc:mysql://localhost:3306/mecaniqa?useSSL=false&allowPublicKeyRetrieval=true&connectionTimeZone=LOCAL";
    private static final String USUARIO_PADRAO = "mecaniqa";

    private static ConnectionFactory instance;

    private final String url, user, password;
    private final ThreadLocal<Connection> current = new ThreadLocal<>();

    private ConnectionFactory() {
        this.url = variavel("DB_URL", URL_PADRAO);
        this.user = variavel("DB_USER", USUARIO_PADRAO);
        this.password = variavel("DB_PASSWORD", "");
    }

    public static synchronized ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    private static String variavel(String nome, String padrao) {
        String valor = System.getenv(nome);
        return (valor == null || valor.isBlank()) ? padrao : valor;
    }

    public String getUrl() { return url; }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    @FunctionalInterface
    public interface SqlWork<T> { T run(Connection connection) throws SQLException; }

    public <T> T execute(SqlWork<T> work) {
        Connection existing = current.get();
        if (existing != null) {
            try { return work.run(existing); } catch (SQLException e) { throw translate(e); }
        }
        try (Connection connection = getConnection()) { return work.run(connection); }
        catch (SQLException e) { throw translate(e); }
    }

    public <T> T transaction(Supplier<T> work) {
        if (current.get() != null) return work.get();
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            current.set(connection);
            try {
                T result = work.get();
                connection.commit();
                return result;
            } catch (RuntimeException | Error | SQLException e) {
                try { connection.rollback(); } catch (SQLException rollback) { e.addSuppressed(rollback); }
                if (e instanceof SQLException sql) throw translate(sql);
                throw e;
            } finally { current.remove(); }
        } catch (SQLException e) { throw translate(e); }
    }

    private RuntimeException translate(SQLException e) {
        if (e.getSQLState() != null && e.getSQLState().startsWith("23"))
            return new ConflitoDadosException("Operação conflita com dados existentes ou vinculados.", e);
        return new PersistenciaException("Não foi possível acessar o banco de dados.", e);
    }
}
