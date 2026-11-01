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
        LoginResult result = TestHelper.loginUser();
        String token = authDatabaseSQL.checkAuthToken(result.authToken());
        assertEquals(result.authToken(), token);
    }

    @Test
    void testCreateUserDoesNotThrowException() {
        clearServiceSQL.clearApplication();
        UserData data = new UserData("username", "password", "email");
        assertDoesNotThrow(() -> userDatabaseSQL.createUser(data));
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
    void testClearDatabases() {
        TestHelper.registerUser();
        LoginResult token = TestHelper.loginUser();
        clearServiceSQL.clearApplication();
        assertThrows(RuntimeException.class, () -> authDatabaseSQL.checkAuthToken(token.authToken()));
    }

}
