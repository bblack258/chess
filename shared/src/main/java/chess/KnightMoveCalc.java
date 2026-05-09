package chess;

import java.util.List;

/**
 * Child class of MoveCalc to extend MoveCalc for Knights
 */

public class KnightMoveCalc extends MoveCalc {

    public KnightMoveCalc(ChessBoard board, ChessPosition startPosition, List<ChessMove> legalMoves) {
        super(board, startPosition, legalMoves);
    }

    public void moveKnight() {
        if (startPosition.getRow() + 2 <= 8 && startPosition.getColumn() + 1 <= 8 && notBlocked(new ChessPosition(
                startPosition.getRow() + 2, startPosition.getColumn() + 1))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 2,
                    startPosition.getColumn() + 1), null));
        }
        if (startPosition.getRow() + 2 <= 8 && startPosition.getColumn() - 1 > 0 && notBlocked(new ChessPosition(
                startPosition.getRow() + 2, startPosition.getColumn() - 1))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 2,
                    startPosition.getColumn() - 1), null));
        }
        if (startPosition.getRow() - 2 > 0 && startPosition.getColumn() + 1 <= 8 && notBlocked(new ChessPosition(
                startPosition.getRow() - 2, startPosition.getColumn() + 1))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 2,
                    startPosition.getColumn() + 1), null));
        }
        if (startPosition.getRow() - 2 > 0 && startPosition.getColumn() - 1 > 0 && notBlocked(new ChessPosition(
                startPosition.getRow() - 2, startPosition.getColumn() - 1))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 2,
                    startPosition.getColumn() - 1), null));
        }
        if (startPosition.getRow() + 1 <= 8 && startPosition.getColumn() + 2 <= 8 && notBlocked(new ChessPosition(
                startPosition.getRow() + 1, startPosition.getColumn() + 2))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() + 2), null));
        }
        if (startPosition.getRow() + 1 <= 8 && startPosition.getColumn() - 2 > 0 && notBlocked(new ChessPosition(
                startPosition.getRow() + 1, startPosition.getColumn() - 2))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() - 2), null));
        }
        if (startPosition.getRow() - 1 > 0 && startPosition.getColumn() + 2 <= 8 && notBlocked(new ChessPosition(
                startPosition.getRow() - 1, startPosition.getColumn() + 2))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn() + 2), null));
        }
        if (startPosition.getRow() - 1 > 0 && startPosition.getColumn() - 2 > 0 && notBlocked(new ChessPosition(
                startPosition.getRow() - 1, startPosition.getColumn() - 2))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn() - 2), null));
        }
    }
}

