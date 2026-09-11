package chess;

import java.util.ArrayList;
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
                ChessMove tempMove = movePawnCapture(board, myPosition, color, 1, 1);
                if (tempMove != null) {
                    validMoves.add(tempMove);
                }
            }

            // check left
            if ((myPosition.getColumn() > 1) && (myPosition.getRow() < BOARD_HEIGHT)) {
                ChessMove tempMove = movePawnCapture(board, myPosition, color, -1, 1);
                if (tempMove != null) {
                    validMoves.add(tempMove);
                }
            }

            // if you're in position zero you can move forward one or two
            if (myPosition.getRow() == 2) {
                // move two forward
                ChessMove tempMove = movePawnForward(board, myPosition, 2);
                if (tempMove != null) {
                    validMoves.add(tempMove);
                }
            }

            // if you're in any other position
            if ((myPosition.getRow() < BOARD_HEIGHT)) {
                ChessMove tempMove = movePawnForward(board, myPosition, 1);
                if (tempMove != null) {
                    validMoves.add(tempMove);
                }
            }
        }

        // BLACK PAWN MOVES
        if (color == ChessGame.TeamColor.BLACK) {

            // check if there is someone diagonally forward
            // you can capture if so
            // check right
            if ((myPosition.getColumn() < BOARD_WIDTH) && (myPosition.getRow() > 1)) {
                ChessMove tempMove = movePawnCapture(board, myPosition, color, 1, -1);
                if (tempMove != null) {
                    validMoves.add(tempMove);
                }
            }

            // check left
            if ((myPosition.getColumn() > 1) && (myPosition.getRow() > 1)) {
                ChessMove tempMove = movePawnCapture(board, myPosition, color, -1, -1);
                if (tempMove != null) {
                    validMoves.add(tempMove);
                }
            }

            // if you're in position zero you can move forward one or two
            if (myPosition.getRow() == 7) {
                // move two forward
                ChessMove tempMove = movePawnForward(board, myPosition, -2);
                if (tempMove != null) {
                    validMoves.add(tempMove);
                }
            }

            // if you're in any other position
            if ((myPosition.getRow() > 1)) {
                ChessMove tempMove = movePawnForward(board, myPosition, -1);
                if (tempMove != null) {
                    validMoves.add(tempMove);
                }
            }
        }

        return validMoves;
    }

    private ChessMove movePawnForward(ChessBoard board, ChessPosition myPosition, int spaces) {
        int tempCol = myPosition.getColumn();
        int tempRow = myPosition.getRow() + spaces;
        ChessPosition tempPos = new ChessPosition(tempRow, tempCol);
        ChessMove validMove = new ChessMove(myPosition, tempPos, null);
        switch(spaces) {
            case 2, -2:
                int prevRow = myPosition.getRow() + (spaces/2);
                ChessPosition previousPosition = new ChessPosition(prevRow, tempCol);
                // if the space is occupied or the space before is occupied
                if ((board.getPiece(tempPos) != null) || (board.getPiece(previousPosition) != null)) {
                    return null;
                }
                else {
                    return validMove;
                }
            default:
                // if the space is occupied
                if (board.getPiece(tempPos) != null) {
                    return null;
                }
                else {
                    return validMove;
                }

        }
    }

    private ChessMove movePawnCapture(ChessBoard board, ChessPosition myPosition,
                                      ChessGame.TeamColor color, int x, int y) {
        int tempCol = myPosition.getColumn() + x;
        int tempRow = myPosition.getRow() + y;
        ChessPosition tempPos = new ChessPosition(tempRow, tempCol);
        ChessMove validMove = new ChessMove(myPosition, tempPos, null);
        // if the space is occupied
        if (board.getPiece(tempPos) != null) {
            // if the piece is your color you can't capture
            if (board.getPiece(tempPos).pieceColor == color) {
                return null;
            }
            // if the piece is the other color you can capture
            else {
                return validMove;
            }
        }
        // otherwise the space is free and you cannot capture
        else {
            return null;
        }
    }
}
