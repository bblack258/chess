package chess;

import java.util.List;

/**
 *  Parent class to help calculate the legal moves a chess piece can make given a certain board and position
 */

public class MoveCalc {

    ChessPiece myPiece;
    ChessBoard board;
    ChessPosition myPosition;
    List<ChessMove> legalMoves;

    public MoveCalc(ChessBoard board, ChessPosition myPosition, List<ChessMove> legalMoves) {
        myPiece = board.getPiece(myPosition);
        this.board = board;
        this.myPosition = myPosition;
        this.legalMoves = legalMoves;
    }

    /**
     * Adds the legal moves for a piece moving only in straight lines either vertically or horizontally,
     * stopping when blocked or taking
     */

    public void moveStraight() {
        for (int i = 1; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
            if (myPosition.getRow() + i <= 8 && notBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i);
            if (myPosition.getColumn() + i <= 8 && notBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());
            if (myPosition.getRow() - i > 0 && notBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i);
            if (myPosition.getColumn() - i > 0 && notBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
    }

    /**
     * Adds valid moves for a piece moving diagonally, stopping when blocked or taking
     */

    public void moveDiagonal() {
        for (int i = 1; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
            if (myPosition.getRow() + i <= 8 && myPosition.getColumn() + i <= 8 && notBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
            if (myPosition.getRow() + i <= 8 && myPosition.getColumn() - i > 0 && notBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
            if (myPosition.getRow() - i > 0 && myPosition.getColumn() + i <= 8 && notBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
            if (myPosition.getRow() - i > 0 && myPosition.getColumn() - i > 0 && notBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
    }

    /**
     * Helper function to test if a space is blocked by a piece of the same color
     *
     * @param endPosition  final position of the piece in question
     * @return             boolean describing if the square is blocked
     */

    public boolean notBlocked(ChessPosition endPosition) {
        ChessPiece other = board.getPiece(endPosition);
        return other == null || other.getTeamColor() != myPiece.getTeamColor();
    }

    /**
     * Helper function to test if a space is occupied by a piece of the opposite color that can be taken
     *
     * @param endPosition  final position of the piece in question
     * @return             boolean describing if there is a piece in the square that can be taken
     */

    public boolean canTake(ChessPosition endPosition) {
        ChessPiece other = board.getPiece(endPosition);
        return other != null && other.getTeamColor() != myPiece.getTeamColor();
    }
}
