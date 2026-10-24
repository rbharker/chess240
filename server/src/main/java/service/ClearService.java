package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class ClearService {
    private MemoryAuthDAO authDAO;
    private MemoryGameDAO gameDAO;
    private MemoryUserDAO userDAO;

    public ClearService(MemoryAuthDAO authDAO, MemoryGameDAO gameDAO, MemoryUserDAO userDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }

    void clearApplication() {
        userDAO.clearUsers();
        gameDAO.clearGames();
        authDAO.clearAuth();
    }
}
