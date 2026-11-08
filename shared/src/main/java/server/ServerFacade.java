package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.*;
import result.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ServerFacade {

    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData registerUser(RegisterRequest registerRequest) throws ResponseException {
        var request = buildRequest("POST", "/user", registerRequest);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public AuthData loginUser(LoginRequest loginRequest) throws ResponseException {
        var request = buildRequest("POST", "/session", loginRequest);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public LogoutResult logoutUser(LogoutRequest logoutRequest) throws ResponseException {
        var request = buildRequest("DELETE", "/session", logoutRequest);
        var response = sendRequest(request);
        return handleResponse(response, LogoutResult.class);
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws ResponseException {
        var request = buildRequest("GET", "/game", listGamesRequest);
        var response = sendRequest(request);
        return handleResponse(response, ListGamesResult.class);
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws ResponseException {
        var request = buildRequest("POST", "/game", createGameRequest);
        var response = sendRequest(request);
        return handleResponse(response, CreateGameResult.class);
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        var request = buildRequest("PUT", "/session", joinGameRequest);
        var response = sendRequest(request);
        return handleResponse(response, JoinGameResult.class);
    }

    public ClearApplicationResult clearApplication() throws ResponseException {
        var request = buildRequest("DELETE", "/db", null);
        var response = sendRequest(request);
        return handleResponse(response, ClearApplicationResult.class);
    }

    // returns a formatted http request
    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    // creates the body of an http request
    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    // tries to send the http request
    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    // takes the http response from the sent request
    // determines what to do with it
    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    // checks if a 200 return status was sent
    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
