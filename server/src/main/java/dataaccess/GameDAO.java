package dataaccess;

import model.*;

import java.util.Collection;

public interface GameDAO {

    public String createGame(String authToken, String gameName, String gameID);

    public Collection<String> getGamesList() throws DataAccessException;

    public String joinGame(String authToken, String gameID, String playerColor) throws DataAccessException;

}