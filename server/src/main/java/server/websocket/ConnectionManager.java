package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<Session, Session>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session) {
        connections.putIfAbsent(gameID, new ConcurrentHashMap<>());
        connections.get(gameID).put(session, session);
    }

    public void remove(Integer gameID, Session session) {
        connections.get(gameID).remove(session, session);
    }
    
    public void broadcast(Session session, ServerMessage message) throws IOException {
        String msg = new Gson().toJson(message);
        if (session.isOpen()) {
            session.getRemote().sendString(msg);
        }
    }

    public <T extends ServerMessage> void broadcast(Integer gameID, Session exclude, T message) throws IOException {
        String msg = new Gson().toJson(message);
        for (Session c : connections.get(gameID).values()) {
            if (c.isOpen()) {
                if (!c.equals(exclude)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}
