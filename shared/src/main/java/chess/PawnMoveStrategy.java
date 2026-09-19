package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveStrategy implements MoveStrategy {
    @Override
    public Collection<ChessMove> validMoves(ChessGame.TeamColor color, ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        // white moves
        if (color == ChessGame.TeamColor.WHITE) {
            if (myPosition.getRow() == 2) {
                if (movePawn(board, myPosition, 2) != null) {
                    validMoves.addAll(movePawn(board, myPosition, 2));
                }
                if (movePawn(board, myPosition, 1) != null) {
                    validMoves.addAll(movePawn(board, myPosition, 1));
                }
            }
            else {
                if (movePawn(board, myPosition, 1) != null) {
                    validMoves.addAll(movePawn(board, myPosition, 1));
                }
            }
            if (capturePawn(color, board, myPosition, 1, 1) != null) {
                validMoves.addAll(capturePawn(color, board, myPosition, 1, 1)) ;
            }
            if (capturePawn(color, board, myPosition, -1, 1) != null) {
                validMoves.addAll(capturePawn(color, board, myPosition, -1, 1)) ;
            }
        }
        // black moves
        else if (color == ChessGame.TeamColor.BLACK) {
            if (myPosition.getRow() == 7) {
                if (movePawn(board, myPosition, -2) != null) {
                    validMoves.addAll(movePawn(board, myPosition, -2));
                }
                if (movePawn(board, myPosition, -1) != null) {
                    validMoves.addAll(movePawn(board, myPosition, -1));
                }
            }
            else {
                if (movePawn(board, myPosition, -1) != null) {
                    validMoves.addAll(movePawn(board, myPosition, -1));
                }
            }
            if (capturePawn(color, board, myPosition, 1, -1) != null) {
                validMoves.addAll(capturePawn(color, board, myPosition, 1, -1)) ;
            }
            if (capturePawn(color, board, myPosition, -1, -1) != null) {
                validMoves.addAll(capturePawn(color, board, myPosition, -1, -1)) ;
            }
        }

        return validMoves;
    }

    Collection<ChessMove> movePawn(ChessBoard board, ChessPosition myPosition, int spaces) {
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
                if ((tempRow <= BoardHeight) &&
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
                if ((tempRow <= BoardHeight) &&
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

    Collection<ChessMove> capturePawn(ChessGame.TeamColor color, ChessBoard board, ChessPosition myPosition, int x, int y) {
        int tempRow = myPosition.getRow() + y;
        int tempCol = myPosition.getColumn() + x;
        Collection<ChessMove> validMoves = new ArrayList<>();

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
}
