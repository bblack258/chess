package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import dataaccesserrors.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySQLGameDAO implements GameDAO {

    public MySQLGameDAO() {
        try {
            DatabaseManager.configureDatabase();
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error: Unable to configure database");
        }
    }

    @Override
    public List<GameData> getGames() throws DataAccessException {
        List<GameData> gameList = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game, gameOver FROM game";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        gameList.add(readGame(rs));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
        return gameList;
    }

    @Override
    public GameData getGames(String gameName) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game, gameOver FROM game WHERE gameName=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, gameName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public GameData getGames(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game, gameOver FROM game WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
        return null;
    }

    @Override
    public int addGame(String gameName) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, game, gameOver) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                ps.setString(1, null);
                ps.setString(2, null);
                ps.setString(3, gameName);
                ps.setString(4, new Gson().toJson(new ChessGame()));
                ps.setBoolean(5, false);
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
        return 0;
    }

    @Override
    public void updateGame(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement;
            if (color == ChessGame.TeamColor.WHITE) {
                statement = "UPDATE game SET whiteUsername = ? WHERE gameID = ?";
            } else if (color == ChessGame.TeamColor.BLACK) {
                statement = "UPDATE game SET blackUsername = ? WHERE gameID = ?";
            } else {
                throw new DataAccessException("Error: invalid team color");
            }
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                ps.setInt(2, gameID);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
    }

    @Override
    public void clearGames() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE game";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException("Error: " + ex.getMessage());
        }
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        int gameID = rs.getInt("gameID");
        String whiteUsername = rs.getString("whiteUsername");
        String blackUsername = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        var json = rs.getString("game");
        boolean gameOver = rs.getBoolean("gameOver");
        ChessGame game = new Gson().fromJson(json, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game, gameOver);
    }
}
