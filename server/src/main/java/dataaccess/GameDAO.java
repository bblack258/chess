package dataaccess;

import java.util.List;
import model.GameData;
import chess.ChessGame.TeamColor;

public interface GameDAO {
    List<GameData> getGames();
    GameData getGames(String gameName);
    GameData getGames(int gameID);
    int addGame(String gameName);
    void updateGame(int gameID, TeamColor color, String username);
    void clearGames();
}
