package dataaccess;

import org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    HashMap<String, List<String>> dataCollection = new HashMap<>();

    // adds a new game and its data to the database
    @Override
    public String createGame(String gameName) {
        // white player first
        // black player second
        List<String> data = Arrays.asList(gameName, null, null);
        String gameID = GameDAO.createGameID();
        dataCollection.put(gameID, data);
        return gameID;
    }

    // returns a list of all active games
    @Override
    public Collection<Collection<String>> getGamesList() throws DataAccessException {
        Collection<Collection<String>> gameList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : dataCollection.entrySet()) {
            Collection<String> data = new ArrayList<>();
            data.addAll(entry.getValue());
            gameList.add(data);
        }
        return gameList;
    }

    @Override
    public List<String> getGameData(String gameID) throws DataAccessException {
        if (!dataCollection.containsKey(gameID)) {
            throw new DataAccessException("Game does not exist!");
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
    public String joinGame(String username, String gameID, String playerColor) throws DataAccessException {
        if (!dataCollection.containsKey(gameID)) {
            throw new DataAccessException("Game does not exist!");
        }
        else {
            var data = getGameData(gameID);
            if ((playerColor == "white") && (data.get(1) == null)) {
                dataCollection.get(gameID).add(1, username);
                return gameID;
            }
            else if ((playerColor == "black") && (data.get(2) == null)) {
                dataCollection.get(gameID).add(2, username);
                return gameID;
            }
            else {
                throw new DataAccessException("Invalid request");
            }
        }
    }
}
