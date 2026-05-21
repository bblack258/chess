package server;

import com.google.gson.stream.JsonReader;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.*;
import service.*;
import dataaccess.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

public class Server {

    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;
    private final Javalin javalin;

    public Server() {
        this(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
    }

    public Server(UserDAO userMemory, AuthDAO authMemory, GameDAO gameMemory) {

        userService = new UserService(userMemory, authMemory);
        gameService = new GameService(userMemory, authMemory, gameMemory);
        clearService = new ClearService(userMemory, authMemory, gameMemory);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("/user", this::register)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .get("/game", this::list)
                .post("/game", this::add)
                .put("/game", this::join)
                .delete("/db", this::clear)
                .exception(DataAccessException.class, this::dataAccessExceptionHandler);

        // Register your endpoints and exception handlers here.

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
        ctx.result(new Gson().toJson(games));
    }

    private void add(Context ctx) throws DataAccessException {
        String authToken = ctx.header("Authorization");
        System.out.println(ctx.body());
        GameData game = new Gson().fromJson(ctx.body(), GameData.class);
        int gameID = gameService.addGame(authToken, game.gameName());
        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(new Gson().toJson(Map.of("gameID", gameID)));
    }

    private void join(Context ctx) throws DataAccessException, IOException {
        String authToken = ctx.header("Authorization");
        JsonReader reader = new JsonReader(new StringReader(ctx.body()));
        reader.beginObject(); // switch to a try w/ resources
        while (reader.hasNext()) {
            String name = reader.nextName();
            String join = reader.nextString();
        }
        reader.endObject();
        joinRequest join = new Gson().fromJson(ctx.body(), joinRequest.class);
        gameService.joinGame(authToken, join.playerColor(), join.gameID());

        ctx.status(200);
        ctx.contentType("appliction/json");
    }

    private void clear(Context ctx) {
        clearService.clearAll();
        ctx.status(200);
        ctx.contentType("appliction/json");
    }

    private void dataAccessExceptionHandler(DataAccessException ex, Context ctx) {
        switch (ex) {
            case BadRequestException _ -> ctx.status(400);
            case UnauthorizedRequestException _ -> ctx.status(401);
            case AlreadyTakenException _ -> ctx.status(403);
            case null, default -> ctx.status(500);
        }
        ctx.contentType("application/json");
        if (ex != null) {
            ctx.result(new Gson().toJson(Map.of("message", ex.getMessage())));
        }
    }
}
