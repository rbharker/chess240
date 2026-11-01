package dataaccess.sql;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import kotlin.text.UStringsKt;
import model.GameData;

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
              `gameID` int NOT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(gameID)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    @Override
    public String createGame(String gameName) {
        String gameID = GameDAO.createGameID();
        GameData data = new GameData(UStringsKt.toUInt(gameID), null, null, gameName);
        Gson serializer = new Gson();
        var updateStatement = "INSERT INTO gameData (gameID, gameName, json) VALUES (?, ?, ?)";
        try{
            DatabaseManager.executeUpdate(updateStatement, gameID, gameName, serializer.toJson(data));
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
        } catch (DataAccessException e) {
            throw new DataAccessException("game does not exist");
        } catch (SQLException e) {
            throw new RuntimeException("bad request");
        }
    }

    @Override
    public void joinGame(String username, String gameID, String playerColor) throws DataAccessException {
        Gson serializer = new Gson();

        // check the color is not null
        if (playerColor == null) {
            throw new DataAccessException("bad request");
        }
        if (!(playerColor.equals("WHITE") || playerColor.equals("BLACK"))) {
            throw new DataAccessException("bad request");
        }

        // check if the color is already being played
        String statement = "";
        if (playerColor.equals("WHITE")) {
            statement = "SELECT whiteUsername FROM gameData where gameID = ?";
        }
        else if (playerColor.equals("BLACK")) {
            statement = "SELECT blackUsername FROM gameData where gameID = ?";
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1,gameID);
                var rs = ps.executeQuery();
                if (rs.next()) {
                    if (playerColor.equals("WHITE") && rs.getString("whiteUsername") != null) {
                        throw new DataAccessException("already taken");
                    }
                    else if (playerColor.equals("BLACK") && rs.getString("blackUsername") != null) {
                        throw new DataAccessException("already taken");
                    }
                }
            }
        } catch (DataAccessException e) {
            throw e;
        } catch (SQLException e) {
            throw new RuntimeException("bad request");
        }

        // if the color is not being played
        String updateStatement = "";
        GameData updated = null;
        String gameNameStatement = "SELECT json FROM gameData where gameID = ?";
        String json = "";
        try (var conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(gameNameStatement)) {
                ps.setString(1, gameID);
                var rs = ps.executeQuery();
                rs.next();
                json = rs.getString("json");
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("bad request");
        }

        GameData oldData = serializer.fromJson(json, GameData.class);

        if (playerColor.equals("WHITE")) {
            updateStatement = "UPDATE gameData SET whiteUsername = ?, json = ? WHERE gameID = ?";
            updated = new GameData(oldData.gameID(), username, oldData.blackUsername(), oldData.gameName());
        }
        else if (playerColor.equals("BLACK")) {
            updateStatement = "UPDATE gameData SET blackUsername = ?, json = ? WHERE gameID = ?";
            updated = new GameData(oldData.gameID(), oldData.whiteUsername(), username, oldData.gameName());
        }
        try {
            DatabaseManager.executeUpdate(updateStatement, username, serializer.toJson(updated), gameID);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
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
            throw new DataAccessException("game does not exist");
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
