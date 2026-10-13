package dataaccess;

import model.*;
import java.util.UUID;

public interface AuthDAO {

    public void clearAuth();

    static String generateToken() {
        return UUID.randomUUID().toString();
    }

    // returns the authToken it created
    public String createAuth();

    public AuthData getAuth(String username) throws DataAccessException;

    // returns null when successful
    public String deleteAuth(String authToken);

    public String checkAuthToken(String authToken);

}