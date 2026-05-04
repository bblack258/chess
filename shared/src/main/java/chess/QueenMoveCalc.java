package chess;

import java.util.List;

public class QueenMoveCalc extends MoveCalc {

    public QueenMoveCalc(ChessBoard board, ChessPosition myPosition, List<ChessMove> legalMoves) {
        super(board, myPosition, legalMoves);
    }

    public void queenMove() {
        new MoveCalc(board, myPosition, legalMoves).moveStraight();
        new MoveCalc(board, myPosition, legalMoves).moveDiagonal();
    }
}
