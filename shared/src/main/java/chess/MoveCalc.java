package chess;

import java.util.ArrayList;

public class MoveCalc {
    // This is a class to help calculate the legal moves a chess piece can make given a certain board and position

    MoveCalc(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> legalMoves,
             ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        switch (type) {
            case KING:

                break;
            case QUEEN:

                break;
            case BISHOP:
                for (int i = 1; i <= 8; i++) {
                    if (myPosition.getRow() + i <= 8 && myPosition.getRow() - i > 0 &&
                            myPosition.getColumn() + i <= 8 && myPosition.getColumn() - i > 0){
//                        legalMoves.add(new ChessMove(myPosition, new ChessPosition()));
                    }
                }
                break;
            case KNIGHT:

                break;
            case ROOK:

                break;
            case PAWN:

                break;
            case null, default:

                break;
        }
    }
}
