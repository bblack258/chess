package server;

import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import model.*;
import server.websocket.WebSocketHandler;
import service.*;
import dataaccess.*;
import dataaccesserrors.*;

import java.util.List;
import java.util.Map;

public class Server {

    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;
    private final Javalin javalin;
    private final WebSocketHandler webSocketHandler;

    public Server() { this(new MySQLUserDAO(), new MySQLAuthDAO(), new MySQLGameDAO()); }

    public Server(UserDAO userMemory, AuthDAO authMemory, GameDAO gameMemory) {
        userService = new UserService(userMemory, authMemory);
        gameService = new GameService(authMemory, gameMemory);
        clearService = new ClearService(userMemory, authMemory, gameMemory);

        webSocketHandler = new WebSocketHandler();

        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("/user", this::register)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .get("/game", this::list)
                .post("/game", this::add)
                .put("/game", this::join)
                .delete("/db", this::clear)
                .exception(DataAccessException.class, this::dataAccessExceptionHandler)
                .ws("/ws", ws -> {
                    ws.onConnect(webSocketHandler);
                    ws.onMessage(webSocketHandler);
                    ws.onClose(webSocketHandler);
                });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void register(Context ctx) throws DataAccessException {
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData auth = userService.register(user);

        ctx.status(200);
        ctx.contentType("appliction/json");
        ctx.result(new Gson().toJson(auth));
    }

    private void login(Context ctx) throws DataAccessException {
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        AuthData auth = userService.login(user);

        ctx.status(200);
        ctx.contentType("appliction/json");
        ctx.result(new Gson().toJson(auth));
    }

    private void logout(Context ctx) throws DataAccessException {
        String authToken = ctx.header("Authorization");
        userService.logout(authToken);

        ctx.status(200);
        ctx.contentType("application/json");
    }

    private void list(Context ctx) throws DataAccessException {
        String authToken = ctx.header("Authorization");
        List<GameData> games = gameService.listGames(authToken);

        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(new Gson().toJson(Map.of("games", games)));
    }

    private void add(Context ctx) throws DataAccessException {
        String authToken = ctx.header("Authorization");
        GameData game = new Gson().fromJson(ctx.body(), GameData.class);
        int gameID = gameService.addGame(authToken, game.gameName());

        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(new Gson().toJson(Map.of("gameID", gameID)));
    }

    private void join(Context ctx) throws DataAccessException {
        String authToken = ctx.header("Authorization");
        JoinRequest join = new Gson().fromJson(ctx.body(), JoinRequest.class);
        gameService.joinGame(authToken, join.playerColor(), join.gameID());

        ctx.status(200);
        ctx.contentType("appliction/json");
    }

    private void clear(Context ctx) throws DataAccessException {
        clearService.clearAll();

        ctx.status(200);
        ctx.contentType("appliction/json");
    }

    private void dataAccessExceptionHandler(DataAccessException ex, Context ctx) {
        switch (ex) {
            case BadRequestException ignored -> ctx.status(400);
            case UnauthorizedRequestException ignored -> ctx.status(401);
            case AlreadyTakenException ignored -> ctx.status(403);
            case null, default -> ctx.status(500);
        }
        ctx.contentType("application/json");
        if (ex != null) {
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        }
    }
}
