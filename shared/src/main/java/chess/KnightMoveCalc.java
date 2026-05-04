package chess;

import java.util.ArrayList;
import java.util.List;

public class KnightMoveCalc extends MoveCalc{

    public KnightMoveCalc(ChessBoard board, ChessPosition myPosition, List<ChessMove> legalMoves) {
        super(board, myPosition, legalMoves);
    }

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
