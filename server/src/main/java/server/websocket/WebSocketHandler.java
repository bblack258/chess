package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

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
                case CONNECT -> connect(cmd.getGameID(), ctx.session);
                case MAKE_MOVE -> makeMove(cmd.getGameID(), ctx.session, cmd.getMove());
                case LEAVE -> leave(cmd.getGameID(), ctx.session);
                case RESIGN -> resign(cmd.getGameID(), ctx.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
        System.out.println("Websocket closed");
    }

    private void connect(Integer gameID, Session session) throws IOException {
        String name = "joey"; // FIGURE OUT HOW TO FIX THIS
        connections.add(gameID, session);
        String message = String.format("%s has joined the game", name);
        connections.broadcast(gameID, session, message); // Maybe come back and do this with the websocket messages classes?
    }

    private void makeMove(Integer gameID, Session session, ChessMove move) {

    }

    private void leave(Integer gameID, Session session) {

    }

    private void resign(Integer gameID, Session session) {

    }
}
