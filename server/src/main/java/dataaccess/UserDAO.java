package dataaccess;

import model.*;

public interface UserDAO {

    public void clearUsers();

    public void createUser(UserData userData) throws DataAccessException;

    public UserData getUser(String username) throws DataAccessException;

}