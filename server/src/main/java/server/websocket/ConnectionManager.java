package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<Session, Session>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session) {
        connections.get(gameID).put(session, session);
    }

    public void remove(Integer gameID, Session session) {
        connections.get(gameID).remove(session, session);
    }

    public void broadcast(Integer gameID, Session exclude, String message) throws IOException {
        for (Session c : connections.get(gameID).values()) {
            if (c.isOpen()) {
                if (!c.equals(exclude)) {
                    c.getRemote().sendString(message);
                }
            }
        }
    }
}
