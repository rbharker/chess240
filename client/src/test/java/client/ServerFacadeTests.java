package client;

import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.*;
import request.*;
import result.CreateGameResult;
import server.Server;
import server.ServerFacade;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    AuthData authData = null;

    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverUrl = "http://localhost:" + port;
        facade = new ServerFacade(serverUrl);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @Order(1)
    public void testRegisterUser() throws ResponseException {
        facade.clearApplication();
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test");
        Assertions.assertDoesNotThrow(() -> facade.registerUser(registerRequest));
    }

    @Test
    @Order(2)
    public void testRegisterSameUser() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test");
        Assertions.assertThrows(ResponseException.class, () -> facade.registerUser(registerRequest));
    }

    @Test
    @Order(3)
    public void testLoginUser() throws ResponseException {
        facade.clearApplication();
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test");
        facade.registerUser(registerRequest);
        LoginRequest loginRequest = new LoginRequest("test", "test");
        authData = Assertions.assertDoesNotThrow(() -> facade.loginUser(loginRequest));
    }

    @Test
    @Order(4)
    public void testLoginUserTwice() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("test", "test");
        Assertions.assertDoesNotThrow(() -> facade.loginUser(loginRequest));
    }

    @Test
    @Order(5)
    public void testLogoutUser() throws ResponseException {
        facade.clearApplication();
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test");
        facade.registerUser(registerRequest);
        LoginRequest loginRequest = new LoginRequest("test", "test");
        authData = facade.loginUser(loginRequest);
        LogoutRequest logoutRequest = new LogoutRequest(authData.authToken());
        Assertions.assertDoesNotThrow(() -> facade.logoutUser(logoutRequest));
    }

    @Test
    @Order(6)
    public void testCreateGame() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("test", "test");
        authData = facade.loginUser(loginRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest(authData.authToken(), "game");
        Assertions.assertDoesNotThrow(() -> facade.createGame(createGameRequest));
        LogoutRequest logoutRequest = new LogoutRequest(authData.authToken());
        facade.logoutUser(logoutRequest);
    }

    @Test
    @Order(7)
    public void testJoinGame() throws ResponseException {
        facade.clearApplication();
        RegisterRequest registerRequest = new RegisterRequest("test", "test", "test");
        facade.registerUser(registerRequest);
        LoginRequest loginRequest = new LoginRequest("test", "test");
        authData = facade.loginUser(loginRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest(authData.authToken(), "game2");
        CreateGameResult createGameResult = facade.createGame(createGameRequest);
        JoinGameRequest joinGameRequest = new JoinGameRequest(authData.authToken(), createGameResult.gameID(), "WHITE");
        Assertions.assertDoesNotThrow(() -> facade.joinGame(joinGameRequest));
        LogoutRequest logoutRequest = new LogoutRequest(authData.authToken());
        facade.logoutUser(logoutRequest);
    }

    @Test
    @Order(8)
    public void testListGames() throws ResponseException {
        LoginRequest loginRequest = new LoginRequest("test", "test");
        authData = facade.loginUser(loginRequest);
        CreateGameRequest createGameRequest = new CreateGameRequest(authData.authToken(), "game2");
        facade.createGame(createGameRequest);
        ListGamesRequest listGamesRequest = new ListGamesRequest(authData.authToken());
        Assertions.assertDoesNotThrow(() -> facade.listGames(listGamesRequest));
        LogoutRequest logoutRequest = new LogoutRequest(authData.authToken());
        facade.logoutUser(logoutRequest);
    }

}