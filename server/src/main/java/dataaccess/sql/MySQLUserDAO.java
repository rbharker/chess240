package dataaccess.sql;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
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
              INDEX(username)
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
        Gson serializer = new Gson();
        String statement = "SELECT username FROM userData where username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            // check if the username already exists
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, userData.username());
                var rs = ps.executeQuery();
                if (rs.next()) {
                    throw new DataAccessException("already taken");
                }
                else {
                    var updateStatement = "INSERT INTO userData (username, password, email, json) VALUES (?, ?, ?, ?)";
                    String hashedPassword = hashUserPassword(userData.password());
                    UserData updatedData = new UserData(userData.username(), hashedPassword, userData.email());
                    try{
                        DatabaseManager.executeUpdate(updateStatement, userData.username(),
                                hashedPassword, userData.email(), serializer.toJson(updatedData));
                    } catch (DataAccessException e2) {
                        throw new RuntimeException(e2);
                    }
                }
            } catch (DataAccessException e) {
                throw new DataAccessException("already taken");
            }
        } catch (SQLException e) {
            throw new RuntimeException("already taken");
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
                if (rs.next()) {
                    UserData data = serializer.fromJson(rs.getString("json"), UserData.class);
                    return data;
                }
                else {
                    throw new DataAccessException("unauthorized");
                }
            }
        } catch (DataAccessException | SQLException e) {
            if (e.getMessage().contains("connection")) {
                throw new RuntimeException(e.getMessage());
            }
            throw new DataAccessException("unauthorized");
        }
    }

    String hashUserPassword(String clearTextPassword) {
        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
        return hashedPassword;
    }

    public boolean verifyPassword(String providedClearTextPassword, String hashedPassword) {
        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }
}
