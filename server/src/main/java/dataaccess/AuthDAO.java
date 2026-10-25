package dataaccess;

import model.*;
import java.util.UUID;

public interface AuthDAO {

    // clears the database of authTokens
    void clearAuth();

    // generates a new, random authToken
    static String generateToken() {
        return UUID.randomUUID().toString();
    }

    // creates an authToken and adds it
    // to the database with the username
    // returns the authToken it created
    String createAuth(AuthData data);

    // returns the authData associated with a username
    String getUserByAuth(String authToken) throws DataAccessException;

    // deletes the specified authToken
    // returns null when successful
    String deleteAuth(String authToken);

    // checks if an authToken exists
    // returns the authToken if it does
    // otherwise returns null
    String checkAuthToken(String authToken);

}