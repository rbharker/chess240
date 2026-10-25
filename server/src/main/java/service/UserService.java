package service;

import dataaccess.*;
import model.*;

public class UserService {

    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;

    public UserService(MemoryUserDAO userDAO, MemoryAuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    // register a new user if the username isn't already taken
    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        try {
            userDAO.getUser(registerRequest.username());
            throw new DataAccessException("User already exists!");
        }
        catch (DataAccessException e) {
            UserData userData = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            AuthData authData = new AuthData(null, registerRequest.username());
            userDAO.createUser(userData);
            String authToken = authDAO.createAuth(authData);
            RegisterResult result = new RegisterResult(userData.username(), authToken);
            return result;
        }
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        try {
            var userData = userDAO.getUser(loginRequest.username());
            if (!userData.password().equals(loginRequest.password())) {
                throw new DataAccessException("Incorrect password!");
            }
            AuthData authData = new AuthData(null, userData.username());
            String authToken = authDAO.createAuth(authData);
            LoginResult result = new LoginResult(userData.username(), authToken);
            return result;
        }
        catch (DataAccessException e) {
            throw e;
        }
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException {
        String authToken = authDAO.checkAuthToken(logoutRequest.authData());
        if (authToken != null) {
            authDAO.deleteAuth(authToken);
        }
        else {
            throw new DataAccessException("Invalid request");
        }
    }
}
