package dataaccess;

import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLGameDAO implements GameDAO {

    public MySQLGameDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameData (
              `id` int NOT NULL AUTO_INCREMENT,
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) NOT NULL DEFAULT NULL,
              `blackUsername` varchar(256) NOT NULL DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(username),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    @Override
    public String createGame(String gameName) {
        String gameID = GameDAO.createGameID();
        var updateStatement = "INSERT INTO gameData (gameID, gameName) VALUES (?, ?)";
        try{
            DatabaseManager.executeUpdate(updateStatement, gameID, gameName);
            return gameID;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        String statement = "SELECT json FROM gameData";
        var serializer = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                var rs = ps.executeQuery();
                List<GameData> data = new ArrayList<>();
                while (rs.next()) {
                    data.add(serializer.fromJson(rs.getString("json"), GameData.class));
                }
                return data;
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(String.format("unable to retrieve user: %s", e.getMessage()));
        }
    }

    @Override
    public void joinGame(String username, String gameID, String playerColor) throws DataAccessException {
        if (playerColor == null) {
            throw new DataAccessException("bad request");
        }
        if (!(playerColor.equals("WHITE") || playerColor.equals("BLACK"))) {
            throw new DataAccessException("bad request");
        }

        String statement = "";
        if (playerColor == "WHITE") {
            statement = "SELECT whiteUsername FROM gameData where gameID = ?";
        }
        else if (playerColor == "BLACK") {
            statement = "SELECT blackUsername FROM gameData where gameID = ?";
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1,gameID);
                var rs = ps.executeQuery();
                rs.next();
                if (playerColor == "WHITE") {
                    if (rs.getString("whiteUsername") != "NULL") {
                        throw new DataAccessException("already taken");
                    }
                }
                else if (playerColor == "BLACK") {
                    if (rs.getString("blackUsername") != "NULL") {
                        throw new DataAccessException("already taken");
                    }
                }
                else {
                    String updateStatement = "";
                    if (playerColor == "WHITE") {
                        updateStatement = "INSERT INTO gameData (whiteUsername) VALUES (?)";
                    }
                    else if (playerColor == "BLACK") {
                        updateStatement = "INSERT INTO gameData (blackUsername) VALUES (?)";
                    }
                    try {
                        DatabaseManager.executeUpdate(updateStatement, username);
                    } catch (DataAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(String.format("unable to join game: %s", e.getMessage()));
        }
    }

    @Override
    public List<Object> getGameData(String gameID) throws DataAccessException {
        String statement = "SELECT whiteUsername, blackUsername, gameName FROM userData WHERE gameId = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, gameID);
                var rs = ps.executeQuery();
                rs.next();
                List<Object> data = new ArrayList<>();
                data.add(gameID);
                data.add(rs.getString("whiteUsername"));
                data.add(rs.getString("blackUsername"));
                data.add(rs.getString("gameName"));
                return data;
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException(String.format("unable to retrieve user: %s", e.getMessage()));
        }
    }

    @Override
    public void clearGames() {
        var statement = "TRUNCATE gameData";
        try{
            DatabaseManager.executeUpdate(statement);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
