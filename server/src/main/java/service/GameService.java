package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

import java.util.Collection;
import java.util.List;

public class GameService {

    private MemoryAuthDAO authDAO;
    private MemoryGameDAO gameDAO;

    public GameService(MemoryAuthDAO authDAO, MemoryGameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        String username = authDAO.checkAuthToken(listGamesRequest.authToken());
        if (username == null) {
            throw new DataAccessException("Invalid request.");
        }
        List<List<String>> gamesList = gameDAO.getGamesList();
        ListGamesResult result = new ListGamesResult(gamesList);
        return result;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        String username = authDAO.checkAuthToken(createGameRequest.authToken());
        if (username == null) {
            throw new DataAccessException("Invalid request.");
        }
        String gameID = gameDAO.createGame(createGameRequest.gameName());
        CreateGameResult result = new CreateGameResult(gameID);
        return result;
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        String username = authDAO.checkAuthToken(joinGameRequest.authToken());
        if (username == null) {
            throw new DataAccessException("Invalid request.");
        }
        try {
            gameDAO.joinGame(username, joinGameRequest.gameID(), joinGameRequest.playerColor());
        }
        catch (DataAccessException e) {
            throw new DataAccessException("Invalid request");
        }
    }

}
