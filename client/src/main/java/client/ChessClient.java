package client;

import dataaccesserrors.BadRequestException;
import dataaccesserrors.DataAccessException;
import model.*;
import ui.GenerateBoard;
import websocket.messages.ServerMessage;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient implements ServerMessageObserver {

    private final ServerFacade server;
    private final WebSocketFacade ws;
    private State state;
    private GameData game;
    private String board;
    private String teamColor;

    public ChessClient(String serverURL) throws DataAccessException {
        server = new ServerFacade(serverURL);
        ws = new WebSocketFacade(serverURL, this);
        state = State.LOGGED_OUT;
    }

    public void run() {
        System.out.println("Welcome to the CS240 chess project. Here are some ideas to start");
        System.out.println(SET_TEXT_COLOR_BLUE + help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            System.out.print(SET_TEXT_COLOR_GREEN + "[" + state + "]" + " >>> ");
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
            } catch (NumberFormatException | IndexOutOfBoundsException ex) {
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
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "makeMove" -> makeMove();
                case "resign" -> resign();
                case "highlight" -> highlight();
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
        alreadyInGame();
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
        alreadyInGame();
        if (params.length >= 2) {
            int listID = Integer.parseInt(params[0]);
            GameList gameList = server.listGames();
            int gameID = gameList.get(listID - 1).gameID();
            teamColor = params[1].toUpperCase();
            JoinRequest join = new JoinRequest(teamColor, gameID);
            server.join(join);

            state = State.PLAYING_GAME;
            System.out.printf("Successfully joined game %d%n", listID);
            ws.enterGame(server.getAuth().authToken(), gameID);
            game = gameList.get(listID - 1);
            board = new GenerateBoard().printBoard(game.game().getBoard(), teamColor);
            return board;
        }
        throw new BadRequestException("Failed to join game: must provide ID and color");
    }

    public String observe(String... params) throws DataAccessException {
        isLoggedIn();
        alreadyInGame();
        if (params.length >= 1) {
            int listID = Integer.parseInt(params[0]);
            GameList gameList = server.listGames();
            state = State.PLAYING_GAME;
            game = gameList.get(listID - 1);
            board = new GenerateBoard().printBoard(game.game().getBoard(), "White");
            return board;
        }
        throw new BadRequestException("Failed to observe: must provide game ID");
    }

    public String logout() throws DataAccessException {
        isLoggedIn();
        alreadyInGame();
        server.logout();
        state = State.LOGGED_OUT;
        return "You have logged out";
    }

    public String redraw() throws DataAccessException {
        notInGame();
        return board;
    }

    public String leave() throws DataAccessException {
        notInGame();
        JoinRequest leave = new JoinRequest(teamColor, game.gameID());
//        server.leave(leave);
        state = State.LOGGED_IN;
        return "You have left game: " + game;
    }

    public String makeMove(String... params) throws DataAccessException {
        isGameOver();
//      new update game with websocket
        return null;
    }

    public String resign() throws DataAccessException {
        isGameOver();
        System.out.println("Are you sure you want to resign?");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
//      update game and change gameOver to true
        return null;
    }

    public String highlight(String... params) throws DataAccessException {
        notInGame();
//      call new function in generate board to highlight given squares for a move set
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
        } else if (state == State.LOGGED_IN) {
            return """
                create <NAME> - make a new chess game
                list - list all current chess games
                join <ID> <WHITE|BLACK> - join a chess game
                observe <ID> - watch a chess game
                help - get possible commands
                logout - log out of your account
                """;
        }
        return """
                redraw - redraw the chess board
                makeMove <START> <END> - make a move from start position to end position
                resign - forfeit and end the game
                highlight <POSITION> - highlight legal moves for a selected piece at given position
                help - get possible commands
                leave - leave the current game
                """;
    }

    private void isLoggedOut() throws DataAccessException {
        if (state != State.LOGGED_OUT) {
            throw new DataAccessException("Error: you are already signed in");
        }
    }

    private void isLoggedIn() throws DataAccessException {
        if (state == State.LOGGED_OUT) {
            throw new DataAccessException("Error: you must be signed in");
        }
    }

    private void notInGame() throws DataAccessException {
        if (state != State.PLAYING_GAME && state != State.OBSERVING) {
            throw new DataAccessException("Error: you must be in a game");
        }
    }

    private void alreadyInGame() throws DataAccessException {
        if (state == State.PLAYING_GAME || state == State.OBSERVING) {
            throw new DataAccessException("Error: you are currently in a game");
        }
    }

    private void isGameOver() throws DataAccessException {
        notInGame();
        if (!game.gameOver()) {
            throw new DataAccessException("Error: game is over");
        }
    }

    @Override
    public void notify(ServerMessage message) {
        System.out.println(SET_TEXT_COLOR_MAGENTA + message.toString());
        System.out.print(SET_TEXT_COLOR_GREEN + "[" + state + "]" + " >>> ");
    }
}
