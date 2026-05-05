package chess;

import java.util.List;

/**
 *  Child class of MoveCalc to help calculate the legal moves a pawn can make given a certain board and position
 */

public class PawnMoveCalc {

    ChessBoard board;
    ChessPosition myPosition;
    List<ChessMove> legalMoves;

    public PawnMoveCalc(ChessBoard board, ChessPosition myPosition, List<ChessMove> legalMoves) {
        this.board = board;
        this.myPosition = myPosition;
        this.legalMoves = legalMoves;
    }

    /**
     * Public function to add the allowed moves for a pawn based on its color
     */

    public void movePawn() {
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        if (color == ChessGame.TeamColor.WHITE) {
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
     * Adds the allowed moves for a white pawn, excepting taking and promotions
     */

    private void moveWhite() {
        if (myPosition.getRow() + 1 < 8 && board.getPiece(new ChessPosition(myPosition.getRow() + 1,
                myPosition.getColumn())) == null ) {
            legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()),null));
        }
        if (myPosition.getRow() == 2 && board.getPiece(new ChessPosition(myPosition.getRow() + 1,
                myPosition.getColumn())) == null && board.getPiece(new ChessPosition(myPosition.getRow() + 2,
                myPosition.getColumn())) == null) {
            legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()),null));
        }
    }

    /**
     * Adds the allowed moves for a black pawn, excepting taking and promotions
     */

    private void moveBlack(){
        if (myPosition.getRow() - 1 > 1 && board.getPiece(new ChessPosition(myPosition.getRow() - 1,
                myPosition.getColumn())) == null ) {
            legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()),null));
        }
        if (myPosition.getRow() == 7 && board.getPiece(new ChessPosition(myPosition.getRow() - 1,
                myPosition.getColumn())) == null && board.getPiece(new ChessPosition(myPosition.getRow() - 2,
                myPosition.getColumn())) == null) {
            legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()),null));
        }
    }

    /**
     * Adds the allowed moves where a white pawn can take
     */

    private void takeWhite() {
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        ChessPiece otherL;
        if (myPosition.getColumn() - 1 > 0) {
            otherL = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1));
        } else {
            otherL = null;
        }

        if (otherL != null && myPosition.getRow() + 1 < 8 && otherL.getTeamColor() != myColor) {
            legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1),null));
        }

        ChessPiece otherR;
        if (myPosition.getColumn() + 1 <= 8) {
            otherR = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1));
        } else {
            otherR = null;
        }

        if (otherR != null && myPosition.getRow() + 1 < 8 && otherR.getTeamColor() != myColor) {
            legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1),null));
        }
    }

    /**
     * Adds the allowed moves where a black pawn can take
     */

    private void takeBlack() {
        ChessGame.TeamColor myColor = board.getPiece(myPosition).getTeamColor();
        ChessPiece otherL;
        if (myPosition.getColumn() - 1 > 0) {
            otherL = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1));
        } else {
            otherL = null;
        }

        if (otherL != null && myPosition.getRow() - 1 > 1 && otherL.getTeamColor() != myColor) {
            legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1),null));
        }

        ChessPiece otherR;
        if (myPosition.getColumn() + 1 <= 8) {
            otherR = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1));
        } else {
            otherR = null;
        }

        if (otherR != null && myPosition.getRow() - 1 > 1 && otherR.getTeamColor() != myColor) {
            legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1),null));
        }
    }

    /**
     * Adds possible promotions for a white pawn, including taking
     */

    private void promoteWhite() {
        if (myPosition.getRow() + 1 == 8) {
            ChessPiece otherC = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()));
            if (otherC == null) {
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));
            }

            ChessPiece otherL = null;
            if (myPosition.getColumn() - 1 > 0) {
                otherL = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1));
            }
            if (otherL != null && otherL.getTeamColor() == ChessGame.TeamColor.BLACK) {
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.QUEEN));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.BISHOP));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.ROOK));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), ChessPiece.PieceType.KNIGHT));
            }

            ChessPiece otherR = null;
            if (myPosition.getColumn() + 1 <= 8) {
                otherR = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1));
            }
            if (otherR != null && otherR.getTeamColor() == ChessGame.TeamColor.BLACK) {
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.QUEEN));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.BISHOP));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.ROOK));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1), ChessPiece.PieceType.KNIGHT));
            }
        }
    }

    /**
     * Adds possible promotions for a black pawn, including taking
     */

    private void promoteBlack() {
        if (myPosition.getRow() - 1 == 1) {
            ChessPiece otherC = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()));
            if (otherC == null) {
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.QUEEN));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.BISHOP));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.ROOK));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), ChessPiece.PieceType.KNIGHT));
            }

            ChessPiece otherL = null;
            if (myPosition.getColumn() - 1 > 0) {
                otherL = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1));
            }
            if (otherL != null && otherL.getTeamColor() == ChessGame.TeamColor.WHITE) {
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.QUEEN));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.BISHOP));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.ROOK));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), ChessPiece.PieceType.KNIGHT));
            }

            ChessPiece otherR = null;
            if (myPosition.getColumn() + 1 <= 8) {
                otherR = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1));
            }
            if (otherR != null && otherR.getTeamColor() == ChessGame.TeamColor.WHITE) {
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.QUEEN));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.BISHOP));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.ROOK));
                legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), ChessPiece.PieceType.KNIGHT));
            }
        }
    }
}
