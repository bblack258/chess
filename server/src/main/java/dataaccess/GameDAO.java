package dataaccess;

import java.util.List;
import model.GameData;
import model.AuthData;
import chess.ChessGame.TeamColor;

public interface GameDAO {
    List<GameData> getGames(AuthData a);
    int addGame(String gameName);
    void updateGame(int gameID, TeamColor color, String username);
    void updateGame(int gameID, String chessGame);
    void clearGames();
}
