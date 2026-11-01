package service;

import dataaccess.*;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;

import java.util.List;

public class GameService {

    private MySQLAuthDAO authDAO;
    private MySQLGameDAO gameDAO;

    public GameService(MySQLAuthDAO authDAO, MySQLGameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        String authToken = "";
        try {
            authToken = authDAO.checkAuthToken(listGamesRequest.authToken());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("failed to get connection")) {
                throw new RuntimeException(e.getMessage());
            }
            throw new DataAccessException("unauthorized");
        }
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

        String authToken = "";
        try {
            authToken = authDAO.checkAuthToken(createGameRequest.authToken());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("failed to get connection")) {
                throw new RuntimeException(e.getMessage());
            }
            throw new DataAccessException("unauthorized");
        }
        if (authToken == null) {
            throw new DataAccessException("unauthorized");
        }
        String gameID = gameDAO.createGame(createGameRequest.gameName());
        CreateGameResult result = new CreateGameResult(gameID);
        return result;
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        String authToken = "";
        try {
            authToken = authDAO.checkAuthToken(joinGameRequest.authToken());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("failed to get connection")) {
                throw e;
            }
            throw new DataAccessException("unauthorized");
        }
        if (authToken == null) {
            throw new DataAccessException("unauthorized");
        }
        try {
            String username = authDAO.getUserByAuth(authToken);
            gameDAO.joinGame(username, joinGameRequest.gameID(), joinGameRequest.playerColor());
            return new JoinGameResult(null);
        }
        catch (DataAccessException e) {
            throw e;
        }
    }

}
