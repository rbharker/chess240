package dataaccess;

import model.*;

public interface UserDAO {

    // clears the user database
    void clearUsers();

    // creates a new user and adds their data to the database
    void createUser(UserData userData) throws DataAccessException;

    // returns the data associated with a specific username
    UserData getUser(String username) throws DataAccessException;

}