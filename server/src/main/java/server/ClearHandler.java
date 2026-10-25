package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.*;
import java.io.*;
import java.net.*;
import java.util.Map;

import com.sun.net.httpserver.*;
import io.javalin.http.Context;
import service.ClearService;

/*
    Class that contains handler for the clear database endpoint
*/
public class ClearHandler {
    private ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void clearApplicationHandler(Context ctx) {
        var serializer = new Gson();
        var authToken = ctx.header("authorization");
        clearService.clearApplication();
        var jsonResult = serializer.toJson(null);
        ctx.json(jsonResult);
        ctx.status(200);
    }
}
