package service;

import dataaccess.*;
import result.ClearApplicationResult;

public class ClearService {
    private MySQLAuthDAO authDAO;
    private MySQLGameDAO gameDAO;
    private MySQLUserDAO userDAO;

    public ClearService(MySQLAuthDAO authDAO, MySQLGameDAO gameDAO, MySQLUserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    public ClearApplicationResult clearApplication() {
        userDAO.clearUsers();
        gameDAO.clearGames();
        authDAO.clearAuth();

        return new ClearApplicationResult(null);
    }
}
