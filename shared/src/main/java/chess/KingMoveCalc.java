package chess;

import java.util.List;

/**
 *  Child class of MoveCalc to help calculate the legal moves a king can make given a certain board and position,
 *  not including check, checkmate, and castling
 */

public class KingMoveCalc extends MoveCalc {

    public KingMoveCalc(ChessBoard board, ChessPosition myPosition, List<ChessMove> legalMoves) {
        super(board, myPosition, legalMoves);
    }

    /**
     * Adds the allowed moves for a king, checking each neighboring square to see if it's out-of-bounds or occupied by
     * a piece from the same team
     */

    public void moveKing() {
        if (startPosition.getRow() + 1 <= 8 && notBlocked(new ChessPosition(startPosition.getRow() + 1,
                startPosition.getColumn()))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn()), null));
        }
        if (startPosition.getRow() - 1 > 0 && notBlocked(new ChessPosition(startPosition.getRow() - 1,
                startPosition.getColumn()))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn()), null));
        }
        if (startPosition.getColumn() + 1 <= 8 && notBlocked(new ChessPosition(startPosition.getRow(),
                startPosition.getColumn() + 1))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(),
                    startPosition.getColumn() + 1), null));
        }
        if (startPosition.getColumn() - 1 > 0 && notBlocked(new ChessPosition(startPosition.getRow(),
                startPosition.getColumn() - 1))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow(),
                    startPosition.getColumn() - 1), null));
        }
        if (startPosition.getRow() + 1 <= 8 && startPosition.getColumn() + 1 <= 8 && notBlocked(new ChessPosition(
                startPosition.getRow() + 1, startPosition.getColumn() + 1))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() + 1), null));
        }
        if (startPosition.getRow() + 1 <= 8 && startPosition.getColumn() - 1 > 0 && notBlocked(new ChessPosition(
                startPosition.getRow() + 1, startPosition.getColumn() - 1))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() + 1,
                    startPosition.getColumn() - 1), null));
        }
        if (startPosition.getRow() - 1 > 0 && startPosition.getColumn() + 1 <= 8 && notBlocked(new ChessPosition(
                startPosition.getRow() - 1, startPosition.getColumn() + 1))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn() + 1), null));
        }
        if (startPosition.getRow() - 1 > 0 && startPosition.getColumn() - 1 > 0 && notBlocked(new ChessPosition(
                startPosition.getRow() - 1, startPosition.getColumn() - 1))) {
            legalMoves.add(new ChessMove(startPosition, new ChessPosition(startPosition.getRow() - 1,
                    startPosition.getColumn() - 1), null));
        }
    }
}
