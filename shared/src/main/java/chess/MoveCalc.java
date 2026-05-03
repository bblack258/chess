package chess;

import java.util.List;

public class MoveCalc {
    // This is a class to help calculate the legal moves a chess piece can make given a certain board and position

    public MoveCalc(ChessBoard board, ChessPosition myPosition, List<ChessMove> legalMoves,
             ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        switch (type) {
            case KING:

                break;
            case QUEEN:

                break;
            case BISHOP:
                for (int i = 1; i <= 8; i++) {
                    if (myPosition.getRow() + i <= 8){
                        bishopLoop1:
                        if (myPosition.getColumn() + i <= 8){
                            ChessPiece other = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i));
                            if (other != null && other.getPieceType() == type) {
                                break bishopLoop1;
                            }
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i,
                                    myPosition.getColumn() + i), null));
                            if (other != null && other.getPieceType() != type) {
                                break bishopLoop1; // What we need to do here is break out of the for loop not the if statement. Maybe do multiple for loops?
                            }
                        }
                        bishopLoop2:
                        if (myPosition.getColumn() - i > 0){
                            ChessPiece other = board.getPiece(new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i));
                            if (other != null && other.getPieceType() == type) {
                                break bishopLoop2;
                            }
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + i,
                                    myPosition.getColumn() - i), null));
                            if (other != null && other.getPieceType() != type) {
                                break bishopLoop2;
                            }
                        }
                    }
                    if (myPosition.getRow() - i > 0){
                        bishopLoop3:
                        if (myPosition.getColumn() + i <= 8){
                            ChessPiece other = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i));
                            if (other != null && other.getPieceType() == type) {
                                break bishopLoop3;
                            }
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i,
                                    myPosition.getColumn() + i), null));
                            if (other != null && other.getPieceType() != type) {
                                break bishopLoop3;
                            }
                        }
                        bishopLoop4:
                        if (myPosition.getColumn() - i > 0){
                            ChessPiece other = board.getPiece(new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i));
                                if (other != null && other.getPieceType() == type) {
                                break bishopLoop4;
                            }
                            legalMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - i,
                                    myPosition.getColumn() - i), null));
                            if (other != null && other.getPieceType() != type) {
                                break bishopLoop4;
                            }
                        }
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
