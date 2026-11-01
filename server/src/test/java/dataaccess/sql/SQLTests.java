package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.sql.MySQLAuthDAO;
import dataaccess.sql.MySQLGameDAO;
import dataaccess.sql.MySQLUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import request.*;
import result.*;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SQLTests {

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

    RegisterResult registerUserSQL() {
        UserData testUserSQL = new UserData("username", "password", "email");
        RegisterRequest registerRequestSQL = new RegisterRequest(testUserSQL.username(), testUserSQL.password(), testUserSQL.email());
        try {
            RegisterResult registerResultSQL = userService.register(registerRequestSQL);
            return registerResultSQL;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    LoginResult loginUserSQL() {
        UserData testUserSQL = new UserData("username", "password", "email");
        LoginRequest loginRequestSQL = new LoginRequest(testUserSQL.username(), testUserSQL.password());
        try {
            LoginResult loginResultSQL = userService.login(loginRequestSQL);
            return loginResultSQL;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    LogoutResult logoutUserSQL(String authToken) {
        AuthData authDataSQL = new AuthData(authToken, null);
        LogoutRequest logoutRequestSQL = new LogoutRequest(authToken);
        try {
            LogoutResult logoutResultSQL = userService.logout(logoutRequestSQL);
            return logoutResultSQL;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    CreateGameResult createGameSQL(String authToken) {
        GameData testGameSQL = new GameData(1, null, null, "myGame");
        CreateGameRequest createGameRequestSQL = new CreateGameRequest(authToken, testGameSQL.gameName());
        try {
            CreateGameResult createGameResultSQL = gameService.createGame(createGameRequestSQL);
            return createGameResultSQL;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    JoinGameResult joinGameSQL(String authToken) {
        CreateGameResult createGameResultSQL= createGameSQL(authToken);
        JoinGameRequest joinGameRequestSQL = new JoinGameRequest(authToken, createGameResultSQL.gameID(), "WHITE");

        try {
            JoinGameResult joinGameResultSQL = gameService.joinGame(joinGameRequestSQL);
            return joinGameResultSQL;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    ListGamesResult listGamesSQL(String authToken) {
        CreateGameResult createGameResultSQL = createGameSQL(authToken);
        ListGamesRequest listGamesRequestSQL = new ListGamesRequest(authToken);
        try {
            ListGamesResult listGamesResultSQL = gameService.listGames(listGamesRequestSQL);
            return listGamesResultSQL;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testRegisterUserSQL() {
        clearService.clearApplication();
        RegisterResult registerResult = registerUserSQL();
        assertEquals("username", registerResult.username());
    }

    @Test
    void testLoginUserSQL() {
        clearService.clearApplication();
        registerUserSQL();
        LoginResult loginResultSQL = loginUserSQL();
        assertNotNull(loginResultSQL.authToken());
    }

    @Test
    void testLogoutUserSQL() {
        clearService.clearApplication();
        registerUserSQL();
        LoginResult loginResultSQL = loginUserSQL();
        LogoutResult logoutResultSQL = logoutUserSQL(loginResultSQL.authToken());
        assertNull(logoutResultSQL.result());
    }

    @Test
    void testCreateGameSQL() {
        clearService.clearApplication();
        registerUserSQL();
        LoginResult loginResult = loginUserSQL();
        CreateGameResult createGameResult = createGameSQL(loginResult.authToken());
        assertNotNull(createGameResult.gameID());
    }

    @Test
    void testJoinGameSQL() {
        clearService.clearApplication();
        registerUserSQL();
        LoginResult loginResult = loginUserSQL();
        JoinGameResult joinGameResult = joinGameSQL(loginResult.authToken());
        assertNull(joinGameResult.result());
    }

    @Test
    void testListGamesSQL() {
        clearService.clearApplication();
        registerUserSQL();
        LoginResult loginResult = loginUserSQL();
        ListGamesResult listGamesResult = listGamesSQL(loginResult.authToken());
        assertNotNull(listGamesResult.games());
    }

    @Test
    void testClearApplicationSQL() {
        clearService.clearApplication();
        registerUserSQL();
        LoginResult loginResult = loginUserSQL();
        CreateGameResult createGameResultSQL = createGameSQL(loginResult.authToken());
        ClearApplicationResult clearApplicationResult = clearService.clearApplication();
        assertNull(clearApplicationResult.result());
    }

    @Test
    void testIncorrectPasswordThrowsExceptionSQL() {
        clearService.clearApplication();
        registerUserSQL();
        LoginRequest loginRequestSQL = new LoginRequest("username", "wrongpassword");
        assertThrows(DataAccessException.class, () -> userService.login(loginRequestSQL));
    }

    @Test
    void testUnauthorizedGameRetrievel() {
        clearService.clearApplication();
        registerUserSQL();
        LoginResult loginResult = loginUserSQL();
        ListGamesRequest request = new ListGamesRequest("12345");
        assertThrows(DataAccessException.class, () -> gameService.listGames(request));
    }
}
