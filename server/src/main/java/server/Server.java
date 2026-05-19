package server;

import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import service.*;
import dataaccess.*;

public class Server {

    private final UserService userService;
    private final Javalin javalin;

    public Server() {
        this(new MemoryUserDAO(), new MemoryAuthDAO());
    }

    public Server(UserDAO userMemory, AuthDAO authMemory) {

        userService = new UserService(userMemory, authMemory);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.post("/user", this::register);

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
        UserData user = new UserData(ctx.pathParam("username"),ctx.pathParam("password"),ctx.pathParam("email"));
        AuthData auth = userService.register(user);
        //Don't forget to return the json
        ctx.status(200);
        ctx.contentType("appliction/json");
        ctx.result(new Gson().toJson(auth));
    }
}
