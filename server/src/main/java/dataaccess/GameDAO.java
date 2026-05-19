package dataaccess;

import java.util.List;
import model.GameData;
import model.AuthData;
import chess.ChessGame.TeamColor;

public interface GameDAO {
    List<GameData> getGames(AuthData a);
    int addGame(String gameName);
    void addPlayer(int gameID, TeamColor color, String username);
    void clearGames();
}
