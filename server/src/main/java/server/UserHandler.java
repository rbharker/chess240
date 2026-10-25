package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.*;
import java.io.*;
import java.net.*;
import java.util.Map;

import com.sun.net.httpserver.*;
import io.javalin.http.Context;
import service.UserService;

/*
    Class that contains handlers for each user endpoint
*/
public class UserHandler {

    private UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    /*
        Method used to handle the register user endpoint
    */
    public void registerHandler(Context ctx) {
        var serializer = new Gson();

        // convert the json request to my request type
        RegisterRequest request = ctx.bodyAsClass(RegisterRequest.class);

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
            ctx.status(403);
            ctx.json(body);
        }
    }

    /*
        Method used to handle the login user endpoint
    */
    public void loginHandler(Context ctx) {
        var serializer = new Gson();

        // convert the json request to my request type
        LoginRequest request = ctx.bodyAsClass(LoginRequest.class);

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
            ctx.status(401);
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
            userService.logout(request);
            var jsonResult = serializer.toJson(null);
            ctx.json(jsonResult);
            ctx.status(200);

        } catch (DataAccessException e) {
            var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()),
                    "success", false));
            ctx.status(401);
            ctx.json(body);
        }
    }
}
