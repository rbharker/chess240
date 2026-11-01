package service;

import dataaccess.*;
import model.*;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;

public class UserService {

    private MySQLUserDAO userDAO;
    private MySQLAuthDAO authDAO;

    public UserService(MySQLUserDAO userDAO, MySQLAuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    // register a new user if the username isn't already taken
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new DataAccessException("bad request");
        }
        try {
            UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            AuthData authData = new AuthData(null, registerRequest.username());
            userDAO.createUser(userData);
            String authToken = authDAO.createAuth(authData);
            RegisterResult result = new RegisterResult(userData.username(), authToken);
            return result;
        } catch (DataAccessException e) {
            if (e.getMessage().contains("failed to get connection")) {
                throw new RuntimeException(e.getMessage());
            }
            throw e;
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        if (loginRequest.username() == null || loginRequest.password() == null) {
            throw new DataAccessException("bad request");
        }
        try {
            var userData = userDAO.getUser(loginRequest.username());
            if (!(userDAO.verifyPassword(loginRequest.password(), userData.password()))) {
                throw new DataAccessException("unauthorized");
            }
            AuthData authData = new AuthData(null, userData.username());
            String authToken = authDAO.createAuth(authData);
            LoginResult result = new LoginResult(userData.username(), authToken);
            return result;
        }
        catch (DataAccessException e) {
            if (e.getMessage().contains("failed to get connection")) {
                throw new RuntimeException(e.getMessage());
            }
            throw e;
        }
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException {
        String authToken = "";
        try {
            authToken = authDAO.checkAuthToken(logoutRequest.authData());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("failed to get connection")) {
                throw new RuntimeException(e.getMessage());
            }
            throw e;
        }
        if (authToken != null) {
            authDAO.deleteAuth(authToken);
            return new LogoutResult(null);
        }
        else {
            throw new DataAccessException("unauthorized");
        }
    }
}
