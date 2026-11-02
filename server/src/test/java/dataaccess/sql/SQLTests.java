package dataaccess.sql;

import com.mysql.cj.util.TestUtils;
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
import helpers.TestHelper;

import java.lang.annotation.Annotation;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SQLTests {

    MySQLAuthDAO authDatabaseSQL = null;
    MySQLUserDAO userDatabaseSQL = null;
    MySQLGameDAO gameDatabaseSQL = null;

    // initialize databases
    {
        try {
            authDatabaseSQL = new MySQLAuthDAO();
            userDatabaseSQL = new MySQLUserDAO();
            gameDatabaseSQL = new MySQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    UserService userServiceSQL = new UserService(userDatabaseSQL, authDatabaseSQL);
    GameService gameServiceSQL = new GameService(authDatabaseSQL, gameDatabaseSQL);
    ClearService clearServiceSQL = new ClearService(authDatabaseSQL, gameDatabaseSQL, userDatabaseSQL);

    TestHelper tester = new TestHelper(userServiceSQL, gameServiceSQL, clearServiceSQL);

    @Test
    void testCreateAuthReturnsAuth() {
        clearServiceSQL.clearApplication();
        AuthData data = new AuthData(null, "username");
        String authToken = authDatabaseSQL.createAuth(data);
        assertNotNull(authToken);
    }

    @Test
    void testGetUserByAuthReturnsUser() {
        clearServiceSQL.clearApplication();
        TestHelper.registerUser();
        LoginResult result = TestHelper.loginUser();
        String username = "";
        try {
            username = authDatabaseSQL.getUserByAuth(result.authToken());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(username);
    }

    @Test
    void testCheckAuthTokenReturnsAuthToken() {
        clearServiceSQL.clearApplication();
        TestHelper.registerUser();
        LoginResult result = TestHelper.loginUser();
        String token = authDatabaseSQL.checkAuthToken(result.authToken());
        assertEquals(result.authToken(), token);
    }

    @Test
    void testCheckAuthTokenThrowsException() {
        clearServiceSQL.clearApplication();
        TestHelper.registerUser();
        TestHelper.loginUser();
        assertThrows(RuntimeException.class, () -> authDatabaseSQL.checkAuthToken("1234"));
    }

    @Test
    void testDeleteAuthToken() {
        clearServiceSQL.clearApplication();
        TestHelper.registerUser();
        LoginResult result = TestHelper.loginUser();
        assertDoesNotThrow(() -> authDatabaseSQL.deleteAuth(result.authToken()));
    }

    @Test
    void testCreateUserDoesNotThrowException() {
        clearServiceSQL.clearApplication();
        UserData data = new UserData("username", "password", "email");
        assertDoesNotThrow(() -> userDatabaseSQL.createUser(data));
    }

    @Test
    void testCreateUserWithUsedUsernameThrowsException() {
        clearServiceSQL.clearApplication();
        UserData data = new UserData("username", "password", "email");
        TestHelper.registerUser();
        assertThrows(DataAccessException.class, () -> userDatabaseSQL.createUser(data));
    }

    @Test
    void testGetUserReturnsUser() {
        clearServiceSQL.clearApplication();
        TestHelper.registerUser();
        UserData data = null;
        try {
            data = userDatabaseSQL.getUser("username");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(data);
    }

    @Test
    void testGetUserDoesNotReturnUser() {
        clearServiceSQL.clearApplication();
        assertThrows(DataAccessException.class, () -> userDatabaseSQL.getUser("username"));
    }

    @Test
    void testHashUserPassword() {
        String password = "password";
        String hashed = userDatabaseSQL.hashUserPassword(password);
        assertNotEquals(password, hashed);
    }

    @Test
    void testVerifyPassword() {
        String password = "password";
        String hashed = userDatabaseSQL.hashUserPassword(password);
        boolean result = userDatabaseSQL.verifyPassword(password, hashed);
        assertTrue(result);
    }

    @Test
    void testCreateGameDoesNotThrowException() {
        clearServiceSQL.clearApplication();
        assertDoesNotThrow(() -> gameDatabaseSQL.createGame("name"));
    }

    @Test
    void testListGamesReturnsGames() {
        clearServiceSQL.clearApplication();
        gameDatabaseSQL.createGame("name");
        List<GameData> data = null;
        try {
            data = gameDatabaseSQL.listGames();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(data);
    }

    @Test
    void testJoinGameDoesNotThrowException() {
        clearServiceSQL.clearApplication();
        String gameID = gameDatabaseSQL.createGame("name");
        assertDoesNotThrow(() -> gameDatabaseSQL.joinGame("username", gameID, "WHITE"));
    }

    @Test
    void testJoinGameThrowsException() {
        clearServiceSQL.clearApplication();
        String gameID = gameDatabaseSQL.createGame("name");
        try {
            gameDatabaseSQL.joinGame("username", gameID, "WHITE");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertThrows(DataAccessException.class, () -> gameDatabaseSQL.joinGame("second", gameID, "WHITE"));
    }

    @Test
    void testJoinGameSecondPlayer() {
        clearServiceSQL.clearApplication();
        String gameID = gameDatabaseSQL.createGame("name");
        try {
            gameDatabaseSQL.joinGame("username", gameID, "WHITE");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertDoesNotThrow(() -> gameDatabaseSQL.joinGame("second", gameID, "BLACK"));
    }

    @Test
    void testClearDatabases() {
        clearServiceSQL.clearApplication();
        TestHelper.registerUser();
        LoginResult token = TestHelper.loginUser();
        clearServiceSQL.clearApplication();
        assertThrows(RuntimeException.class, () -> authDatabaseSQL.checkAuthToken(token.authToken()));
    }

    @Test
    void testClearAuthDoesNotThrowException() {
        clearServiceSQL.clearApplication();
        AuthData data = new AuthData(null, "username");
        assertDoesNotThrow(() -> authDatabaseSQL.clearAuth());
    }

    @Test
    void testClearUsersDoesNotThrowException() {
        clearServiceSQL.clearApplication();
        TestHelper.registerUser();
        assertDoesNotThrow(() -> userDatabaseSQL.clearUsers());
    }

    @Test
    void testClearGamesDoesNotThrowException() {
        clearServiceSQL.clearApplication();
        TestHelper.registerUser();
        LoginResult result = TestHelper.loginUser();
        TestHelper.createGame(result.authToken());
        assertDoesNotThrow(() -> gameDatabaseSQL.clearGames());
    }

}
