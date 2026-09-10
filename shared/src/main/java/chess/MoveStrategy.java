package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Logic is encapsulated in specific strategy objects
interface MoveStrategy {
    int BOARD_HEIGHT = 8;
    int BOARD_WIDTH = 8;
    List<ChessMove> getValidMoves(ChessPosition position, ChessBoard board, ChessGame.TeamColor color);

    default ChessMove move_one(ChessBoard board, ChessPosition myPosition,
                                              ChessGame.TeamColor color, int x, int y) {
        int tempCol = myPosition.getColumn() + x;
        int tempRow = myPosition.getRow() + y;
        ChessPosition tempPos = new ChessPosition(tempRow, tempCol);
        ChessMove validMove = new ChessMove(myPosition, tempPos, null);
        // if the space is occupied
        if (board.getPiece(tempPos) != null) {
            // if the piece is your color you can't move there
            if (board.getPiece(tempPos).pieceColor == color) {
                return null;
            }
            // if the piece is the other color you can move there
            else {
                return validMove;
            }
        }
        // otherwise the space is free and you can move
        else {
            return validMove;
        }
    }

    default Collection<ChessMove> move_multiple(ChessBoard board, ChessPosition myPosition,
                                                ChessGame.TeamColor color,int x, int y) {
        int tempCol = myPosition.getColumn() + x;
        int tempRow = myPosition.getRow() + y;
        Collection<ChessMove> validMoves = new ArrayList<>();
        // while you're still on the board
        while ((tempCol <= BOARD_WIDTH) && (tempRow <= BOARD_HEIGHT)
                && (tempCol > 0) && (tempRow > 0)) {
            ChessPosition tempPos = new ChessPosition(tempRow, tempCol);
            if (board.getPiece(tempPos) != null) {
                // if the piece is your color you can't move there
                // the farthest you can go this way is the previous iteration
                if (board.getPiece(tempPos).pieceColor == color) {
                    break;
                }
                // if the piece is the other color you can move there
                else {
                    ChessMove validMove = new ChessMove(myPosition, tempPos, null);
                    validMoves.add(validMove);
                    break;
                }
            }
            else {
                ChessMove validMove = new ChessMove(myPosition, tempPos, null);
                validMoves.add(validMove);
            }
            tempCol += x;
            tempRow += y;
        }

        return validMoves;
    }
}