package chess;

import java.util.ArrayList;
import java.util.List;

public class KnightMoveStrategy implements MoveStrategy {
    List<ChessMove> validMoves = new ArrayList<>();
    @Override
    public List<ChessMove> getValidMoves(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor color) {
        // CHECK UP 2 RIGHT IF POSSIBLE
        if ((myPosition.getColumn() < BOARD_WIDTH) && (myPosition.getRow() < BOARD_HEIGHT)) {
            ChessMove tempMove = move_one(board, myPosition, color, 1, 2);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        // CHECK UP 2 LEFT IF POSSIBLE
        if ((myPosition.getColumn() > 1) && (myPosition.getRow() < BOARD_HEIGHT)) {
            ChessMove tempMove = move_one(board, myPosition, color, -1, 2);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        // CHECK DOWN 2 RIGHT IF POSSIBLE
        if ((myPosition.getColumn() < BOARD_WIDTH) && (myPosition.getRow() > 1)) {
            ChessMove tempMove = move_one(board, myPosition, color, 1, -2);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        // CHECK DOWN 2 LEFT IF POSSIBLE
        if ((myPosition.getColumn() > 1) && (myPosition.getRow() > 1)) {
            ChessMove tempMove = move_one(board, myPosition, color, -1, -2);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        // CHECK UP 1 RIGHT IF POSSIBLE
        if ((myPosition.getColumn() < BOARD_WIDTH) && (myPosition.getRow() < BOARD_HEIGHT)) {
            ChessMove tempMove = move_one(board, myPosition, color, 2, 1);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        // CHECK UP 1 LEFT IF POSSIBLE
        if ((myPosition.getColumn() > 1) && (myPosition.getRow() < BOARD_HEIGHT)) {
            ChessMove tempMove = move_one(board, myPosition, color, -2, 1);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        // CHECK DOWN 1 RIGHT IF POSSIBLE
        if ((myPosition.getColumn() < BOARD_WIDTH) && (myPosition.getRow() > 1)) {
            ChessMove tempMove = move_one(board, myPosition, color, 2, -1);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        // CHECK DOWN 1 LEFT IF POSSIBLE
        if ((myPosition.getColumn() > 1) && (myPosition.getRow() > 1)) {
            ChessMove tempMove = move_one(board, myPosition, color, -2, -1);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        return validMoves;
    }
}
