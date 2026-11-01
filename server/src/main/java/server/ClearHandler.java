package server;

import com.google.gson.Gson;
import result.ClearApplicationResult;

import io.javalin.http.Context;
import service.ClearService;

import java.util.Map;

/*
    Class that contains handler for the clear database endpoint
*/
public class ClearHandler {
    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public void clearApplicationHandler(Context ctx) {
        var serializer = new Gson();
        var authToken = ctx.header("authorization");
        try {
            ClearApplicationResult clearApplicationResult = clearService.clearApplication();
            var jsonResult = serializer.toJson(clearApplicationResult);
            ctx.json(jsonResult);
            ctx.status(200);
        } catch (RuntimeException e) {
            var excp = new Gson().toJson(Map.of("message", String.format("Error: unauthorized"),
                    "success", false));
            ctx.status(500);
            ctx.json(excp);
        }
    }
}
