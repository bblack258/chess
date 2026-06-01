package client;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.*;

import java.util.Arrays;
import java.util.Scanner;

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

    public String register(String... params) throws DataAccessException {
        if (params.length >= 3) {
            AuthData auth = server.register(new UserData(params[0], params[1], params[2]));
            state = State.LOGGED_IN;
            return String.format("You have registered and logged in as %s", auth.username());
        }
        return null;
    }

    public String login(String... params) throws DataAccessException {
        if (params.length >= 2) {
            AuthData auth = server.login(new UserData(params[0], params[1], null));
            state = State.LOGGED_IN;
            return String.format("You have logged in as %s", auth.username());
        }
        return null;
    }

    public String create(String... params) throws DataAccessException {
        isLoggedIn();
        if (params.length >= 1) {
            String game = server.create(params[0]);
            return String.format("Created new game: %s", game);
        }
        return "Failed to create game";
    }

    public String list(String... params) throws DataAccessException {
        isLoggedIn();
        GameList gameList = server.listGames();
        StringBuilder list = new StringBuilder();
        for (GameData game : gameList) {
            list.append(new Gson().toJson(game)).append('\n');
        }
        return list.toString();
    }

    public String join(String... params) throws DataAccessException {
        isLoggedIn();
        if (params.length >= 2) {
            int listID = Integer.parseInt(params[0]);
            GameList gameList = server.listGames();
            int gameID = gameList.get(listID - 1).gameID();
            JoinRequest join = new JoinRequest(params[1].toUpperCase(), gameID);
            server.join(join);
            return String.format("Successfully joined game %d", listID);
        }
        return "Failed to join game";
    }

    public String observe(String... params) throws DataAccessException {
        isLoggedIn();
        if (params.length >= 2) {
            int listID = Integer.parseInt(params[0]);
            GameList gameList = server.listGames();
            return gameList.get(listID - 1).game().toString();
        }
        return "Failed to observe";
    }

    public String logout(String... params) throws DataAccessException {
        isLoggedIn();
        server.logout();
        return "You have logged out";
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

    private void isLoggedIn() throws DataAccessException {
        if (state != State.LOGGED_IN) {
            throw new DataAccessException("Error: you must be signed in");
        }
    }
}
