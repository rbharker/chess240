package dataaccess;
import java.util.*;

public class MemoryGameDAO implements GameDAO {
    HashMap<String, List<String>> dataCollection = new HashMap<>();

    // adds a new game and its data to the database
    @Override
    public String createGame(String gameName) {
        // white player first
        // black player second
        List<String> data = new ArrayList<>(Arrays.asList(gameName, null, null));
        String gameID = GameDAO.createGameID();
        dataCollection.put(gameID, data);
        return gameID;
    }

    // returns a list of all active games
    @Override
    public List<List<String>> getGamesList() throws DataAccessException {
        List<List<String>> gameList = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : dataCollection.entrySet()) {
            List<String> data = new ArrayList<>();
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
    public void joinGame(String username, String gameID, String playerColor) throws DataAccessException {
        if (!dataCollection.containsKey(gameID)) {
            throw new DataAccessException("Game does not exist!");
        }
        else {
            var data = dataCollection.get(gameID);
            if ((playerColor.equals("WHITE")) && (data.get(1) == null)) {
                dataCollection.get(gameID).set(1, username);
            }
            else if ((playerColor.equals("BLACK")) && (data.get(2) == null)) {
                dataCollection.get(gameID).set(2, username);
            }
            else {
                throw new DataAccessException("Colors taken!");
            }
        }
    }

    // clears the database of all games
    @Override
    public void clearGames() {
        dataCollection.clear();
    }
}
