package service;

import dataaccess.*;
import model.*;

import java.util.List;

public class GameService {

    private final UserDAO userMemory;
    private final AuthDAO authMemory;
    private final GameDAO gameMemory;

    public GameService(UserDAO userMemory, AuthDAO authMemory, GameDAO gameMemory) {
        this.userMemory = userMemory;
        this.authMemory = authMemory;
        this.gameMemory = gameMemory;
    }

    public int addGame(String authToken, String gameName) throws DataAccessException {
        authorize(authToken);
        if (gameMemory.getGames(gameName) != null) {
            throw new AlreadyExistsException("Error: game already exists");
        }
        return gameMemory.addGame(gameName);
    }

    public void joinGame(String authToken, String playerColor, String gameID) throws DataAccessException{
        authorize(authToken);
        if (gameMemory.getGames(gameID) == null) {
            throw new DoesNotExistException("Error: game does not exist");
        }
        GameData game = gameMemory.getGames(gameID);

    }

    public List<GameData> listGames(String authToken) throws DataAccessException {
        authorize(authToken);
        return gameMemory.getGames(authMemory.getAuth(authToken));
    }

    private void authorize(String authToken) throws DataAccessException {
        if (authToken == null || authToken.isEmpty() || authMemory.getAuth(authToken) == null) {
            throw new UnauthorizedRequestException("Error: unauthorized");
        }
    }
}
