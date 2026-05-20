package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    List<GameData> gameList = new ArrayList<>();
    int gameID = 1;

    @Override
    public List<GameData> getGames(AuthData a) {
        List<GameData> myGames = new ArrayList<>();
        for (GameData game : gameList) {
            if (game.blackUsername().equals(a.username()) || game.whiteUsername().equals(a.username())) {
                myGames.add(game);
            }
        }
        return myGames;
    }

    @Override
    public GameData getGames(String gameName) {
        for (GameData games : gameList) {
            if (games.gameName().equals(gameName)) {
                return games;
            }
        }
        return null;
    }

    @Override
    public GameData getGames(int gameID) {
        for (GameData games : gameList) {
            if (games.gameID() == gameID) {
                return games;
            }
        }
        return null;
    }

    @Override
    public int addGame(String gameName) {
        gameList.add(new GameData(gameID++,null, null, gameName, new ChessGame()));
        return gameID;
    }

    @Override
    public void updateGame(int gameID, ChessGame.TeamColor color, String username) {
        for (GameData game : gameList) {
            if (gameID == game.gameID()) {
                if (color.equals(ChessGame.TeamColor.WHITE)) {
                    gameList.remove(game);
                    gameList.add(new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game()));
                } else {
                    gameList.remove(game);
                    gameList.add(new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game()));
                }
            }
        }
    }

    @Override
    public void clearGames() {
        gameList = new ArrayList<>();
    }
}
