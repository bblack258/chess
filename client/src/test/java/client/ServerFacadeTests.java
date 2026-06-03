package client;

import dataaccesserrors.DataAccessException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static int port;
    private static ServerFacade facade;
    private static HttpClient client;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
        client = HttpClient.newHttpClient();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @AfterEach
    void cleanUp() throws DataAccessException {
        clearRequest();
    }


    @Test
    public void registerCorrect() throws DataAccessException {
        AuthData auth = facade.register(new UserData("username", "password", "email"));
        assertNotNull(auth.authToken());
        assertEquals("username", auth.username());
    }

    @Test
    public void registerIncorrect() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        facade.register(user);
        assertThrows(DataAccessException.class, () -> facade.register(user));
    }

    @Test
    public void loginCorrect() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        facade.register(user);
        AuthData auth = facade.login(user);
        assertNotNull(auth.authToken());
        assertEquals("username", auth.username());
    }

    @Test
    public void loginIncorrect() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        UserData badUser1 = new UserData("username", "none", null);
        UserData badUser2 = new UserData("usrname", "password", null);
        facade.register(user);
        assertThrows(DataAccessException.class, () -> facade.login(badUser1));
        assertThrows(DataAccessException.class, () -> facade.login(badUser2));
    }

    @Test
    public void logoutCorrect() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        AuthData auth1 = facade.register(user);
        facade.logout();
        assertThrows(DataAccessException.class, () -> facade.register(user));
        AuthData auth2 = facade.login(user);
        assertNotEquals(auth1.authToken(), auth2.authToken());
    }

    @Test
    public void logoutIncorrect() {
        assertThrows(DataAccessException.class, () -> facade.logout());
    }

    @Test
    public void listCorrect() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        facade.register(user);
        assertEquals(List.of(), facade.listGames());
        facade.create("game1");
        facade.create("game2");
        facade.create("game3");
        facade.create("game4");
        assertEquals(4, facade.listGames().size());
    }

    @Test
    public void listIncorrect() {
        assertThrows(DataAccessException.class, () -> facade.listGames());
    }

    @Test
    public void createCorrect() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        facade.register(user);
        String gameName = facade.create("game");
        assertEquals("game", gameName);
        gameName = facade.create("otherGame");
        assertEquals("otherGame", gameName);
        assertEquals(2, facade.listGames().size());
    }

    @Test
    public void createIncorrect() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> facade.create("game"));
        UserData user = new UserData("username", "password", "email");
        facade.register(user);
        facade.create("game");
        assertThrows(DataAccessException.class, () -> facade.create(null));
        assertThrows(DataAccessException.class, () -> facade.create("game"));
    }

    @Test
    public void joinCorrect() throws DataAccessException {
        UserData user = new UserData("username", "password", "email");
        facade.register(user);
        facade.create("game");
        assertDoesNotThrow(() -> facade.join(new JoinRequest("white", 1)));
    }

    @Test
    public void joinIncorrect() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> facade.join(new JoinRequest("white", 1)));
        UserData user = new UserData("username", "password", "email");
        facade.register(user);
        assertThrows(DataAccessException.class, () -> facade.join(new JoinRequest("white", 1)));
        facade.create("game");
        assertThrows(DataAccessException.class, () -> facade.join(new JoinRequest("white", 0)));
        assertThrows(DataAccessException.class, () -> facade.join(new JoinRequest("teal", 1)));
    }

    private void clearRequest() throws DataAccessException {
        HttpRequest.Builder request;
        HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.noBody();
        String serverURL = "http://localhost:" + port;
        String method = "DELETE";
        String path = "/db";

        AuthData auth;
        try {
            auth = facade.login(new UserData("username", "password", null));
        } catch (DataAccessException e) {
            auth = facade.register(new UserData("username","password","email"));
        }
        request = HttpRequest.newBuilder().uri(URI.create(serverURL + path)).method(method, requestBody);
        if (auth != null) {
            request.setHeader("Authorization", auth.authToken());
        }

        try {
            client.send(request.build(), HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new DataAccessException("Server error: " + ex.getMessage(), ex);
        }
    }

}
