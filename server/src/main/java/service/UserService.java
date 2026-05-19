package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class UserService {

    private final MemoryUserDAO userMemory;
    private final MemoryAuthDAO authMemory;

    public UserService(MemoryUserDAO userMemory, MemoryAuthDAO authMemory) {
        this.userMemory = userMemory;
        this.authMemory = authMemory;
    }

    AuthData register(UserData r) throws DataAccessException {
        if (r.username() == null || r.username().isEmpty()) {
            throw new BadRequestException("Invalid Username");
        }
        if (r.password() == null || r.password().isEmpty()) {
            throw new BadRequestException("Invalid Password");
        }
        if (r.email() == null || r.email().isEmpty()) {
            throw new BadRequestException("Invalid Email");
        }
        if (userMemory.getUser(r.username()) != null) {
            throw new AlreadyTakenException("Username already taken");
        }

        UserData newUser = new UserData(r.username(), r.password(), r.email());
        userMemory.addUser(newUser);

        return authMemory.addAuth(r.username());
    }

    AuthData login(UserData r) throws DataAccessException {

        UserData user = null;

        if (r.username() != null && userMemory.getUser(r.username()) != null) {
            user = userMemory.getUser(r.username());
        } else {
            throw new BadRequestException("Invalid Username");
        }
        if (Objects.equals(user.password(), r.password())) {
            throw new BadRequestException("Invalid Password");
        }

        return authMemory.addAuth(r.username());
    }

    void logout(String authToken) throws DataAccessException {
        if (authToken == null || authToken.isEmpty() || authMemory.getAuth(authToken) == null) {
            throw new UnauthorizedRequestException("Unauthorized");
        }
        authMemory.deleteAuth(authToken);
    }
}
