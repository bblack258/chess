package chess;

import java.util.List;

/**
 * Parent class to define basic functions of movement for multiple pieces
 */

public class MoveCalc {

    ChessBoard board;
    ChessPosition startPosition;
    List<ChessMove> legalMoves;
    ChessPiece myPiece;
    ChessGame.TeamColor myColor;

    public MoveCalc(ChessBoard board, ChessPosition startPosition, List<ChessMove> legalMoves) {
        this.board = board;
        this.startPosition = startPosition;
        this.legalMoves = legalMoves;
        myPiece = board.getPiece(startPosition);
        myColor = myPiece.getTeamColor();
    }

    /**
     * Method to return legal moves in straight lines
     */
    public void moveStraight() {
        for (int i = 1; i < 8; i++) {
            if (startPosition.getRow() + i <= 8 && notBlocked(new ChessPosition(startPosition.getRow() + i,
                    startPosition.getColumn()))) {
                legalMoves.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow() + i,
                        startPosition.getColumn()),null));
            } else {
                break;
            }
            if (canTake(new ChessPosition(startPosition.getRow() + i, startPosition.getColumn()))) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (startPosition.getRow() - i >0 && notBlocked(new ChessPosition(startPosition.getRow() - i,
                    startPosition.getColumn()))) {
                legalMoves.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow() - i,
                        startPosition.getColumn()),null));
            } else {
                break;
            }
            if (canTake(new ChessPosition(startPosition.getRow() - i, startPosition.getColumn()))) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (startPosition.getColumn() + i <= 8 && notBlocked(new ChessPosition(startPosition.getRow(),
                    startPosition.getColumn() + i))) {
                legalMoves.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow(),
                        startPosition.getColumn() + i),null));
            } else {
                break;
            }
            if (canTake(new ChessPosition(startPosition.getRow(), startPosition.getColumn() + i))) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (startPosition.getColumn() - i > 0 && notBlocked(new ChessPosition(startPosition.getRow(),
                    startPosition.getColumn() - i))) {
                legalMoves.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow(),
                        startPosition.getColumn() - i),null));
            } else {
                break;
            }
            if (canTake(new ChessPosition(startPosition.getRow(), startPosition.getColumn() - i))) {
                break;
            }
        }
    }

    /**
     * Method to return legal moves in diagonal lines
     */
    public void moveDiagonal() {
        for (int i = 1; i < 8; i++) {
            if (startPosition.getRow() + i <= 8 && startPosition.getColumn() + i <= 8 && notBlocked(new ChessPosition(
                    startPosition.getRow() + i, startPosition.getColumn() + i))) {
                legalMoves.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow() + i,
                        startPosition.getColumn() + i),null));
            } else {
                break;
            }
            if (canTake(new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() + i))) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (startPosition.getRow() + i <= 8 && startPosition.getColumn() - i > 0 && notBlocked(new ChessPosition(
                    startPosition.getRow() + i, startPosition.getColumn() - i))) {
                legalMoves.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow() + i,
                        startPosition.getColumn() - i),null));
            } else {
                break;
            }
            if (canTake(new ChessPosition(startPosition.getRow() + i, startPosition.getColumn() - i))) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (startPosition.getRow() - i > 0 && startPosition.getColumn() + i <= 8 && notBlocked(new ChessPosition(
                    startPosition.getRow() - i, startPosition.getColumn() + i))) {
                legalMoves.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow() - i,
                        startPosition.getColumn() + i),null));
            } else {
                break;
            }
            if (canTake(new ChessPosition(startPosition.getRow() - i, startPosition.getColumn() + i))) {
                break;
            }
        }
        for (int i = 1; i < 8; i++) {
            if (startPosition.getRow() - i > 0 && startPosition.getColumn() - i > 0 && notBlocked(new ChessPosition(
                    startPosition.getRow() - i, startPosition.getColumn() - i))) {
                legalMoves.add(new ChessMove(startPosition,new ChessPosition(startPosition.getRow() - i,
                        startPosition.getColumn() - i),null));
            } else {
                break;
            }
            if (canTake(new ChessPosition(startPosition.getRow() - i, startPosition.getColumn() - i))) {
                break;
            }
        }
    }

    /**
     * method to check that a square is not blocked
     *
     * @param endPosition  the final position of the piece
     * @return             returns whether the piece is blocked or not
     */
    public boolean notBlocked(ChessPosition endPosition) {
        ChessPiece other = board.getPiece(endPosition);
        return other == null || other.getTeamColor() != myColor;
    }

    /**
     * method to see if a piece on a given square can be taken
     *
     * @param endPosition  the final position of the piece
     * @return             returns whether there is a piece that can be taken or not
     */
    public boolean canTake(ChessPosition endPosition) {
        ChessPiece other = board.getPiece(endPosition);
        return other != null && other.getTeamColor() != myColor;
    }
}
