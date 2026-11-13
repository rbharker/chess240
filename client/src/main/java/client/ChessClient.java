package client;

import java.util.Arrays;
import java.util.Scanner;

import com.google.gson.Gson;
import model.*;
import exception.ResponseException;
import request.LoginRequest;
import request.LogoutRequest;
import request.RegisterRequest;
import result.LogoutResult;
import server.ServerFacade;


public class ChessClient {
    private String visitorName = null;
    private final ServerFacade server;
    private State state = State.LOGGED_OUT;
    private AuthData authData = null;
    private UserData userData = null;

    public ChessClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to Chess. Type Help to get started.");

        Scanner scanner = new Scanner(System.in);
        while (!state.equals(State.QUIT)) {
            String input = scanner.nextLine();
            eval(input);
            if (state == State.LOGGED_OUT) {
                handlePreLogin();
            } else {
                handlePostLogin();
            }
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
                default -> help();
            }
        } catch (ResponseException e) {
            System.out.println("Error: bad request");
        }
    }

    public void handlePreLogin() {
        return;
    }

    public void handlePostLogin() {
        return;
    }

    public void register() throws ResponseException {
        System.out.println("username:");
        Scanner userNameScanner = new Scanner(System.in);
        var username = "";

        while (!username.equals("quit")) {
            username = userNameScanner.nextLine();
        }

        System.out.println("password:");
        Scanner passwordScanner = new Scanner(System.in);
        var password = "";

        while (!password.equals("quit")) {
            password = passwordScanner.nextLine();
        }

        System.out.println("email:");
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
        System.out.println("username:");
        Scanner userNameScanner = new Scanner(System.in);
        var username = "";

        while (!username.equals("quit")) {
            username = userNameScanner.nextLine();
        }

        System.out.println("password:");
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
        return;
    }

    public void createGame() {
        return;
    }

    public void listGames() {
        return;
    }

    public void joinGame() {
        return;
    }

    public void observerGame() {
        return;
    }

    public void help() {
        return;
    }

    public void quit() {
            return;
        }

}
