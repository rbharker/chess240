package client;

import java.util.Arrays;
import java.util.Scanner;

import com.google.gson.Gson;
import model.*;
import exception.ResponseException;
import server.ServerFacade;


public class ChessClient {
    private String visitorName = null;
    private final ServerFacade server;
    private State state = State.LOGGED_OUT;


    public ChessClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println("Welcome to Chess. Type Help to get started.");

        Scanner scanner = new Scanner(System.in);
        var result = "";
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
        switch (cmd) {
            case "login" -> state = State.LOGGED_IN;
            case "logout" -> state = State.LOGGED_OUT;
            case "quit" -> state = State.QUIT;
            default -> state = State.HELP;
        }
    }

    public void handlePreLogin() {
        return;
    }

    public void handlePostLogin() {
        return;
    }

    public void login() {
        return;
    }

    public void logout() {
        return;
    }

    public void

}
