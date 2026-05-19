package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    List<GameData> gameList = new ArrayList<>();

    @Override
    public List<GameData> getGames(AuthData a) {
        return List.of();
    }

    @Override
    public int addGame(String gameName) {
        return 0;
    }

    @Override
    public void updateGame(int gameID, ChessGame.TeamColor color, String username) {

    }

    @Override
    public void updateGame(int gameID, String chessGame) {

    }

    @Override
    public void clearGames() {
        gameList = new ArrayList<>();
    }
}
