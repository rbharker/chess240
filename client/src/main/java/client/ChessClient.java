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
    private String visitorName = null;
    private final ServerFacade server;
    private State state = State.LOGGED_OUT;
    private AuthData authData = null;
    private UserData userData = null;
    private List<GameData> games = null;

    public ChessClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to Chess. Type Help to get started.");

        Scanner scanner = new Scanner(System.in);
        while (!state.equals(State.QUIT)) {
            String input = scanner.nextLine();
            eval(input);
//            if (state == State.LOGGED_OUT) {
//                handlePreLogin();
//            } else {
//                handlePostLogin();
//            }
        }
    }

    public void eval(String input) {
        String[] tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            switch (cmd) {
                case "register" -> register();
                case "login" -> login();
                case "logout" -> logout();
                case "help" -> help();
                case "quit" -> state = State.QUIT;
                case "create" -> createGame();
                case "play" -> joinGame(tokens[2], tokens[3]);
                case "list" -> listGames();
                case "observe" -> observeGame(tokens[2]);
                default -> help();
            }
        } catch (ResponseException e) {
            System.out.println("Error: bad request");
        }
    }

//    public void handlePreLogin() {
//        // i think this will just print options
//
//        return;
//    }
//
//    public void handlePostLogin() {
//        // i think this will just print options
//
//        return;
//    }

    public void register() throws ResponseException {
        System.out.println("Username:");
        Scanner userNameScanner = new Scanner(System.in);
        var username = "";

        while (!username.equals("quit")) {
            username = userNameScanner.nextLine();
        }

        System.out.println("Password:");
        Scanner passwordScanner = new Scanner(System.in);
        var password = "";

        while (!password.equals("quit")) {
            password = passwordScanner.nextLine();
        }

        System.out.println("Email:");
        Scanner emailScanner = new Scanner(System.in);
        var email = "";

        while (!email.equals("quit")) {
            email = emailScanner.nextLine();
        }

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
        var username = "";

        while (!username.equals("quit")) {
            username = userNameScanner.nextLine();
        }

        System.out.println("Password:");
        Scanner passwordScanner = new Scanner(System.in);
        var password = "";

        while (!password.equals("quit")) {
            username = passwordScanner.nextLine();
        }

        LoginRequest loginData = new LoginRequest(username, password);
        try {
            authData = server.loginUser(loginData);
            state = State.LOGGED_IN;
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
            } catch (ResponseException e) {
                System.out.println("Error: bad request");
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
        var gameName = "";

        while (!gameName.equals("quit")) {
            gameName = gameNameScanner.nextLine();
        }

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
        return;
    }

    public void quit() {
            return;
        }

}
