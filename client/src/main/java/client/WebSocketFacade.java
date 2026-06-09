package client;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccesserrors.DataAccessException;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    private final Session session;

    public WebSocketFacade(String url, ServerMessageObserver observer) throws DataAccessException {
        try {

            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler((MessageHandler.Whole<String>) message -> {
                ServerMessage type = new Gson().fromJson(message, ServerMessage.class);
                switch (type.getServerMessageType()) {
                    case NOTIFICATION -> {
                        type = new Gson().fromJson(message, NotificationMessage.class);
                        observer.notify(type);
                    }
                    case ERROR -> {
                        type = new Gson().fromJson(message, ErrorMessage.class);
                        observer.notify(type);
                    }
                    case LOAD_GAME -> {
                        type = new Gson().fromJson(message, LoadGameMessage.class);
                        observer.notify(type);
                    }
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new DataAccessException("Error: Server error " + ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void enterGame(String authToken, int gameID) throws DataAccessException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException("Error: Server error -> " + ex.getMessage());
        }
    }

    public void move(String authToken, int gameID, ChessMove move) throws DataAccessException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException("Error: Server error -> " + ex.getMessage());
        }
    }

    public void leave(String authToken, int gameID) throws DataAccessException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException("Error: Server error -> " + ex.getMessage());
        }
    }

    public void resign(String authToken, int gameID) throws DataAccessException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new DataAccessException("Error: Server error -> " + ex.getMessage());
        }
    }
}
