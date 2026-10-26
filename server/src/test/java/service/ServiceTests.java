package service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import request.*;
import dataaccess.*;
import model.*;
import result.*;

class ServiceTest {
    MemoryAuthDAO authDatabase = new MemoryAuthDAO();
    MemoryUserDAO userDatabase = new MemoryUserDAO();
    MemoryGameDAO gameDatabase = new MemoryGameDAO();

    UserService userService = new UserService(userDatabase, authDatabase);
    GameService gameService = new GameService(authDatabase, gameDatabase);
    ClearService clearService = new ClearService(authDatabase, gameDatabase, userDatabase);

    RegisterResult registerUser() {
        UserData testUser = new UserData("username", "password", "email");
        RegisterRequest registerRequest = new RegisterRequest(testUser.username(), testUser.password(), testUser.email());
        try {
            RegisterResult registerResult = userService.register(registerRequest);
            return registerResult;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    LoginResult loginUser() {
        UserData testUser = new UserData("username", "password", "email");
        LoginRequest loginRequest = new LoginRequest(testUser.username(), testUser.password());
        try {
            LoginResult loginResult = userService.login(loginRequest);
            return loginResult;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    LogoutResult logoutUser(String authToken) {
        AuthData authData = new AuthData(authToken, null);
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        try {
            LogoutResult logoutResult = userService.logout(logoutRequest);
            return logoutResult;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    CreateGameResult createGame(String authToken) {
        GameData testGame = new GameData(1, null, null, "myGame");
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, testGame.gameName());
        try {
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            return createGameResult;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    JoinGameResult joinGame(String authToken) {
        CreateGameResult createGameResult = createGame(authToken);
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, createGameResult.gameID(), "WHITE");

        try {
            JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
            return joinGameResult;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    ListGamesResult listGames(String authToken) {
        CreateGameResult createGameResult = createGame(authToken);
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        try {
            ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
            return listGamesResult;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRegisterUser() {
        RegisterResult registerResult = registerUser();
        assertEquals("username", registerResult.username());
    }

    @Test
    void testLoginUser() {
        registerUser();
        LoginResult loginResult = loginUser();
        assertNotNull(loginResult.authToken());
    }

    @Test
    void testLogoutUser() {
        registerUser();
        LoginResult loginResult = loginUser();
        LogoutResult logoutResult = logoutUser(loginResult.authToken());
        assertNull(logoutResult.result());
    }

    @Test
    void testCreateGame() {
        registerUser();
        LoginResult loginResult = loginUser();
        CreateGameResult createGameResult = createGame(loginResult.authToken());
        assertNotNull(createGameResult.gameID());
    }

    @Test
    void testJoinGame() {
        registerUser();
        LoginResult loginResult = loginUser();
        JoinGameResult joinGameResult = joinGame(loginResult.authToken());
        assertNull(joinGameResult.result());
    }

    @Test
    void testListGames() {
        registerUser();
        LoginResult loginResult = loginUser();
        ListGamesResult listGamesResult = listGames(loginResult.authToken());
        assertNotNull(listGamesResult.games());
    }

    @Test
    void testClearApplication() {
        registerUser();
        LoginResult loginResult = loginUser();
        CreateGameResult createGameResult = createGame(loginResult.authToken());
        ClearApplicationResult clearApplicationResult = clearService.clearApplication();
        assertNull(clearApplicationResult.result());
    }

    @Test
    void testIncorrectPasswordThrowsException() {
        registerUser();
        LoginRequest loginRequest = new LoginRequest("username", "wrongpassword");
        assertThrows(DataAccessException.class, () -> userService.login(loginRequest));
    }

}
