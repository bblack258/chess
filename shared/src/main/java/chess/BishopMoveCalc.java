package chess;

import java.util.List;

public class BishopMoveCalc extends MoveCalc {

    public BishopMoveCalc(ChessBoard board, ChessPosition myPosition, List<ChessMove> legalMoves) {
        super(board, myPosition, legalMoves);
    }
}
