package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.*;
import service.UserService;

public class Server {

    private final Javalin javalin;
    MemoryUserDAO userDatabase = new MemoryUserDAO();
    MemoryGameDAO gameDatabase = new MemoryGameDAO();
    MemoryAuthDAO authDatabase = new MemoryAuthDAO();

    UserService userService = new UserService()

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
