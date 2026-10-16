package dataaccess;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    HashMap<String, List<String>> dataCollection = new HashMap<>();

    // adds a new game and its data to the database
    @Override
    public String createGame(String authToken, String gameName) {
        List<String> data = Arrays.asList(authToken, gameName);
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

    // adds a user to the specified team and game if possible
    @Override
    public String joinGame(String authToken, String gameID, String playerColor) throws DataAccessException {
        return "";
    }
}
