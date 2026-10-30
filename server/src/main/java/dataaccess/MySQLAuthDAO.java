package dataaccess;

import model.AuthData;

import java.sql.*;

public class MySQLAuthDAO implements AuthDAO {

    public MySQLAuthDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authData (
              `id` int NOT NULL AUTO_INCREMENT,
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(authtoken),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    @Override
    public void clearAuth() {
        var statement = "TRUNCATE authData";
        try{
            DatabaseManager.executeUpdate(statement);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createAuth(AuthData data) {
        String authToken = AuthDAO.generateToken();
        var statement = "INSERT INTO authData (authToken, username, json) VALUES (?, ?, ?)";
        try{
            DatabaseManager.executeUpdate(statement, authToken, data.username(), data);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return authToken;
    }

    @Override
    public String getUserByAuth(String authToken) throws DataAccessException {
        String statement = "SELECT username FROM authData WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                var rs = ps.executeQuery();
                rs.next();
                return rs.getString("username");
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(String.format("unable to retrieve user: %s", e.getMessage()));
        }
    }

    @Override
    public String deleteAuth(String authToken) {
        var statement = "DELETE authToken, username FROM authData WHERE authToken = ?";
        try{
            DatabaseManager.executeUpdate(statement, authToken);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public String checkAuthToken(String authToken) {
        var statement = "SELECT authToken FROM authData WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                var rs = ps.executeQuery();
                rs.next();
                return rs.getString("authToken");
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

