package service;

import chess.ChessGame;
import dataaccess.*;
import dataaccesserrors.*;
import model.*;

import java.util.List;

public class GameService {

    private final AuthDAO authMemory;
    private final GameDAO gameMemory;

    public GameService(AuthDAO authMemory, GameDAO gameMemory) {
        this.authMemory = authMemory;
        this.gameMemory = gameMemory;
    }

    public int addGame(String authToken, String gameName) throws DataAccessException {
        authorize(authToken);
        if (gameName == null || gameName.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        if (gameMemory.getGames(gameName) != null) {
            throw new AlreadyExistsException("Error: game already exists");
        }
        return gameMemory.addGame(gameName);
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws DataAccessException{
        authorize(authToken);
        GameData game = gameMemory.getGames(gameID);
        if (game == null) {
            throw new BadRequestException("Error: game does not exist");
        }
        if (playerColor == null || playerColor.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        AuthData auth = authMemory.getAuth(authToken);
        if (playerColor.equalsIgnoreCase("WHITE")) {
            if (colorNotTaken(ChessGame.TeamColor.WHITE, game)) {
                gameMemory.updateGame(game.gameID(), ChessGame.TeamColor.WHITE, auth.username());
            } else {
                throw new AlreadyTakenException("Error: color already taken");
            }
        } else if (playerColor.equalsIgnoreCase("BLACK")) {
            if (colorNotTaken(ChessGame.TeamColor.BLACK, game)) {
                gameMemory.updateGame(game.gameID(), ChessGame.TeamColor.BLACK, auth.username());
            } else {
                throw new AlreadyTakenException("Error: color already taken");
            }
        } else {
            throw new BadRequestException("Error: not a valid color");
        }

    }

    public List<GameData> listGames(String authToken) throws DataAccessException {
        authorize(authToken);
        return gameMemory.getGames();
    }

    private void authorize(String authToken) throws DataAccessException {
        if (authToken == null || authToken.isEmpty() || authMemory.getAuth(authToken) == null) {
            throw new UnauthorizedRequestException("Error: unauthorized");
        }
    }

    private boolean colorNotTaken(ChessGame.TeamColor color, GameData game) {
        return (color == ChessGame.TeamColor.WHITE && game.whiteUsername() == null) ||
                (color == ChessGame.TeamColor.BLACK && game.blackUsername() == null);
    }
}
