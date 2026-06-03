package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class GenerateBoard {

    private static final int BOARD_SIDE_LENGTH_IN_SQUARES = 8;
    private static final int BORDER_SIZE_IN_SQUARES = 1;
    private static final int TOTAL_NUM_SQUARES = 2 * BORDER_SIZE_IN_SQUARES + BOARD_SIDE_LENGTH_IN_SQUARES;

    private static ByteArrayOutputStream outputStream;
    private static PrintStream out;

    public GenerateBoard() {
        outputStream = new ByteArrayOutputStream();
        out = new PrintStream(outputStream, true, StandardCharsets.UTF_8);
    }

    public String printBoard(ChessBoard board, String color) {

        ChessGame.TeamColor teamColor = colorToTeam(color);

        for (int row = 0; row < TOTAL_NUM_SQUARES; row++) {
            if (row == 0) {
                printHeader(teamColor);
            } else if (row == TOTAL_NUM_SQUARES - 1) {
                printHeader(teamColor);
            } else {
                printLine(row, board, teamColor);
                setBlack();
            }

            setBKG();
            out.println();
        }
        return outputStream.toString();
    }

    private void printHeader(ChessGame.TeamColor color) {
        setHeader();
        for (int col = 0; col < TOTAL_NUM_SQUARES; col++) {
            if (col == 0 || col == TOTAL_NUM_SQUARES - 1) {
                out.print(EMPTY);
            } else if (color == ChessGame.TeamColor.WHITE) {
                out.print("\u2005" + "\u2005" + "\u2009" + (char) ('a' + col - 1) + " " + "\u200A");
            } else {
                out.print("\u2005" + "\u2005" + "\u2009" + (char) ('h' - col + 1) + " " + "\u200A");
            }
        }
    }

    private void printSide(int row, ChessGame.TeamColor color) {
        setHeader();
        switch (color) {
            case WHITE -> out.print(" " + "\u2009" + (9 - row) + "\u200A" + " ");
            case BLACK -> out.print(" " + "\u2009" + row + "\u200A" + " ");
            case null, default -> out.print(EMPTY);
        }
    }

    private void printLine(int row, ChessBoard board, ChessGame.TeamColor color) {
        for (int col = 0; col < TOTAL_NUM_SQUARES; col++) {
            if (col == 0 ) {
                printSide(row, color);
            } else if (col == TOTAL_NUM_SQUARES - 1) {
                printSide(row, color);
            } else if ((row % 2 == 1 && col % 2 == 1) || (row % 2 == 0 && col % 2 == 0)) {
                setWhite();
                printPiece(board, row, col, color);
            } else {
                setGrey();
                printPiece(board, row, col, color);
            }
        }
    }

    private void printPiece(ChessBoard board, int row, int col, ChessGame.TeamColor color) {
        ChessPiece piece;
        if (color == ChessGame.TeamColor.WHITE) {
            piece = board.getPiece(new ChessPosition(9 - row, col));
        } else {
            piece = board.getPiece(new ChessPosition(row, col));
        }
        if (piece == null) {
            out.print(EMPTY);
            return;
        }
        switch (piece.getTeamColor()) {
            case WHITE:
                switch (piece.getPieceType()) {
                    case KING -> out.print(WHITE_KING);
                    case QUEEN -> out.print(WHITE_QUEEN);
                    case ROOK -> out.print(WHITE_ROOK);
                    case BISHOP -> out.print(WHITE_BISHOP);
                    case KNIGHT -> out.print(WHITE_KNIGHT);
                    case PAWN -> out.print(WHITE_PAWN);
                    case null, default -> out.print(EMPTY);
                }
                break;
            case BLACK:
                switch (piece.getPieceType()) {
                    case KING -> out.print(BLACK_KING);
                    case QUEEN -> out.print(BLACK_QUEEN);
                    case ROOK -> out.print(BLACK_ROOK);
                    case BISHOP -> out.print(BLACK_BISHOP);
                    case KNIGHT -> out.print(BLACK_KNIGHT);
                    case PAWN -> out.print(BLACK_PAWN);
                    case null, default -> out.print(EMPTY);
                }
                break;
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

    private void setGrey() {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private void setBKG() {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private ChessGame.TeamColor colorToTeam(String color) {
        if (color.equalsIgnoreCase("WHITE")) {
            return ChessGame.TeamColor.WHITE;
        }
        return ChessGame.TeamColor.BLACK;
    }

}
