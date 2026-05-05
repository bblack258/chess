package chess;

import java.util.List;

/**
 *  Child class of MoveCalc to help calculate the legal moves a king can make given a certain board and position,
 *  not including check, checkmate, and castling
 */

public class KingMoveCalc extends MoveCalc {

    public KingMoveCalc(ChessBoard board, ChessPosition myPosition, List<ChessMove> legalMoves) {
        super(board, myPosition, legalMoves);
    }

    /**
     * Adds the allowed moves for a king, checking each neighboring square to see if it's out-of-bounds or occupied by
     * a piece from the same team
     */

    public void moveKing() {
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        ChessPiece other;
        if (myPosition.getRow() - 1 > 0) {
            other = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()));
            if (other == null || other.getTeamColor() != myColor) {
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
            }
        }
        if (myPosition.getRow() + 1 <= 8) {
            other = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()));
            if (other == null || other.getTeamColor() != myColor) {
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));
            }
        }
        if (myPosition.getColumn() - 1 > 0) {
            other = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1));
            if (other == null || other.getTeamColor() != myColor) {
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1), null));
            }
        }
        if (myPosition.getColumn() + 1 <= 8) {
            other = board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1));
            if (other == null || other.getTeamColor() != myColor) {
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1), null));
            }
        }
        if (myPosition.getRow() - 1 > 0 && myPosition.getColumn() - 1 > 0) {
            other = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1));
            if (other == null || other.getTeamColor() != myColor) {
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
            }
        }
        if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() - 1 > 0) {
            other = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1));
            if (other == null || other.getTeamColor() != myColor) {
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
            }
        }
        if (myPosition.getRow() - 1 > 0 && myPosition.getColumn() + 1 <= 8) {
            other = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1));
            if (other == null || other.getTeamColor() != myColor) {
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));
            }
        }
        if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() + 1 <= 8) {
            other = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1));
            if (other == null || other.getTeamColor() != myColor) {
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), null));
            }
        }
    }
}
