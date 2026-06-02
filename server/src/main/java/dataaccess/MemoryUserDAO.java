package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements UserDAO {

    List<UserData> userList = new ArrayList<>();

    @Override
    public UserData getUser(String username) {
        for (UserData user : userList) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void addUser(UserData u) {
        userList.add(u);
    }

    @Override
    public void clearUsers() {
        userList = new ArrayList<>();
    }
}
