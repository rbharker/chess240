package dataaccess;

import model.*;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public interface GameDAO {

    // adds a new game and its data to the database
    String createGame(String gameName);

    // returns a list of all active games
    List<GameData> listGames() throws DataAccessException;

    // adds a user to the specified team and game if possible
    void joinGame(String username, String gameID, String playerColor) throws DataAccessException;

    // returns the corresponding game data if it exists
    List<Object> getGameData(String gameID) throws DataAccessException;

    // creates a random gameID
    static String createGameID() {
        Random r = new Random();
        int randNum = r.nextInt(1000);
        return Integer.toString(randNum);
    }

    // clears the database of all games
    void clearGames();

}