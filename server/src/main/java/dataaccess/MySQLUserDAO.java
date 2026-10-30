package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO {

    public MySQLUserDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  userData (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(username),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    @Override
    public void clearUsers() {
        var statement = "TRUNCATE userData";
        try{
            DatabaseManager.executeUpdate(statement);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        String statement = "SELECT username FROM userData";
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                var rs = ps.executeQuery();
                rs.next();
                if (rs.getString("username") == userData.username()) {
                    throw new DataAccessException("already taken");
                }
                else {
                    var updateStatement = "INSERT INTO userData (username, password, email, json) VALUES (?, ?, ?, ?)";
                    String hashedPassword = hashUserPassword(userData.password());
                    try{
                        DatabaseManager.executeUpdate(updateStatement, userData.username(),
                                hashedPassword, userData.email());
                    } catch (DataAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(String.format("unable to retrieve user: %s", e.getMessage()));
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String statement = "SELECT json FROM userData WHERE username = ?";
        var serializer = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                var rs = ps.executeQuery();
                rs.next();
                UserData data = serializer.fromJson(rs.getString("json"), UserData.class);
                return data;
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(String.format("unable to retrieve user: %s", e.getMessage()));
        }
    }

    String hashUserPassword(String clearTextPassword) {
        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
        return hashedPassword;
    }

    boolean verifyPassword(String providedClearTextPassword, String hashedPassword) {
        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }
}
