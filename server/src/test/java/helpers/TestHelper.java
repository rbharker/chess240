package helpers;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.*;
import result.*;
import service.ClearService;
import service.GameService;
import service.UserService;

public class TestHelper {
    static UserService userService;
    static GameService gameService;
    ClearService clearService;

    public TestHelper(UserService userService, GameService gameService, ClearService clearService) {
        this.clearService = clearService;
        this.gameService = gameService;
        this.userService = userService;
    }

    public static RegisterResult registerUser() {
        UserData testUser = new UserData("username", "password", "email");
        RegisterRequest registerRequest = new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        try {
            RegisterResult registerResult = userService.register(registerRequest);
            return registerResult;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public static LoginResult loginUser() {
        UserData testUser = new UserData("username", "password", "email");
        LoginRequest loginRequest = new LoginRequest(testUser.username(), testUser.password());
        try {
            LoginResult loginResult = userService.login(loginRequest);
            return loginResult;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static LogoutResult logoutUser(String authToken) {
        AuthData authData = new AuthData(authToken, null);
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        try {
            LogoutResult logoutResult = userService.logout(logoutRequest);
            return logoutResult;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static CreateGameResult createGame(String authToken) {
        GameData testGame = new GameData(1, null, null, "myGame");
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, testGame.gameName());
        try {
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            return createGameResult;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static JoinGameResult joinGame(String authToken) {
        CreateGameResult createGameResult = createGame(authToken);
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, createGameResult.gameID(), "WHITE");

        try {
            JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
            return joinGameResult;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static ListGamesResult listGames(String authToken) {
        CreateGameResult createGameResult = createGame(authToken);
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        try {
            ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
            return listGamesResult;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
