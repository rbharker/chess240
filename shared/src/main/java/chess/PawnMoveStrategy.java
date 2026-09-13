package chess;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMoveStrategy implements MoveStrategy {
    @Override
    public List<ChessMove> getValidMoves(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor color) {
        List<ChessMove> validMoves = new ArrayList<>();

        // WHITE PAWN MOVES
        if (color == ChessGame.TeamColor.WHITE) {

            // check if there is someone diagonally forward
            // you can capture if so
            // check right
            if ((myPosition.getColumn() < BOARD_WIDTH) && (myPosition.getRow() < BOARD_HEIGHT)) {
                Collection<ChessMove> tempMove = movePawnCapture(board, myPosition, color, 1, 1);
                if (tempMove != null) {
                    validMoves.addAll(tempMove);
                }
            }

            // check left
            if ((myPosition.getColumn() > 1) && (myPosition.getRow() < BOARD_HEIGHT)) {
                Collection<ChessMove> tempMove = movePawnCapture(board, myPosition, color, -1, 1);
                if (tempMove != null) {
                    validMoves.addAll(tempMove);
                }
            }

            // if you're in position zero you can move forward one or two
            if (myPosition.getRow() == 2) {
                // move two forward
                Collection<ChessMove> tempMove = movePawnForward(board, myPosition, color, 2);
                if (tempMove != null) {
                    validMoves.addAll(tempMove);
                }
            }

            // if you're in any other position
            if ((myPosition.getRow() < BOARD_HEIGHT)) {
                Collection<ChessMove> tempMove = movePawnForward(board, myPosition, color, 1);
                if (tempMove != null) {
                    validMoves.addAll(tempMove);
                }
            }
        }

        // BLACK PAWN MOVES
        if (color == ChessGame.TeamColor.BLACK) {

            // check if there is someone diagonally forward
            // you can capture if so
            // check right
            if ((myPosition.getColumn() < BOARD_WIDTH) && (myPosition.getRow() > 1)) {
                Collection<ChessMove> tempMove = movePawnCapture(board, myPosition, color, 1, -1);
                if (tempMove != null) {
                    validMoves.addAll(tempMove);
                }
            }

            // check left
            if ((myPosition.getColumn() > 1) && (myPosition.getRow() > 1)) {
                Collection<ChessMove> tempMove = movePawnCapture(board, myPosition, color, -1, -1);
                if (tempMove != null) {
                    validMoves.addAll(tempMove);
                }
            }

            // if you're in position zero you can move forward one or two
            if (myPosition.getRow() == 7) {
                // move two forward
                Collection<ChessMove> tempMove = movePawnForward(board, myPosition, color,-2);
                if (tempMove != null) {
                    validMoves.addAll(tempMove);
                }
            }

            // if you're in any other position
            if ((myPosition.getRow() > 1)) {
                Collection<ChessMove> tempMove = movePawnForward(board, myPosition, color,-1);
                if (tempMove != null) {
                    validMoves.addAll(tempMove);
                }
            }
        }

        return validMoves;
    }

    private Collection<ChessMove> movePawnForward(ChessBoard board, ChessPosition myPosition,
                                                  ChessGame.TeamColor color, int spaces) {
        int tempCol = myPosition.getColumn();
        int tempRow = myPosition.getRow() + spaces;
        ChessPosition tempPos = new ChessPosition(tempRow, tempCol);
        ChessMove validMove = new ChessMove(myPosition, tempPos, null);
        Collection<ChessMove> validMoves = new ArrayList<>();
        switch(spaces) {
            case 2, -2:
                int prevRow = myPosition.getRow() + (spaces/2);
                ChessPosition previousPosition = new ChessPosition(prevRow, tempCol);
                // if the space is occupied or the space before is occupied
                if ((board.getPiece(tempPos) != null) || (board.getPiece(previousPosition) != null)) {
                    return null;
                }
                else {
                    validMoves.add(validMove);
                    return validMoves;
                }
            default:
                // if the space is occupied
                if (board.getPiece(tempPos) != null) {
                    return null;
                }
                else {
                    if ((tempRow == 8 && color == ChessGame.TeamColor.WHITE) ||
                            (tempRow == 1 && color == ChessGame.TeamColor.BLACK)) {
                        ChessMove validMove1 = new ChessMove(myPosition, tempPos, ChessPiece.PieceType.QUEEN);
                        ChessMove validMove2 = new ChessMove(myPosition, tempPos, ChessPiece.PieceType.KNIGHT);
                        ChessMove validMove3 = new ChessMove(myPosition, tempPos, ChessPiece.PieceType.BISHOP);
                        ChessMove validMove4 = new ChessMove(myPosition, tempPos, ChessPiece.PieceType.ROOK);
                        validMoves.add(validMove1);
                        validMoves.add(validMove2);
                        validMoves.add(validMove3);
                        validMoves.add(validMove4);
                        return validMoves;
                    }
                    else {
                        validMoves.add(validMove);
                        return validMoves;
                    }
                }

        }
    }

    private Collection<ChessMove> movePawnCapture(ChessBoard board, ChessPosition myPosition,
                                      ChessGame.TeamColor color, int x, int y) {
        int tempCol = myPosition.getColumn() + x;
        int tempRow = myPosition.getRow() + y;
        ChessPosition tempPos = new ChessPosition(tempRow, tempCol);
        ChessMove validMove = new ChessMove(myPosition, tempPos, null);
        Collection<ChessMove> validMoves = new ArrayList<>();
        // if the space is occupied
        if (board.getPiece(tempPos) != null) {
            // if the piece is your color you can't capture
            if (board.getPiece(tempPos).pieceColor == color) {
                return null;
            }
            // if the piece is the other color you can capture
            else {
                // if you're capturing on the end of the board you can be promoted
                if ((tempRow == 8 && color == ChessGame.TeamColor.WHITE) ||
                        (tempRow == 1 && color == ChessGame.TeamColor.BLACK)) {
                    ChessMove validMove1 = new ChessMove(myPosition, tempPos, ChessPiece.PieceType.QUEEN);
                    ChessMove validMove2 = new ChessMove(myPosition, tempPos, ChessPiece.PieceType.KNIGHT);
                    ChessMove validMove3 = new ChessMove(myPosition, tempPos, ChessPiece.PieceType.BISHOP);
                    ChessMove validMove4 = new ChessMove(myPosition, tempPos, ChessPiece.PieceType.ROOK);
                    validMoves.add(validMove1);
                    validMoves.add(validMove2);
                    validMoves.add(validMove3);
                    validMoves.add(validMove4);
                    return validMoves;
                }
                else {
                    validMoves.add(validMove);
                    return validMoves;
                }
            }
        }
        // otherwise the space is free and you cannot capture
        else {
            return null;
        }
    }
}
