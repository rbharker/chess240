package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.*;
import java.io.*;
import java.net.*;
import java.util.Map;

import com.sun.net.httpserver.*;
import io.javalin.http.Context;
import service.GameService;

public class GameHandler {
    private GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public void listGamesHandler(Context ctx) {
        var serializer = new Gson();

        // convert the json request to my request type
        ListGamesRequest request = ctx.bodyAsClass(ListGamesRequest.class);

        // register the user and return the result as a json object
        try {
            ListGamesResult result = gameService.listGames(request);
            var jsonResult = serializer.toJson(result);
            ctx.json(jsonResult);
            ctx.status(200);

            // catch an exception and return the error as a json object with the error code
        } catch (DataAccessException e) {
            var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()),
                    "success", false));
            ctx.status(403);
            ctx.json(body);
        }
    }

    public void createGameHandler(Context ctx) {
        var serializer = new Gson();

        // convert the json request to my request type
        CreateGameRequest request = ctx.bodyAsClass(CreateGameRequest.class);

        // register the user and return the result as a json object
        try {
            CreateGameResult result = gameService.createGame(request);
            var jsonResult = serializer.toJson(result);
            ctx.json(jsonResult);
            ctx.status(200);

            // catch an exception and return the error as a json object with the error code
        } catch (DataAccessException e) {
            var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()),
                    "success", false));
            ctx.status(403);
            ctx.json(body);
        }
    }

    public void joinGameHandler(Context ctx) {
        var serializer = new Gson();

        // convert the json request to my request type
        JoinGameRequest request = ctx.bodyAsClass(JoinGameRequest.class);

        // register the user and return the result as a json object
        try {
            gameService.joinGame(request);
            var jsonResult = serializer.toJson(null);
            ctx.json(jsonResult);
            ctx.status(200);

            // catch an exception and return the error as a json object with the error code
        } catch (DataAccessException e) {
            var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()),
                    "success", false));
            ctx.status(403);
            ctx.json(body);
        }
    }
}
