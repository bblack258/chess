package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {

    private final UserDAO userMemory;
    private final AuthDAO authMemory;

    public UserService(UserDAO userMemory, AuthDAO authMemory) {
        this.userMemory = userMemory;
        this.authMemory = authMemory;
    }

    public AuthData register(UserData r) throws DataAccessException {
        if (r.username() == null || r.username().isEmpty()) {
            throw new BadRequestException("Error: Invalid Username");
        }
        if (r.password() == null || r.password().isEmpty()) {
            throw new BadRequestException("Error: Invalid Password");
        }
        if (r.email() == null || r.email().isEmpty()) {
            throw new BadRequestException("Error: Invalid Email");
        }
        if (userMemory.getUser(r.username()) != null) {
            throw new AlreadyTakenException("Error: Username already taken");
        }

        UserData newUser = new UserData(r.username(), r.password(), r.email());
        userMemory.addUser(newUser);

        return authMemory.addAuth(r.username());
    }

    public AuthData login(UserData r) throws DataAccessException {
        UserData user;
        if (r.username() == null || r.password() == null) {
            throw new BadRequestException("Error: missing username");
        }
        if (userMemory.getUser(r.username()) != null) {
            user = userMemory.getUser(r.username());
        } else {
            throw new UnauthorizedRequestException("Error: Invalid Username");
        }
        if (!Objects.equals(user.password(), r.password())) {
            throw new UnauthorizedRequestException("Error: Invalid Password");
        }
        return authMemory.addAuth(r.username());
    }

    public void logout(String authToken) throws DataAccessException {
        if (authToken == null || authToken.isEmpty() || authMemory.getAuth(authToken) == null) {
            throw new UnauthorizedRequestException("Error: Unauthorized");
        }
        authMemory.deleteAuth(authToken);
    }
}
