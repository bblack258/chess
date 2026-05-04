package chess;

import java.util.List;

public class MoveCalc {

    /**
     *  This is a parent class to help calculate the legal moves a chess piece can make given a certain board and position
     */

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

    public void moveStraight() {
        for (int i = 0; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
            if (myPosition.getRow() + i <= 8 && !isBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i);
            if (myPosition.getColumn() + i <= 8 && !isBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());
            if (myPosition.getRow() - i > 0 && !isBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i);
            if (myPosition.getColumn() - i > 0 && !isBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
    }

    public void moveDiagonal() {
        for (int i = 0; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
            if (myPosition.getRow() + i <= 8 && myPosition.getColumn() + i <= 8 && !isBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
            if (myPosition.getRow() + i <= 8 && myPosition.getColumn() - i > 0 && !isBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
            if (myPosition.getRow() - i > 0 && myPosition.getColumn() + i <= 8 && !isBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
            if (myPosition.getRow() - i > 0 && myPosition.getColumn() - i > 0 && !isBlocked(endPosition)) {
                legalMoves.add(new ChessMove(myPosition,endPosition,null));
                if (canTake(endPosition)){
                    break;
                }
            } else {
                break;
            }
        }
    }

    public boolean isBlocked(ChessPosition endPosition) {
        ChessPiece other = board.getPiece(endPosition);
        return other != null && other.getTeamColor() == myPiece.getTeamColor();
    }

    public boolean canTake(ChessPosition endPosition) {
        ChessPiece other = board.getPiece(endPosition);
        return other != null && other.getTeamColor() != myPiece.getTeamColor();
    }
}
