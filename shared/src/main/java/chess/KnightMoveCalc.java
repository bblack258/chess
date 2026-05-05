package chess;

import java.util.ArrayList;
import java.util.List;

/**
 *  Child class of MoveCalc to help calculate the legal moves a knight can make given a certain board and position
 */

public class KnightMoveCalc extends MoveCalc{

    public KnightMoveCalc(ChessBoard board, ChessPosition myPosition, List<ChessMove> legalMoves) {
        super(board, myPosition, legalMoves);
    }

    /**
     * Adds the possible moves for a knight, checking if each possible square is out-of-bounds or blocked
     */

    public void moveKnight() {
        List<ChessPosition> moves = new ArrayList<>();
        if (myPosition.getRow() + 2 <= 8 && myPosition.getColumn() - 1 > 0) {
            moves.add(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1));
        }
        if (myPosition.getRow() + 2 <= 8 && myPosition.getColumn() + 1 <= 8) {
            moves.add(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1));
        }
        if (myPosition.getRow() - 2 > 0 && myPosition.getColumn() - 1 > 0) {
            moves.add(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1));
        }
        if (myPosition.getRow() - 2 > 0 && myPosition.getColumn() + 1 <= 8) {
            moves.add(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1));
        }
        if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() + 2 <= 8) {
            moves.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2));
        }
        if (myPosition.getRow() - 1 > 0 && myPosition.getColumn() + 2 <= 8) {
            moves.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2));
        }
        if (myPosition.getRow() + 1 <= 8 && myPosition.getColumn() - 2 > 0) {
            moves.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2));
        }
        if (myPosition.getRow() - 1 > 0 && myPosition.getColumn() - 2 > 0) {
            moves.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2));
        }

        for (ChessPosition move : moves) {
            if (notBlocked(move)){
                legalMoves.add(new ChessMove(myPosition, move, null));
            }
        }
    }
}
