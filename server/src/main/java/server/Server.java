package server;

import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import model.*;
import service.*;
import dataaccess.*;

import java.util.Map;

public class Server {

    private final UserService userService;
//    private final GameService gameService;
    private final ClearService clearService;
    private final Javalin javalin;

    public Server() {
        this(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
    }

    public Server(UserDAO userMemory, AuthDAO authMemory, GameDAO gameMemory) {

        userService = new UserService(userMemory, authMemory);
//        gameService
        clearService = new ClearService(userMemory, authMemory, gameMemory);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("/user", this::register)
                .post("/session", this::login)
                .delete("/session", this::logout)
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
        //Don't forget to return the Json
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
