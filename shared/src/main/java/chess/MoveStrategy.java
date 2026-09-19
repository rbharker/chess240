package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface MoveStrategy {

    int BoardHeight = 8;
    int BoardWidth = 8;

    Collection<ChessMove> validMoves(ChessGame.TeamColor color, ChessBoard board, ChessPosition myPosition);

    default ChessMove moveOne(ChessGame.TeamColor color, int x, int y, ChessPosition myPosition, ChessBoard board) {
        int tempRow = myPosition.getRow() + y;
        int tempCol = myPosition.getColumn() + x;

        // if you're staying in the board limits
        if ((tempCol <= BoardWidth) &&
                (tempCol >= 1) &&
                (tempRow <= BoardHeight) &&
                (tempRow >= 1)) {

            ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);
            ChessMove validMove = new ChessMove(myPosition, tempPosition, null);
            // if there's a piece there
            if (board.getPiece(tempPosition) != null) {
                // if the piece is your color, you can't move there
                if (board.getPiece(tempPosition).getTeamColor() == color) {
                    return null;
                }
                // if the piece is the other color, you can move there
                else {
                    return validMove;
                }
            }
            // if there's not a piece there you can move there
            else {
                return validMove;
            }
        }
        else {
            return null;
        }
    }

    default Collection<ChessMove> moveMultiple(ChessGame.TeamColor color, int x, int y, ChessPosition myPosition, ChessBoard board) {
        int tempRow = myPosition.getRow() + y;
        int tempCol = myPosition.getColumn() + x;
        Collection<ChessMove> validMoves = new ArrayList<>();

        // if you're staying in the board limits
        while ((tempCol <= BoardWidth) &&
                (tempCol >= 1) &&
                (tempRow <= BoardHeight) &&
                (tempRow >= 1)) {

            ChessPosition tempPosition = new ChessPosition(tempRow, tempCol);
            ChessMove validMove = new ChessMove(myPosition, tempPosition, null);
            // if there's a piece there
            if (board.getPiece(tempPosition) != null) {
                // if the piece is your color, you can't move there
                // break the loop and return the moves already found
                if (board.getPiece(tempPosition).getTeamColor() == color) {
                    return validMoves;
                }
                // if the piece is the other color, you can move there
                // add the move to collection
                // break the loop and return the moves found
                else {
                    validMoves.add(validMove);
                    return validMoves;
                }
            }
            // if there's not a piece there you can move there
            // keep iterating through the loop
            else {
                validMoves.add(validMove);
            }
            tempCol += x;
            tempRow += y;
        }
        return validMoves;
    }
}