package chess;

import java.util.List;

public class KingMoveCalc extends MoveCalc {

    public KingMoveCalc(ChessBoard board, ChessPosition myPosition, List<ChessMove> legalMoves) {
        super(board, myPosition, legalMoves);
    }

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
