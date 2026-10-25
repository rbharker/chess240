package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.ListGamesRequest;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;

import java.util.Map;

import io.javalin.http.Context;
import service.GameService;

/*
    Class that contains handlers for each game endpoint
*/
public class GameHandler {
    private final GameService gameService;
    private final BodyObject bodyObject = new BodyObject();

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    /*
        Method used to handle the list games endpoint
    */
    public void listGamesHandler(Context ctx) {
        var serializer = new Gson();

        // convert the json request to list games request type
        String authToken = ctx.header("authorization");
        ListGamesRequest request = new ListGamesRequest(authToken);

        // register the user and return the result as a json object
        try {
            ListGamesResult result = gameService.listGames(request);
            var jsonResult = serializer.toJson(result);
            ctx.json(jsonResult);
            ctx.status(200);

            // catch an exception and return the error as a json object with the error code
        } catch (DataAccessException e) {
            var listGamesBody = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()),
                    "success", false));
            ctx.status(401);
            ctx.json(listGamesBody);
        }
    }

    /*
        Method used to handle the create games endpoint
    */
    public void createGameHandler(Context ctx) {
        var serializer = new Gson();

        // convert the json request to create game request type
        CreateGameRequest body = bodyObject.getBodyObject(ctx, CreateGameRequest.class);
        String authToken = ctx.header("authorization");
        CreateGameRequest request = new CreateGameRequest(authToken, body.gameName());

        // register the user and return the result as a json object
        try {
            CreateGameResult result = gameService.createGame(request);
            var jsonResult = serializer.toJson(result);
            ctx.json(jsonResult);
            ctx.status(200);

            // catch an exception and return the error as a json object with the error code
        } catch (DataAccessException e) {
            var excp = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()),
                    "success", false));
            if (e.getMessage().equals("unauthorized")) {
                ctx.status(401);
            }
            else if (e.getMessage().equals("bad request")) {
                ctx.status(400);
            }
            ctx.json(excp);
        }
    }

    /*
        Method used to handle the join game endpoint
    */
    public void joinGameHandler(Context ctx) {
        var serializer = new Gson();

        // convert the json request to join game request type
        JoinGameRequest body = bodyObject.getBodyObject(ctx, JoinGameRequest.class);
        String authToken = ctx.header("authorization");
        JoinGameRequest request = new JoinGameRequest(authToken, body.gameID(), body.playerColor());

        // register the user and return the result as a json object
        try {
            JoinGameResult joinGameResult = gameService.joinGame(request);
            var jsonResult = serializer.toJson(joinGameResult);
            ctx.json(jsonResult);
            ctx.status(200);

            // catch an exception and return the error as a json object with the error code
        } catch (DataAccessException e) {
            var excp = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()),
                    "success", false));
            if (e.getMessage().equals("bad request")) {
                ctx.status(400);
            }
            else if (e.getMessage().equals("unauthorized")) {
                ctx.status(401);
            }
            else if (e.getMessage().equals("already taken")) {
                ctx.status(403);
            }
            ctx.json(excp);
        }
    }
}
