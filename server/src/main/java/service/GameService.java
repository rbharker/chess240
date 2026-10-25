package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.GameData;

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
        String authToken = authDAO.checkAuthToken(listGamesRequest.authToken());
        if (authToken == null) {
            throw new DataAccessException("unauthorized");
        }
        List<GameData> games = gameDAO.listGames();
        ListGamesResult result = new ListGamesResult(games);
        return result;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        if (createGameRequest.gameName() == null) {
            throw new DataAccessException("bad request");
        }

        String authToken = authDAO.checkAuthToken(createGameRequest.authToken());
        if (authToken == null) {
            throw new DataAccessException("unauthorized");
        }
        String gameID = gameDAO.createGame(createGameRequest.gameName());
        CreateGameResult result = new CreateGameResult(gameID);
        return result;
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        String authToken = authDAO.checkAuthToken(joinGameRequest.authToken());
        if (authToken == null) {
            throw new DataAccessException("unauthorized");
        }
        try {
            String username = authDAO.getUserByAuth(authToken);
            gameDAO.joinGame(username, joinGameRequest.gameID(), joinGameRequest.playerColor());
        }
        catch (DataAccessException e) {
            throw e;
        }
    }

}
