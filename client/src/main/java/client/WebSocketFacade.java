package client;

import com.google.gson.Gson;
import dataaccesserrors.DataAccessException;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    ServerMessageObserver observer;

    public WebSocketFacade(String url, ServerMessageObserver observer) throws DataAccessException {
        try {
            this.observer = observer;

            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage type = new Gson().fromJson(message, ServerMessage.class);
                    switch (type.getServerMessageType()) {
                        case NOTIFICATION -> type = new Gson().fromJson(message, NotificationMessage.class);
                        case ERROR -> type = new Gson().fromJson(message, ErrorMessage.class);
                        case LOAD_GAME -> type = new Gson().fromJson(message, LoadGameMessage.class);
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

}
