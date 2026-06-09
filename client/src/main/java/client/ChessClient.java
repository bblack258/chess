package client;

import chess.*;
import dataaccesserrors.BadRequestException;
import dataaccesserrors.DataAccessException;
import model.*;
import ui.GenerateBoard;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient implements ServerMessageObserver {

    private final ServerFacade server;
    private final WebSocketFacade ws;
    private State state;
    private GameData game;
    private ChessBoard board;
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
        String quit = "quit";
        while (!quit.equals(result)) {
            System.out.print(SET_TEXT_COLOR_GREEN + "[" + state + "]" + " >>> ");
            String line = scanner.nextLine();

            try {
                result = eval(line);
                if (result != null) {
                    System.out.println(SET_TEXT_COLOR_BLUE + result);
                }
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
                case "makemove" -> makeMove(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
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
            ws.enterGame(server.getAuth().authToken(), gameID);
            game = gameList.get(listID - 1);
            board = game.game().getBoard();
            return String.format("Successfully joined game %d%n", listID);
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
            teamColor = "White";
            ws.enterGame(server.getAuth().authToken(), game.gameID());
            board = game.game().getBoard();
            return String.format("Observing game: %d", game.gameID());
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
        return new GenerateBoard().printBoard(board, teamColor);
    }

    public String leave() throws DataAccessException {
        notInGame();
        ws.leave(server.getAuth().authToken(), game.gameID());
        state = State.LOGGED_IN;
        return "You have left the game";
    }

    public String makeMove(String... params) throws DataAccessException {
        notInGame();
        int startCol = read(params[0].charAt(0));
        int startRow = Character.getNumericValue(params[0].charAt(1));
        int endCol = read(params[1].charAt(0));
        int endRow = Character.getNumericValue(params[1].charAt(1));
        if (startCol == 0 || endCol == 0 || startRow < 1 || startRow > 8 || endRow < 1 || endRow > 8) {
            throw new DataAccessException("Error: Invalid move");
        }
        ChessPiece.PieceType promotionPiece = null;
        if (params.length >= 3) {
            String piece = params[2];
            switch (piece) {
                case "queen" -> promotionPiece = ChessPiece.PieceType.QUEEN;
                case "knight" -> promotionPiece = ChessPiece.PieceType.KNIGHT;
                case "bishop" -> promotionPiece = ChessPiece.PieceType.BISHOP;
                case "rook" -> promotionPiece = ChessPiece.PieceType.ROOK;
                default -> throw new BadRequestException("Error: please enter a valid promotion piece after the move");
            }
        }
        ChessMove move = new ChessMove(new ChessPosition(startRow,startCol), new ChessPosition(endRow, endCol), promotionPiece);

        ws.move(server.getAuth().authToken(), game.gameID(), move);
        return null;
    }

    public String resign() throws DataAccessException {
        notInGame();
        System.out.println("Are you sure you want to resign? [Y/N]");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if (line.equalsIgnoreCase("Y")) {
            ws.resign(server.getAuth().authToken(), game.gameID());
        }
        return null;
    }

    public String highlight(String... params) throws DataAccessException {
        notInGame();
        int startCol = read(params[0].charAt(0));
        int startRow = Character.getNumericValue(params[0].charAt(1));
        ChessPosition startPosition = new ChessPosition(startRow, startCol);
        return new GenerateBoard().highlightBoard(game.game(),teamColor, startPosition);
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

    @Override
    public void notify(ServerMessage message) {
        if (message instanceof NotificationMessage) {
            System.out.println(SET_TEXT_COLOR_MAGENTA + ((NotificationMessage) message).getNotification());
        } else if (message instanceof ErrorMessage) {
            System.out.println(SET_TEXT_COLOR_MAGENTA + ((ErrorMessage) message).getError());
        } else {
            board = ((LoadGameMessage) message).getGame();
            ChessGame chess = new ChessGame();
            chess.setBoard(board);
            chess.setTeamTurn(game.game().getTeamTurn());
            game = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), chess, game.gameOver());
            System.out.println("\n" + new GenerateBoard().printBoard(((LoadGameMessage) message).getGame(), teamColor));
        }
        System.out.print(SET_TEXT_COLOR_GREEN + "[" + state + "]" + " >>> ");
    }

    private int read(char col) {
        switch (col) {
            case 'a' -> { return 1; }
            case 'b' -> { return 2; }
            case 'c' -> { return 3; }
            case 'd' -> { return 4; }
            case 'e' -> { return 5; }
            case 'f' -> { return 6; }
            case 'g' -> { return 7; }
            case 'h' -> { return 8; }
            default -> { return 0; }
        }
    }
}
