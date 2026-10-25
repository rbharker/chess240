package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.LogoutResult;
import result.RegisterResult;

import java.util.Map;

import io.javalin.http.Context;
import service.UserService;

/*
    Class that contains handlers for each user endpoint
*/
public class UserHandler {

    private final UserService userService;
    private final BodyObject bodyObject = new BodyObject();

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    /*
        Method used to handle the register user endpoint
    */
    public void registerHandler(Context ctx) {
        var serializer = new Gson();

        // convert the json request to my request type
        RegisterRequest request = bodyObject.getBodyObject(ctx, RegisterRequest.class);

        // register the user and return the result as a json object
        try {
            RegisterResult result = userService.register(request);
            var jsonResult = serializer.toJson(result);
            ctx.json(jsonResult);
            ctx.status(200);

        // catch an exception and return the error as a json object with the error code
        } catch (DataAccessException e) {
            var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()),
                    "success", false));
            if (e.getMessage().equals("already taken")) {
                ctx.status(403);
            }
            else if (e.getMessage().equals("bad request")) {
                ctx.status(400);
            }
            ctx.json(body);
        }
    }

    /*
        Method used to handle the login user endpoint
    */
    public void loginHandler(Context ctx) {
        var serializer = new Gson();

        // convert the json request to my request type
        LoginRequest request = bodyObject.getBodyObject(ctx, LoginRequest.class);

        // login the user and return the result as a json object
        try {
            LoginResult result = userService.login(request);
            var jsonResult = serializer.toJson(result);
            ctx.json(jsonResult);
            ctx.status(200);

        // catch an exception and return the error as a json object with the error code
        } catch (DataAccessException e) {
            var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()),
                    "success", false));
            if (e.getMessage().equals("bad request")) {
                ctx.status(400);
            }
            else if (e.getMessage().equals("unauthorized")) {
                ctx.status(401);
            }
            ctx.json(body);
        }
    }

    /*
        Method used to handle the logout user endpoint
    */
    public void logoutHandler(Context ctx) {
        var serializer = new Gson();
        var authToken = ctx.header("authorization");
        LogoutRequest request = new LogoutRequest(authToken);
        try {
            LogoutResult logoutResult = userService.logout(request);
            var jsonResult = serializer.toJson(logoutResult);
            ctx.json(jsonResult);
            ctx.status(200);

        } catch (DataAccessException e) {
            var logoutBody = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()),
                    "success", false));
            ctx.status(401);
            ctx.json(logoutBody);
        }
    }
}
