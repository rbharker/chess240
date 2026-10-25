package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface MoveStrategy {

    int BOARD_HEIGHT = 8;
    int BOARD_WIDTH = 8;

    Collection<ChessMove> validMoves(ChessGame.TeamColor color, ChessBoard board, ChessPosition myPosition);

    static ChessMove moveOne(ChessGame.TeamColor color, int x, int y, ChessPosition myPosition, ChessBoard board) {
        int tempRow = myPosition.getRow() + y;
        int tempCol = myPosition.getColumn() + x;

        // if you're staying in the board limits
        if ((tempCol <= BOARD_WIDTH) &&
                (tempCol >= 1) &&
                (tempRow <= BOARD_HEIGHT) &&
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

    static Collection<ChessMove> moveMultiple(ChessGame.TeamColor color, int x, int y, ChessPosition myPosition, ChessBoard board) {
        int tempRow = myPosition.getRow() + y;
        int tempCol = myPosition.getColumn() + x;
        Collection<ChessMove> validMoves = new ArrayList<>();

        // if you're staying in the board limits
        while ((tempCol <= BOARD_WIDTH) &&
                (tempCol >= 1) &&
                (tempRow <= BOARD_HEIGHT) &&
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

    static Collection<ChessMove> capturePawn(ChessGame.TeamColor color, ChessBoard board, ChessPosition myPosition, int x, int y) {
        int tempRow = myPosition.getRow() + y;
        int tempCol = myPosition.getColumn() + x;
        Collection<ChessMove> validMoves = new ArrayList<>();

        // if you're staying in the board limits
        if ((tempCol <= BOARD_WIDTH) &&
                (tempCol >= 1) &&
                (tempRow <= BOARD_HEIGHT) &&
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
                    if (tempRow == 8 || tempRow == 1) {
                        ChessMove validMove1 = new ChessMove(myPosition, tempPosition, ChessPiece.PieceType.ROOK);
                        ChessMove validMove2 = new ChessMove(myPosition, tempPosition, ChessPiece.PieceType.BISHOP);
                        ChessMove validMove3 = new ChessMove(myPosition, tempPosition, ChessPiece.PieceType.KNIGHT);
                        ChessMove validMove5 = new ChessMove(myPosition, tempPosition, ChessPiece.PieceType.QUEEN);
                        validMoves.add(validMove1);
                        validMoves.add(validMove2);
                        validMoves.add(validMove3);
                        validMoves.add(validMove5);
                    }
                    else {
                        validMoves.add(validMove);
                    }
                    return validMoves;
                }
            }
            // if there's not a piece there you cannot move there
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

    static Collection<ChessMove> movePawn(ChessBoard board, ChessPosition myPosition, int spaces) {
        int tempRow;
        ChessPosition tempPosition;
        ChessMove validMove;
        Collection<ChessMove> validMoves = new ArrayList<>();
        switch(spaces) {
            case 2, -2:
                tempRow = myPosition.getRow() + spaces;
                tempPosition = new ChessPosition(tempRow, myPosition.getColumn());
                validMove = new ChessMove(myPosition, tempPosition, null);
                // if you're staying in the board limits
                if ((tempRow <= BOARD_HEIGHT) &&
                        (tempRow >= 1)) {
                    // if there's a piece there
                    if (board.getPiece(tempPosition) != null) {
                        // if there is a piece there, you can't move there
                        return null;
                    }
                    // if there's not a piece there you can move there
                    // only if the space before is also empty
                    else {
                        tempRow = myPosition.getRow() + (spaces/2);
                        tempPosition = new ChessPosition(tempRow, myPosition.getColumn());
                        if (board.getPiece(tempPosition) != null) {
                            // if there is a piece there, you can't move there
                            return null;
                        }
                        // if there's not a piece there you can move there
                        // only if the space before is also empty
                        else {
                            validMoves.add(validMove);
                            return validMoves;
                        }
                    }
                }
                else {
                    return null;
                }
            default:
                tempRow = myPosition.getRow() + spaces;
                tempPosition = new ChessPosition(tempRow, myPosition.getColumn());
                validMove = new ChessMove(myPosition, tempPosition, null);
                // if you're staying in the board limits
                if ((tempRow <= BOARD_HEIGHT) &&
                        (tempRow >= 1)) {
                    // if there's a piece there
                    if (board.getPiece(tempPosition) != null) {
                        // if there is a piece there, you can't move there
                        return null;
                    }
                    // if there's not a piece there you can move there
                    else {
                        if (tempRow == 8 || tempRow == 1) {
                            ChessMove validMove1 = new ChessMove(myPosition, tempPosition, ChessPiece.PieceType.ROOK);
                            ChessMove validMove2 = new ChessMove(myPosition, tempPosition, ChessPiece.PieceType.BISHOP);
                            ChessMove validMove3 = new ChessMove(myPosition, tempPosition, ChessPiece.PieceType.KNIGHT);
                            ChessMove validMove4 = new ChessMove(myPosition, tempPosition, ChessPiece.PieceType.QUEEN);
                            validMoves.add(validMove1);
                            validMoves.add(validMove2);
                            validMoves.add(validMove3);
                            validMoves.add(validMove4);
                        }
                        else {
                            validMoves.add(validMove);
                        }
                        return validMoves;
                    }
                }
                else {
                    return null;
                }
        }
    }
}