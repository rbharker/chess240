package dataaccess;

import model.*;

import java.util.Collection;
import java.util.Random;

public interface GameDAO {

    // adds a new game and its data to the database
    String createGame(String authToken, String gameName);

    // returns a list of all active games
    Collection<Collection<String>> getGamesList() throws DataAccessException;

    // adds a user to the specified team and game if possible
    String joinGame(String authToken, String gameID, String playerColor) throws DataAccessException;

    static String createGameID() {
        Random r = new Random();
        int randNum = r.nextInt(1000);
        return Integer.toString(randNum);
    }

}