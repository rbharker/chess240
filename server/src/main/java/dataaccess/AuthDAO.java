package dataaccess;

import model.*;
import java.util.UUID;

public interface AuthDAO {

    // clears the database of authTokens
    public void clearAuth();

    // generates a new, random authToken
    static String generateToken() {
        return UUID.randomUUID().toString();
    }

    // creates an authToken and adds it
    // to the database with the username
    // returns the authToken it created
    public String createAuth(AuthData data);

    // returns the authData associated with a username
    public String getAuth(String username) throws DataAccessException;

    // deletes the specified authToken
    // returns null when successful
    public String deleteAuth(String authToken);

    // checks if an authToken exists
    // returns the authToken if it does
    // otherwise returns null
    public String checkAuthToken(String authToken);

}