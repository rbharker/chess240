package server;

import com.google.gson.Gson;
import io.javalin.http.Context;

public class BodyObject {

    public BodyObject() {

    }

    public static <T> T getBodyObject(Context context, Class<T> clazz) {
        var bodyObject = new Gson().fromJson(context.body(), clazz);

        if (bodyObject == null) {
            throw new RuntimeException("missing required body");
        }

        return bodyObject;
    }
}
