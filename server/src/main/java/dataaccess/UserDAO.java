package dataaccess;

import model.*;

public interface UserDAO {

    // clears the user database
    public void clearUsers();

    // creates a new user and adds their data to the database
    public void createUser(UserData userData) throws DataAccessException;

    // returns the data associated with a specific username
    public UserData getUser(String username) throws DataAccessException;

}