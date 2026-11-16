package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import model.*;
import exception.ResponseException;
import request.*;
import result.CreateGameResult;
import result.JoinGameResult;
import result.ListGamesResult;
import result.LogoutResult;
import server.ServerFacade;


public class ChessClient {
    private final ServerFacade server;
    private State state = State.LOGGED_OUT;
    private AuthData authData = null;
    private UserData userData = null;
    private List<GameData> games = null;

    public ChessClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        try {
            server.clearApplication();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Welcome to Chess. Type Help to get started.");
        while (!state.equals(State.QUIT)) {
            if (state == State.LOGGED_OUT) {
                handlePreLogin();
            } else {
                handlePostLogin();
            }
        }
        System.out.println();
    }

    public void evalPreLogin(String input) {
        String[] preTokens = input.toLowerCase().split(" ");
        String cmd = (preTokens.length > 0) ? preTokens[0] : "help";
        try {
            switch (cmd) {
                case "register" -> register();
                case "login" -> login();
                case "help" -> help();
                case "quit" -> state = State.QUIT;
                default -> help();
            }
        } catch (ResponseException e) {
            System.out.println("Error: bad request");
        }
    }

    public void evalPostLogin(String input) {
        String[] postTokens = input.toLowerCase().split(" ");
        String cmd = (postTokens.length > 0) ? postTokens[0] : "help";
        String[] params = Arrays.copyOfRange(postTokens, 1, postTokens.length);
        switch (cmd) {
            case "logout" -> logout();
            case "help" -> help();
            case "quit" -> state = State.QUIT;
            case "create" -> createGame();
            case "play" -> joinGame(params[1], params[2]);
            case "list" -> listGames();
            case "observe" -> observeGame(params[1]);
            default -> help();
        }
    }

    public void handlePreLogin() {
        Scanner preLoginScanner = new Scanner(System.in);
        String preLoginInput = preLoginScanner.nextLine();
        evalPreLogin(preLoginInput);
    }

    public void handlePostLogin() {
        Scanner postLoginScanner = new Scanner(System.in);
        String postLoginInput = postLoginScanner.nextLine();
        evalPostLogin(postLoginInput);
    }

    public void register() throws ResponseException {
        System.out.println("Username:");
        Scanner userNameScanner = new Scanner(System.in);
        var username = userNameScanner.nextLine();

        System.out.println("Password:");
        Scanner passwordScanner = new Scanner(System.in);
        var password = passwordScanner.nextLine();

        System.out.println("Email:");
        Scanner emailScanner = new Scanner(System.in);
        var email = emailScanner.nextLine();

        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        try {
            server.registerUser(registerRequest);
            userData = new UserData(username, password, email);
        } catch (ResponseException e) {
            System.out.println("Error: bad request");
        }
    }

    public void login() throws ResponseException {
        System.out.println("Username:");
        Scanner userNameScanner = new Scanner(System.in);
        var username = userNameScanner.nextLine();

        System.out.println("Password:");
        Scanner passwordScanner = new Scanner(System.in);
        var password = passwordScanner.nextLine();

        LoginRequest loginData = new LoginRequest(username, password);
        try {
            authData = server.loginUser(loginData);
            state = State.LOGGED_IN;
            System.out.println("Successfully logged in.");
        } catch (ResponseException e) {
            System.out.println("Error: bad request");
        }
    }

    public void logout() {
        if (authData != null) {
            LogoutRequest logoutRequest = new LogoutRequest(authData.authToken());
            try {
                server.logoutUser(logoutRequest);
                authData = null;
                System.out.println("Successfully logged out.");
            } catch (ResponseException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        else {
            System.out.println("Error: unauthorized");
        }
        state = State.LOGGED_OUT;
    }

    public void createGame() {
        System.out.println("Game name:");
        Scanner gameNameScanner = new Scanner(System.in);
        var gameName = gameNameScanner.nextLine();

        CreateGameRequest createGameRequest = new CreateGameRequest(authData.authToken(), gameName);

        try {
            CreateGameResult createGameResult = server.createGame(createGameRequest);
            System.out.println("Game created!");
        } catch (ResponseException e) {
            System.out.println("Error: bad request");
        }
    }

    public void listGames() {
        ListGamesRequest listGamesRequest = new ListGamesRequest(authData.authToken());
        try {
            ListGamesResult listGamesResult = server.listGames(listGamesRequest);
            int i = 1;
            for (GameData game : listGamesResult.games()) {
                System.out.println("Game " + i);
                System.out.println("Game name: " + game.gameName());
                System.out.println("White player:" + game.whiteUsername());
                System.out.println("Black player:" + game.blackUsername());
                i++;
                games.add(game);
            }
        } catch (ResponseException e) {
            System.out.println("Error: bad request");
        }
    }

    public void joinGame(String gameNum, String color) {
        int num = Integer.parseInt(gameNum);
        if (color.compareToIgnoreCase("WHITE") == 0) {
            color = "WHITE";
        }
        else if (color.compareToIgnoreCase("BLACK") == 0) {
            color = "BLACK";
        }
        else {
            System.out.println("Error: bad request");
            return;
        }
        String game = String.valueOf(games.get(num-1).gameID());
        JoinGameRequest joinGameRequest = new JoinGameRequest(authData.authToken(), game, color);
        try {
            JoinGameResult joinGameResult = server.joinGame(joinGameRequest);
            System.out.println("Game joined!");
        } catch (ResponseException e) {
            System.out.println("Error: bad request");
        }
    }

    public void observeGame(String gameNum) {
        int num = Integer.parseInt(gameNum);
        String game = String.valueOf(games.get(num-1).gameID());
        System.out.println("Game observed!");
    }

    public void help() {
        if (state == State.LOGGED_OUT) {
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
            System.out.println("login <USERNAME> <PASSWORD> - to login");
            System.out.println("quit - quit the application");
            System.out.println("help - list possible commands");
        }
        else if (state == State.LOGGED_IN) {
            System.out.println("logout - to logout of your account");
            System.out.println("quit - quit playing chess");
            System.out.println("help - list possible commands");
            System.out.println("create game - to create a chess game");
            System.out.println("list games - to receive a list of all active chess games");
            System.out.println("play game <GAME #> <COLOR (WHITE/BLACK)> - to play a chess game");
            System.out.println("observe game <GAME #> - to observe a chess game");
        }
    }

    public void quit() {
        return;
    }

}
