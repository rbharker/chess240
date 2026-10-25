package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.*;
import service.ClearService;
import service.GameService;
import service.UserService;

public class Server {
    // initialize javalin
    private final Javalin javalin;

    // initialize databases
    MemoryUserDAO userDatabase = new MemoryUserDAO();
    MemoryGameDAO gameDatabase = new MemoryGameDAO();
    MemoryAuthDAO authDatabase = new MemoryAuthDAO();

    // initialize service classes
    UserService userService = new UserService(userDatabase, authDatabase);
    GameService gameService = new GameService(authDatabase, gameDatabase);
    ClearService clearService = new ClearService(authDatabase, gameDatabase, userDatabase);

    // initialize handlers
    ClearHandler clearHandler = new ClearHandler(clearService);
    UserHandler userHandler = new UserHandler(userService);
    GameHandler gameHandler = new GameHandler(gameService);

    public Server() {
        // create a javalin instance and register all endpoints
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                    .post("/user", userHandler::registerHandler)
                    .post("/session", userHandler::loginHandler)
                    .delete("/session", userHandler::loginHandler)
                    .get("/game", gameHandler::listGamesHandler)
                    .post("/game", gameHandler::createGameHandler)
                    .put("/game", gameHandler::joinGameHandler)
                    .delete("/db", clearHandler::clearApplicationHandler);
    }

    // run the server on the desired port
    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    // stop running the server
    public void stop() {
        javalin.stop();
    }
}
