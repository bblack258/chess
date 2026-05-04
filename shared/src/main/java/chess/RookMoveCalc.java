package chess;

import java.util.List;

public class RookMoveCalc extends MoveCalc{

    public RookMoveCalc(ChessBoard board, ChessPosition myPosition, List<ChessMove> legalMoves) {
        super(board, myPosition, legalMoves);
    }
}
