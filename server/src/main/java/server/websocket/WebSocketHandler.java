package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.MySQLAuthDAO;
import dataaccesserrors.DataAccessException;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final AuthDAO authMemory = new MySQLAuthDAO();

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
                case LEAVE -> leave(cmd.getGameID(), ctx.session);
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
        String message = String.format("%s has joined the game", user);
        connections.broadcast(gameID, session, message); // Maybe come back and do this with the websocket messages classes?
    }

    private void makeMove(Integer gameID, Session session, ChessMove move) {

    }

    private void leave(Integer gameID, Session session) {

    }

    private void resign(Integer gameID, Session session) {

    }

    private String getUsername(String authToken) throws DataAccessException {
        return authMemory.getAuth(authToken).username();
    }
}
