package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.List;

public class MySQLGameDAO implements GameDAO {

    public MySQLGameDAO() {
        try {
            DatabaseManager.configureDatabase();
        } catch (DataAccessException ex) {
            throw new RuntimeException("Unable to configure database");
        }
    }

    @Override
    public List<GameData> getGames() {
        return List.of();
    }

    @Override
    public GameData getGames(String gameName) {
        return null;
    }

    @Override
    public GameData getGames(int gameID) {
        return null;
    }

    @Override
    public int addGame(String gameName) {
        return 0;
    }

    @Override
    public void updateGame(int gameID, ChessGame.TeamColor color, String username) {

    }

    @Override
    public void clearGames() {

    }
}
