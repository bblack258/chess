package server;

import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import service.*;
import dataaccess.*;

import java.util.List;

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
                .delete("/db", this::clear);

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

    private void clear(Context ctx) throws DataAccessException {
        clearService.clearAll();
        ctx.status(200);
        ctx.contentType("appliction/json");
        ctx.result(new Gson().toJson(List.of()));
    }
}
