package dataaccess;

import model.AuthData;
import model.UserData;

import java.security.KeyException;
import java.util.HashMap;
import java.util.List;

public class MemoryAuthDAO implements  AuthDAO {
    HashMap<String, String> dataCollection = new HashMap<>();

    // clears the database of authTokens
    @Override
    public void clearAuth() {
        dataCollection.clear();
    }

    // creates an authToken and adds it
    // to the database with the username
    // returns the authToken it created
    @Override
    public String createAuth(AuthData data) {
        String authToken = AuthDAO.generateToken();
        dataCollection.put(authToken, data.username());
        return authToken;
    }

    // returns the authData associated with a username
    @Override
    public String getAuth(String username) throws DataAccessException {
        if (dataCollection.containsKey(username)){
            return dataCollection.get(username);
        }
        else {
            throw new DataAccessException("Auth token not found.");
        }
    }

    // deletes the specified authToken
    // returns null when successful
    @Override
    public String deleteAuth(String authToken) {
        dataCollection.remove(authToken);
        return null;
    }

    // checks if an authToken exists
    // returns the authToken if it does
    // otherwise returns null
    @Override
    public String checkAuthToken(String authToken) {
        String token = dataCollection.get(authToken);
        if (token != null) {
            return token;
        }
        else {
            return null;
        }
    }
}
