package dataaccess;

import model.*;

import java.util.Collection;

public interface GameDAO {

    // adds a new game and its data to the database
    public String createGame(String authToken, String gameName, String gameID);

    // returns a list of all active games
    public Collection<String> getGamesList() throws DataAccessException;

    // adds a user to the specified team and game if possible
    public String joinGame(String authToken, String gameID, String playerColor) throws DataAccessException;

}