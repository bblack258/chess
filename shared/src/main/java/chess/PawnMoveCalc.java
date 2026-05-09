package chess;

import java.util.List;

/**
 * Child class to extend MoveCalc for Pawns
 */

public class PawnMoveCalc extends MoveCalc {

    public PawnMoveCalc(ChessBoard board, ChessPosition startPosition, List<ChessMove> legalMoves) {
        super(board, startPosition, legalMoves);
    }

    /**
     * Wrapper function for the moves a pawn can make depending on its color
     */
    public void movePawn() {
        if (myColor == ChessGame.TeamColor.WHITE) {
            moveWhite();
            takeWhite();
            promoteWhite();
        } else {
            moveBlack();
            takeBlack();
            promoteBlack();
        }
    }

    /**
     * adds legal moves for a pawn moving in a straight line without considering taking or promotion
     */
    public void moveWhite() {
        if (startPosition.getRow() == 2 && notBlocked(new ChessPosition(startPosition.getRow() + 1,
                startPosition.getColumn())) && notBlocked(new ChessPosition(startPosition.getRow() + 2,
                startPosition.getColumn()))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 2,
                    startPosition.getColumn()), null));
        }
        if (startPosition.getRow() + 1 < 8 && notBlocked(new ChessPosition(startPosition.getRow() + 1,
                startPosition.getColumn()))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn()), null));
        }
    }

    public void moveBlack() {
        if (startPosition.getRow() == 7 && notBlocked(new ChessPosition(startPosition.getRow() - 1,
                startPosition.getColumn())) && notBlocked(new ChessPosition(startPosition.getRow() - 2,
                startPosition.getColumn()))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 2,
                    startPosition.getColumn()), null));
        }
        if (startPosition.getRow() - 1 > 1 && notBlocked(new ChessPosition(startPosition.getRow() - 1,
                startPosition.getColumn()))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn()), null));
        }
    }

    /**
     * adds legal moves for a pawn when taking, except when it would result in promotion
     */
    public void takeWhite() {
        ChessPiece otherL = null;
        if (startPosition.getRow() + 1 < 8 && startPosition.getColumn() - 1 > 0) {
            otherL = board.getPiece(new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() - 1));
        }
        if (otherL != null && otherL.getTeamColor() != myColor) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() - 1), null));
        }

        ChessPiece otherR = null;
        if (startPosition.getRow() + 1 < 8 && startPosition.getColumn() + 1 <= 8) {
            otherR = board.getPiece(new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1));
        }
        if (otherR != null && otherR.getTeamColor() != myColor) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() + 1), null));
        }
    }

    public void takeBlack() {
        ChessPiece otherL = null;
        if (startPosition.getRow() - 1 > 1 && startPosition.getColumn() - 1 > 0) {
            otherL = board.getPiece(new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() - 1));
        }
        if (otherL != null && otherL.getTeamColor() != myColor) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn() - 1), null));
        }

        ChessPiece otherR = null;
        if (startPosition.getRow() - 1 > 1 && startPosition.getColumn() + 1 <= 8) {
            otherR = board.getPiece(new ChessPosition(startPosition.getRow() - 1, startPosition.getColumn() + 1));
        }
        if (otherR != null && otherR.getTeamColor() != myColor) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn() + 1), null));
        }
    }

    /**
     * adds legal moves for a pawn that would result in promotion
     */
    public void promoteWhite() {
        if (startPosition.getRow() + 1 == 8 && notBlocked(new ChessPosition(startPosition.getRow() + 1,
                startPosition.getColumn()))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn()), ChessPiece.PieceType.QUEEN));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn()), ChessPiece.PieceType.KNIGHT));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn()), ChessPiece.PieceType.BISHOP));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn()), ChessPiece.PieceType.ROOK));
        }
        if (startPosition.getRow() + 1 == 8 && startPosition.getColumn() - 1 > 0 && canTake(new ChessPosition(
                startPosition.getRow() + 1, startPosition.getColumn() - 1))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() - 1), ChessPiece.PieceType.QUEEN));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() - 1), ChessPiece.PieceType.KNIGHT));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() - 1), ChessPiece.PieceType.BISHOP));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() - 1), ChessPiece.PieceType.ROOK));
        }
        if (startPosition.getRow() + 1 == 8 && startPosition.getColumn() + 1 <= 8 && canTake(new ChessPosition(
                startPosition.getRow() + 1, startPosition.getColumn() + 1))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() + 1), ChessPiece.PieceType.QUEEN));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() + 1), ChessPiece.PieceType.KNIGHT));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() + 1), ChessPiece.PieceType.BISHOP));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() + 1), ChessPiece.PieceType.ROOK));
        }
    }

    public void promoteBlack() {
        if (startPosition.getRow() - 1 == 1 && notBlocked(new ChessPosition(startPosition.getRow() - 1,
                startPosition.getColumn()))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn()), ChessPiece.PieceType.QUEEN));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn()), ChessPiece.PieceType.KNIGHT));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn()), ChessPiece.PieceType.BISHOP));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn()), ChessPiece.PieceType.ROOK));
        }
        if (startPosition.getRow() - 1 == 1 && startPosition.getColumn() - 1 > 0 && canTake(new ChessPosition(
                startPosition.getRow() - 1, startPosition.getColumn() - 1))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn() - 1), ChessPiece.PieceType.QUEEN));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn() - 1), ChessPiece.PieceType.KNIGHT));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn() - 1), ChessPiece.PieceType.BISHOP));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn() - 1), ChessPiece.PieceType.ROOK));
        }
        if (startPosition.getRow() - 1 == 1 && startPosition.getColumn() + 1 <= 8 && canTake(new ChessPosition(
                startPosition.getRow() - 1, startPosition.getColumn() + 1))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn() + 1), ChessPiece.PieceType.QUEEN));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn() + 1), ChessPiece.PieceType.KNIGHT));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn() + 1), ChessPiece.PieceType.BISHOP));
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn() + 1), ChessPiece.PieceType.ROOK));
        }
    }

    @Override
    public boolean notBlocked(ChessPosition endPosition) {
        return board.getPiece(endPosition) == null;
    }
}
