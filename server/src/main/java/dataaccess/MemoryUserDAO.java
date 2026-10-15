package dataaccess;

import model.UserData;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryUserDAO implements UserDAO {
    HashMap<String, List<String >> dataCollection = new HashMap<>();

    @Override
    public void clearUsers() {
        dataCollection.clear();
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if (dataCollection.containsKey(userData.username())) {
            throw new DataAccessException("Username taken.");
        }
        else {
            List<String> data = Arrays.asList(userData.password(), userData.email());
            dataCollection.put(userData.username(), data);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (!dataCollection.containsKey(username)) {
            throw new DataAccessException("User not found.");
        }
        else {
            UserData data = new UserData(username,
                                        dataCollection.get(username).get(0),
                                        dataCollection.get(username).get(1));
            return data;
        }
    }
}
