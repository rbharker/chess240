package dataaccess;
import kotlin.text.UStringsKt;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    HashMap<String, List<String>> dataCollection = new HashMap<>();

    // adds a new game and its data to the database
    @Override
    public String createGame(String gameName) {
        // white player first
        // black player second
        List<String> data = new ArrayList<>(Arrays.asList(null, null, gameName));
        String gameID = GameDAO.createGameID();
        dataCollection.put(gameID, data);
        return gameID;
    }

    // returns a list of all active games
    @Override
    public List<GameData> listGames() {
        List<GameData> gameList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : dataCollection.entrySet()) {
            String gameId = entry.getKey();
            GameData game = new GameData(Integer.parseInt(gameId), entry.getValue().get(0), entry.getValue().get(1), entry.getValue().get(2));
            gameList.add(game);
        }
        return gameList;
    }

    @Override
    public List<String> getGameData(String gameID) throws DataAccessException {
        if (!dataCollection.containsKey(gameID)) {
            throw new DataAccessException("game does not exist");
        }
        else {
            List<String> data = new ArrayList<>();
            data.add(gameID);
            data.addAll(dataCollection.get(gameID));
            return data;
        }
    }

    // adds a user to the specified team and game if possible
    @Override
    public void joinGame(String username, String gameID, String playerColor) throws DataAccessException {
        if (!dataCollection.containsKey(gameID)) {
            throw new DataAccessException("bad request");
        }
        else {
            var data = dataCollection.get(gameID);
            if (playerColor == null) {
                throw new DataAccessException("bad request");
            }
            if (!(playerColor.equals("WHITE") || playerColor.equals("BLACK"))) {
                throw new DataAccessException("bad request");
            }
            if ((playerColor.equals("WHITE")) && (data.get(0) == null)) {
                dataCollection.get(gameID).set(0, username);
            }
            else if ((playerColor.equals("BLACK")) && (data.get(1) == null)) {
                dataCollection.get(gameID).set(1, username);
            }
            else {
                throw new DataAccessException("already taken");
            }
        }
    }

    // clears the database of all games
    @Override
    public void clearGames() {
        dataCollection.clear();
    }
}
