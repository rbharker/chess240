package dataaccess;

import kotlin.collections.MapAccessorsKt;
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

    // returns the username associated with an authToken
    @Override
    public String getUserByAuth(String authToken) throws DataAccessException {
        if (dataCollection.containsKey(authToken)){
            return dataCollection.get(authToken);
        }
        else {
            throw new DataAccessException("bad request");
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
        String username = dataCollection.get(authToken);
        if (username != null) {
            return authToken;
        }
        else {
            return null;
        }
    }

    public HashMap<String, String> getDataCollection() {
        return dataCollection;
    }
}
