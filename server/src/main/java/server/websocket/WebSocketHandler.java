package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccesserrors.DataAccessException;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final AuthDAO authMemory;
    private final GameDAO gameMemory;

    public WebSocketHandler(AuthDAO authMemory, GameDAO gameMemory) {
        this.authMemory = authMemory;
        this.gameMemory = gameMemory;
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) {
        try {
            UserGameCommand cmd = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (cmd.getCommandType()) {
                case CONNECT -> connect(cmd.getGameID(), ctx.session, cmd.getAuthToken());
                case MAKE_MOVE -> makeMove(cmd.getGameID(), ctx.session, cmd.getMove(),cmd.getAuthToken());
                case LEAVE -> leave(cmd.getGameID(), ctx.session, cmd.getAuthToken());
                case RESIGN -> resign(cmd.getGameID(), ctx.session, cmd.getAuthToken());
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(Integer gameID, Session session, String authToken) throws IOException {
        ServerMessage message;
        try {
            String user = getUsername(authToken);
            connections.add(gameID, session);
            String join;
            if (gameMemory.getGames(gameID) == null) {
                throw new DataAccessException("Error: Invalid game ID");
            } else if (Objects.equals(gameMemory.getGames(gameID).whiteUsername(), user)) {
                join = String.format("%s has joined the game as white player", user);
            } else if (Objects.equals(gameMemory.getGames(gameID).blackUsername(), user)) {
                join = String.format("%s has joined the game as black player", user);
            } else {
                join = String.format("%s is observing the game", user);
            }
            message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, join);
            connections.broadcast(gameID, session, message);
        } catch (DataAccessException ex) {
            message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
            connections.broadcast(session, message);
            return;
        }

        try {
            GameData game = gameMemory.getGames(gameID);
            message = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game().getBoard());
        } catch (DataAccessException ex) {
            message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
        }
        connections.broadcast(session, message);
    }

    private void makeMove(Integer gameID, Session session, ChessMove move, String authToken) throws IOException {
        ServerMessage gameMessage;
        ServerMessage notification;
        try {
            String user = getUsername(authToken);
            if (user == null) {
                throw new DataAccessException("Error: Unauthorized request");
            }
            GameData game = gameMemory.getGames(gameID);
            if (game == null) {
                throw new DataAccessException("Error: Invalid game ID");
            }
            checkObserver(user, game);
            checkTurn(user, game);
            checkPiece(user, game, move.getStartPosition());
            if (game.gameOver()) {
                throw new DataAccessException("Error: Game is over");
            }

            if (game.game().validMoves(move.getStartPosition()).contains(move)) {
                game.game().makeMove(move);
                gameMemory.updateGame(gameID, game.game());
                gameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game().getBoard());
                notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                        String.format("%s made move: " + move.readable(), user));

                ChessGame.TeamColor turn = game.game().getTeamTurn();
                NotificationMessage check = checkConditions(gameID, game.game(), turn);

                connections.broadcast(gameID, null, gameMessage);
                connections.broadcast(gameID, session, notification);
                if (check != null) {
                    connections.broadcast(gameID, null, check);
                }
            } else {
                throw new DataAccessException("Error: Invalid move");
            }
        } catch (InvalidMoveException | DataAccessException ex) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
            connections.broadcast(session, message);
        }
    }

    private void leave(Integer gameID, Session session, String authToken) throws IOException {
        ServerMessage message;
        try {
            String user = getUsername(authToken);
            GameData game = gameMemory.getGames(gameID);
            if (Objects.equals(game.whiteUsername(), user)) {
                gameMemory.updateGame(gameID, ChessGame.TeamColor.WHITE, null);
            } else if (Objects.equals(game.blackUsername(), user)) {
                gameMemory.updateGame(gameID, ChessGame.TeamColor.BLACK, null);
            }
            connections.remove(gameID, session);
            message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    String.format("%s has left the game", user));
            connections.broadcast(gameID, session, message);
        } catch (DataAccessException ex) {
            message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
            connections.broadcast(session, message);
        }
    }

    private void resign(Integer gameID, Session session, String authToken) throws IOException {
        ServerMessage message;
        try {
            String user = getUsername(authToken);
            GameData game = gameMemory.getGames(gameID);
            if (game == null) {
                throw new DataAccessException("Error: Invalid game");
            }
            checkObserver(user, game);
            if (game.gameOver()) {
                throw new DataAccessException("Error: Game is over");
            }

            gameMemory.finishGame(gameID);
            message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    String.format("%s has resigned the game", user));
            connections.broadcast(gameID, null, message);
        } catch (DataAccessException ex) {
            message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
            connections.broadcast(session, message);
        }
    }

    private String getUsername(String authToken) throws DataAccessException {
        AuthData authData = authMemory.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Error: Unauthorized request");
        }
        return authData.username();
    }

    private void checkObserver(String user, GameData game) throws DataAccessException {
        if (!(Objects.equals(user, game.whiteUsername()) || Objects.equals(user, game.blackUsername()))) {
            throw new DataAccessException("Error: observer");
        }
    }

    private void checkTurn(String username, GameData game) throws DataAccessException {
        ChessGame.TeamColor turn = game.game().getTeamTurn();
        if ((Objects.equals(game.whiteUsername(), username) && turn == ChessGame.TeamColor.BLACK) ||
                (Objects.equals(game.blackUsername(), username) && turn == ChessGame.TeamColor.WHITE)) {
            throw new DataAccessException("Error: not your turn");
        }
    }

    private void checkPiece(String username, GameData game, ChessPosition start) throws DataAccessException {
        ChessPiece piece = game.game().getBoard().getPiece(start);
        if (piece == null) {
            throw new DataAccessException("Error: not a valid piece");
        }
        if ((Objects.equals(game.whiteUsername(), username) && piece.getTeamColor() == ChessGame.TeamColor.BLACK) ||
                (Objects.equals(game.blackUsername(), username) && piece.getTeamColor() == ChessGame.TeamColor.WHITE)) {
            throw new DataAccessException("Error: not your piece");
        }
    }

    private NotificationMessage checkConditions(int gameID, ChessGame game, ChessGame.TeamColor otherTeam) throws DataAccessException {
        NotificationMessage message = null;
        ChessGame.TeamColor team = ChessGame.TeamColor.WHITE;
        if (otherTeam == ChessGame.TeamColor.WHITE) {
            team = ChessGame.TeamColor.BLACK;
        }
        if (game.isInCheckmate(otherTeam)) {
            message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    String.format("%s is in checkmate", otherTeam));
            gameMemory.finishGame(gameID);
        } else if (game.isInCheck(otherTeam)) {
            message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    String.format("%s is in check", otherTeam));
        } else if (game.isInStalemate(otherTeam) || game.isInStalemate(team)) {
            message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "Stalemate!");
        }
        return message;
    }
}
