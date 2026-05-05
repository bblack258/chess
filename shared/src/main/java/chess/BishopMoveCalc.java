package chess;

import java.util.List;

/**
 *  Child class of MoveCalc to help calculate the legal moves a bishop can make given a certain board and position
 */

public class BishopMoveCalc extends MoveCalc {

    public BishopMoveCalc(ChessBoard board, ChessPosition myPosition, List<ChessMove> legalMoves) {
        super(board, myPosition, legalMoves);
    }
}
