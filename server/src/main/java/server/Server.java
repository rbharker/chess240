package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.*;
import service.ClearService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;
    MemoryUserDAO userDatabase = new MemoryUserDAO();
    MemoryGameDAO gameDatabase = new MemoryGameDAO();
    MemoryAuthDAO authDatabase = new MemoryAuthDAO();

    UserService userService = new UserService(userDatabase, authDatabase);
    GameService gameService = new GameService(authDatabase, gameDatabase);
    ClearService clearService = new ClearService(authDatabase, gameDatabase, userDatabase);

    ClearHandler clearHandler = new ClearHandler(clearService);
    UserHandler userHandler = new UserHandler(userService);
    GameHandler gameHandler = new GameHandler(gameService);

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        private void run() {
            Javalin.create(config -> config.staticFiles.add("web"))
                    .post("/user", userHandler::registerHandler)
                    .post("/session", userHandler::loginHandler)
                    .delete("/session", userHandler::loginHandler)
                    .get("/game", gameHandler::listGamesHandler)
                    .post("/game", gameHandler::createGameHandler)
                    .put("/game", gameHandler::joinGameHandler)
                    .delete("/db", clearHandler::clearApplicationHandler)
                    .start(8080);
        }


    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
