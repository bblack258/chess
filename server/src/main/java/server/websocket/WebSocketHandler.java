package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccesserrors.DataAccessException;
import io.javalin.websocket.*;
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
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        try {
            UserGameCommand cmd = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (cmd.getCommandType()) {
                case CONNECT -> connect(cmd.getGameID(), ctx.session, cmd.getAuthToken());
                case MAKE_MOVE -> makeMove(cmd.getGameID(), ctx.session, cmd.getMove(),cmd.getAuthToken());
                case LEAVE -> leave(cmd.getGameID(), ctx.session, cmd.getAuthToken());
                case RESIGN -> resign(cmd.getGameID(), ctx.session, cmd.getAuthToken());
            }
        } catch (IOException ex) {
            ex.printStackTrace(); // Handle this better
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
            } else if (gameMemory.getGames(gameID).whiteUsername().equals(user)) {
                join = String.format("%s has joined the game as white player", user);
            } else if (gameMemory.getGames(gameID).blackUsername().equals(user)) {
                join = String.format("%s has joined the game as black player", user);
            } else {
                join = String.format("%s is observing the game", user);
            }
            message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, join);
            connections.broadcast(gameID, session, message);
        } catch (DataAccessException ex) {
            message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
            connections.broadcast(session, message);
        }

        try {
            GameData game = gameMemory.getGames(gameID);
            message = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game().getBoard());
        } catch (DataAccessException ex) {
            message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
        }
        connections.broadcast(session, message);
    }

    private void makeMove(Integer gameID, Session session, ChessMove move, String authToken) throws IOException, DataAccessException {
        ServerMessage message;
        try {
            String user = getUsername(authToken);
            GameData game = gameMemory.getGames(gameID);
            if (game == null) {
                throw new DataAccessException("Error: Not a valid game");
            }
            if (game.game().validMoves(move.getStartPosition()).contains(move)) {
                game.game().makeMove(move);
                gameMemory.updateGame(gameID, game.game());
                message = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game().getBoard());
                connections.broadcast(gameID, null, message);
                // Finish this out - notify users that a move was made, check for check, etc.
            } else {
                throw new DataAccessException("Error: Invalid move");
            }
        } catch (DataAccessException ex) {
            message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
        } catch (InvalidMoveException e) {
            throw new DataAccessException("Error: Invalid move");
        }
        connections.broadcast(gameID, session, message);
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
            } else {
                throw new DataAccessException("Error: Not a valid leave request");
            }
            connections.remove(gameID, session);
            message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    String.format("%s has left the game", user));
        } catch (DataAccessException ex) {
            message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
        }
        connections.broadcast(gameID, session, message);
    }

    private void resign(Integer gameID, Session session, String authToken) throws IOException {
        ServerMessage message;
        try {
            String user = getUsername(authToken);
            gameMemory.finishGame(gameID);
            message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    String.format("%s has resigned the game", user));
        } catch (DataAccessException ex) {
            message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage());
        }
        connections.broadcast(gameID, null, message);
    }

    private String getUsername(String authToken) throws DataAccessException {
        return authMemory.getAuth(authToken).username();
    }
}
