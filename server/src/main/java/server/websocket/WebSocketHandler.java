package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccesserrors.DataAccessException;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final UserDAO userMemory;
    private final AuthDAO authMemory;
    private final GameDAO gameMemory;

    public WebSocketHandler(UserDAO userMemory, AuthDAO authMemory, GameDAO gameMemory) {
        this.userMemory = userMemory;
        this.authMemory = authMemory;
        this.gameMemory = gameMemory;
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) throws Exception {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        try {
            UserGameCommand cmd = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (cmd.getCommandType()) {
                case CONNECT -> connect(cmd.getGameID(), ctx.session, cmd.getAuthToken());
                case MAKE_MOVE -> makeMove(cmd.getGameID(), ctx.session, cmd.getMove());
                case LEAVE -> leave(cmd.getGameID(), ctx.session, cmd.getAuthToken());
                case RESIGN -> resign(cmd.getGameID(), ctx.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace(); // Handle this better
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
        System.out.println("Websocket closed");
    }

    private void connect(Integer gameID, Session session, String authToken) throws IOException, DataAccessException {
        String user = getUsername(authToken);
        connections.add(gameID, session);
        String join;
        if (gameMemory.getGames(gameID).whiteUsername().equals(user)) {
            join = String.format("%s has joined the game as white player", user);
        } else if (gameMemory.getGames(gameID).blackUsername().equals(user)) {
            join = String.format("%s has joined the game as black player", user);
        } else {
            join = String.format("%s is observing the game", user);
        }
        NotificationMessage message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, join); // Include player color
        connections.broadcast(gameID, session, message);
    }

    private void makeMove(Integer gameID, Session session, ChessMove move) {

    }

    private void leave(Integer gameID, Session session, String authToken) throws IOException, DataAccessException {
        String user = getUsername(authToken);
        GameData game = gameMemory.getGames(gameID);
        if (Objects.equals(game.whiteUsername(), user)) {
            gameMemory.updateGame(gameID, ChessGame.TeamColor.WHITE, null);
        } else if (Objects.equals(game.blackUsername(), user)) {
            gameMemory.updateGame(gameID, ChessGame.TeamColor.BLACK, null);
        } else {
            throw new DataAccessException("Error: Not a valid leave request");
        }
        NotificationMessage message = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                String.format("%s has left the game", user));
        connections.broadcast(gameID, session, message);
        connections.remove(gameID, session);
    }

    private void resign(Integer gameID, Session session) {

    }

    private String getUsername(String authToken) throws DataAccessException {
        return authMemory.getAuth(authToken).username();
    }
}
