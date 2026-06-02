package dataaccess;

import java.util.List;
import model.GameData;
import dataaccesserrors.*;
import chess.ChessGame.TeamColor;

public interface GameDAO {
    List<GameData> getGames() throws DataAccessException;
    GameData getGames(String gameName) throws DataAccessException;
    GameData getGames(int gameID) throws DataAccessException;
    int addGame(String gameName) throws DataAccessException;
    void updateGame(int gameID, TeamColor color, String username) throws DataAccessException;
    void clearGames() throws DataAccessException;
}
