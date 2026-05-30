package client;

import dataaccess.DataAccessException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.DataFormatException;

import static ui.EscapeSequences.*;

public class ChessClient {

    private final ServerFacade server;
    private State state;

    public ChessClient(String serverURL) {
        server = new ServerFacade(serverURL);
        state = State.LOGGED_OUT;
    }

    public void run() {
        System.out.println("Welcome to the CS240 chess project. Type 'help' for ideas to start");
        System.out.println(help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            System.out.println(SET_TEXT_COLOR_GREEN + "\n>>>");
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println();
    }

    public String eval(String command) {
        try {
            String[] tokens = command.toLowerCase().split(" ");
            String request = "help";
            if (tokens.length > 0) {
                request = tokens[0];
            }
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (request) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (DataAccessException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) {
        return null;
    }

    public String login(String... params) {
        return null;
    }

    public String create(String... params) {
        return null;
    }

    public String list(String... params) {
        return null;
    }

    public String join(String... params) {
        return null;
    }

    public String observe(String... params) {
        return null;
    }

    public String logout(String... params) {
        return null;
    }

    public String help() {
        if (state == State.LOGGED_OUT) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - create a new account
                    login <USERNAME> <PASSWORD> - log back in to your account
                    help - get possible commands
                    quit - stop playing chess
                    """;
        }
        return """
                create <NAME> - make a new chess game
                list - list all current chess games
                join <ID> <WHITE|BLACK> - join a chess game
                observe <ID> - watch a chess game
                help - get possible commands
                logout - log out of your account
                """;
    }

    private boolean isLoggedIn() throws DataAccessException {
        if (state != State.LOGGED_IN) {
            throw new DataAccessException("Error: you must be signed in");
        }
        return true;
    }
}
