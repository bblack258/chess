package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */


public class ChessMove {

    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */

    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */

    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */

    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    public String readable() {
        String first = convert(startPosition.getColumn()) + startPosition.getRow();
        String second = convert(endPosition.getColumn()) + endPosition.getRow();
        return first + " to " + second;
    }

    public String convert(int num) {
        switch (num) {
            case 1 -> { return "a"; }
            case 2 -> { return "b"; }
            case 3 -> { return "c"; }
            case 4 -> { return "d"; }
            case 5 -> { return "e"; }
            case 6 -> { return "f"; }
            case 7 -> { return "g"; }
            case 8 -> { return "h"; }
            default -> { return null; }
        }
    }

    @Override
    public String toString() {
        return String.format("%s%s",startPosition,endPosition);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition,
                chessMove.endPosition) && promotionPiece == chessMove.promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }
}
