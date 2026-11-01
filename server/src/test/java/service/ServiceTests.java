package service;

import static org.junit.jupiter.api.Assertions.*;

import dataaccess.sql.MySQLAuthDAO;
import dataaccess.sql.MySQLGameDAO;
import dataaccess.sql.MySQLUserDAO;
import org.junit.jupiter.api.Test;
import request.*;
import dataaccess.*;
import model.*;
import result.*;
import helpers.TestHelper;

class ServiceTests {
    MySQLAuthDAO authDatabase = null;
    MySQLUserDAO userDatabase = null;
    MySQLGameDAO gameDatabase = null;

    // initialize databases
    {
        try {
            authDatabase = new MySQLAuthDAO();
            userDatabase = new MySQLUserDAO();
            gameDatabase = new MySQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    UserService userService = new UserService(userDatabase, authDatabase);
    GameService gameService = new GameService(authDatabase, gameDatabase);
    ClearService clearService = new ClearService(authDatabase, gameDatabase, userDatabase);

    TestHelper tester = new TestHelper(userService, gameService, clearService);

    @Test
    void testRegisterUser() {
        clearService.clearApplication();
        RegisterResult registerResult = TestHelper.registerUser();
        assertEquals("username", registerResult.username());
    }

    @Test
    void testLoginUser() {
        clearService.clearApplication();
        TestHelper.registerUser();
        LoginResult loginResult = TestHelper.loginUser();
        assertNotNull(loginResult.authToken());
    }

    @Test
    void testLogoutUser() {
        clearService.clearApplication();
        TestHelper.registerUser();
        LoginResult loginResult = TestHelper.loginUser();
        LogoutResult logoutResult = TestHelper.logoutUser(loginResult.authToken());
        assertNull(logoutResult.result());
    }

    @Test
    void testCreateGame() {
        clearService.clearApplication();
        TestHelper.registerUser();
        LoginResult loginResult = TestHelper.loginUser();
        CreateGameResult createGameResult = TestHelper.createGame(loginResult.authToken());
        assertNotNull(createGameResult.gameID());
    }

    @Test
    void testJoinGame() {
        clearService.clearApplication();
        TestHelper.registerUser();
        LoginResult loginResult = TestHelper.loginUser();
        JoinGameResult joinGameResult = TestHelper.joinGame(loginResult.authToken());
        assertNull(joinGameResult.result());
    }

    @Test
    void testListGames() {
        clearService.clearApplication();
        TestHelper.registerUser();
        LoginResult loginResult = TestHelper.loginUser();
        ListGamesResult listGamesResult = TestHelper.listGames(loginResult.authToken());
        assertNotNull(listGamesResult.games());
    }

    @Test
    void testClearApplication() {
        clearService.clearApplication();
        TestHelper.registerUser();
        LoginResult loginResult = TestHelper.loginUser();
        CreateGameResult createGameResult = TestHelper.createGame(loginResult.authToken());
        ClearApplicationResult clearApplicationResult = clearService.clearApplication();
        assertNull(clearApplicationResult.result());
    }

    @Test
    void testIncorrectPasswordThrowsException() {
        clearService.clearApplication();
        TestHelper.registerUser();
        LoginRequest loginRequest = new LoginRequest("username", "wrongpassword");
        assertThrows(DataAccessException.class, () -> userService.login(loginRequest));
    }

}
