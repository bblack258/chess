package ui;

import chess.ChessBoard;
import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class GenerateBoard {

    private static final int BOARD_SIDE_LENGTH_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int BORDER_SIZE_IN_SQUARES = 1;

    private static PrintStream out;

    public GenerateBoard() {
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        GenerateBoard board = new GenerateBoard();

        board.printBoard(new ChessBoard(), "White");
    }

    public void printBoard(ChessBoard board, String color) {

        ChessGame.TeamColor teamColor = colorToTeam(color);

        for (int row = 0; row < (2 * BORDER_SIZE_IN_SQUARES + BOARD_SIDE_LENGTH_IN_SQUARES ); row++) {
            if (row == 0) {
                printHeader(teamColor);
            } else if (row == 2 * BORDER_SIZE_IN_SQUARES + BOARD_SIDE_LENGTH_IN_SQUARES - 1) {
                printHeader(teamColor);
            } else {
                printLine(row, board);
                setBlack();
            }

            setBlack();
            out.println();
        }
    }

    private void printHeader(ChessGame.TeamColor color) {
        for (int col = 0; col < 2 * BORDER_SIZE_IN_SQUARES + BOARD_SIDE_LENGTH_IN_SQUARES; col++) {
            setHeader();
            out.print(EMPTY);
        }
        // Add functionality for flippling the board - maybe do it by changing which way we iterate through the board
    }

    private void printLine(int row, ChessBoard board) {
        for (int col = 0; col < 2 * BORDER_SIZE_IN_SQUARES + BOARD_SIDE_LENGTH_IN_SQUARES; col++) {
            if (col == 0 || col == 2 * BORDER_SIZE_IN_SQUARES + BOARD_SIDE_LENGTH_IN_SQUARES - 1) {
                setHeader();
            } else if ((row % 2 == 1 && col % 2 == 1) || (row % 2 == 0 && col % 2 == 0)) { // here I can take the 1 or 0 and tie it to color
                setWhite();
            } else {
                setBlack();
            }
            out.print(EMPTY);
        }

    }

    private void setHeader() {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private void setWhite() {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private void setBlack() {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private ChessGame.TeamColor colorToTeam(String color) {
        if (color.equalsIgnoreCase("WHITE")) {
            return ChessGame.TeamColor.WHITE;
        }
        return ChessGame.TeamColor.BLACK;
    }

}
