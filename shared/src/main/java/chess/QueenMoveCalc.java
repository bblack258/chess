package chess;

import java.util.List;

/**
 *  Child class of MoveCalc to help calculate the legal moves a queen can make given a certain board and position
 */

public class QueenMoveCalc extends MoveCalc {

    public QueenMoveCalc(ChessBoard board, ChessPosition myPosition, List<ChessMove> legalMoves) {
        super(board, myPosition, legalMoves);
    }

    /**
     * Add the allowed moves for a queen both in straight and diagonal paths
     */

    public void queenMove() {
        new MoveCalc(board, myPosition, legalMoves).moveStraight();
        new MoveCalc(board, myPosition, legalMoves).moveDiagonal();
    }
}
