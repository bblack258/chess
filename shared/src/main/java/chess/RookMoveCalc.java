package chess;

import java.util.List;

/**
 *  Child class of MoveCalc to help calculate the legal moves a rook can make given a certain board and position
 */

public class RookMoveCalc extends MoveCalc{

    public RookMoveCalc(ChessBoard board, ChessPosition myPosition, List<ChessMove> legalMoves) {
        super(board, myPosition, legalMoves);
    }
}
