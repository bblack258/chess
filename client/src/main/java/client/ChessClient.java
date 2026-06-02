package client;

import dataaccesserrors.BadRequestException;
import dataaccesserrors.DataAccessException;
import model.*;
import ui.GenerateBoard;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {

    private final ServerFacade server;
    private State state;
    private final GenerateBoard drawBoard;

    public ChessClient(String serverURL) {
        server = new ServerFacade(serverURL);
        state = State.LOGGED_OUT;
        drawBoard = new GenerateBoard();
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
            } catch (NumberFormatException ex) {
                System.out.println(SET_TEXT_COLOR_BLUE + "Please enter a valid ID");
            } catch (Exception ex) {
                System.out.println(SET_TEXT_COLOR_BLUE + ex.getMessage());
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
        isLoggedOut();
        if (params.length >= 3) {
            AuthData auth = server.register(new UserData(params[0], params[1], params[2]));
            state = State.LOGGED_IN;
            return String.format("You have registered and logged in as %s", auth.username());
        }
        throw new BadRequestException("Error: must have username, password, and email");
    }

    public String login(String... params) throws DataAccessException {
        isLoggedOut();
        if (params.length >= 2) {
            AuthData auth = server.login(new UserData(params[0], params[1], null));
            state = State.LOGGED_IN;
            return String.format("You have logged in as %s", auth.username());
        }
        throw new BadRequestException("Error: must contain username and password");
    }

    public String create(String... params) throws DataAccessException {
        isLoggedIn();
        if (params.length >= 1) {
            String game = server.create(params[0]);
            return String.format("Successfully created new game: %s", game);
        }
        throw new BadRequestException("Failed to create game: must provide game name");
    }

    public String list() throws DataAccessException {
        isLoggedIn();
        GameList gameList = server.listGames();
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < gameList.size(); i++) {
            GameData game = gameList.get(i);
            list.append(String.format("%d. Game name: %-15s", i+1, game.gameName()));
            list.append(String.format("White: %-10s", game.whiteUsername()));
            list.append(String.format("Black: %-10s", game.blackUsername()));
            list.append('\n');
        }
        return list.toString();
    }

    public String join(String... params) throws DataAccessException {
        isLoggedIn();
        if (params.length >= 2) {
            int listID = Integer.parseInt(params[0]);
            GameList gameList = server.listGames();
            if (!(listID > 0 && listID <= gameList.size())) {
                throw new BadRequestException("Please enter a valid ID");
            }
            int gameID = gameList.get(listID - 1).gameID();
            JoinRequest join = new JoinRequest(params[1].toUpperCase(), gameID);
            server.join(join);
            System.out.printf("Successfully joined game %d%n", listID);
            return drawBoard.printBoard(gameList.get(listID - 1).game().getBoard(), params[1].toUpperCase());
        }
        throw new BadRequestException("Failed to join game: must provide ID and color");
    }

    public String observe(String... params) throws DataAccessException {
        isLoggedIn();
        if (params.length >= 1) {
            int listID = Integer.parseInt(params[0]);
            GameList gameList = server.listGames();
            if (!(listID > 0 && listID <= gameList.size())) {
                throw new BadRequestException("Please enter a valid ID");
            }
            return drawBoard.printBoard(gameList.get(listID - 1).game().getBoard(), "White");
        }
        throw new BadRequestException("Failed to observe: must provide game ID");
    }

    public String logout() throws DataAccessException {
        isLoggedIn();
        server.logout();
        state = State.LOGGED_OUT;
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

    private void isLoggedOut() throws DataAccessException {
        if (state != State.LOGGED_OUT) {
            throw new DataAccessException("Error: you are already signed in");
        }
    }

    private void isLoggedIn() throws DataAccessException {
        if (state != State.LOGGED_IN) {
            throw new DataAccessException("Error: you must be signed in");
        }
    }
}
