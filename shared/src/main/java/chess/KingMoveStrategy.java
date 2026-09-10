package chess;

import java.util.ArrayList;
import java.util.List;

public class KingMoveStrategy implements MoveStrategy {
    List<ChessMove> validMoves = new ArrayList<>();
    @Override
    public List<ChessMove> getValidMoves(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor color) {
        // CHECK UP RIGHT IF POSSIBLE
        if ((myPosition.getColumn() < BOARD_WIDTH) && (myPosition.getRow() < BOARD_HEIGHT)) {
            ChessMove tempMove = move_one(board, myPosition, color, 1, 1);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        // CHECK UP LEFT IF POSSIBLE
        if ((myPosition.getColumn() > 1) && (myPosition.getRow() < BOARD_HEIGHT)) {
            ChessMove tempMove = move_one(board, myPosition, color, -1, 1);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        // CHECK DOWN RIGHT IF POSSIBLE
        if ((myPosition.getColumn() < BOARD_WIDTH) && (myPosition.getRow() > 1)) {
            ChessMove tempMove = move_one(board, myPosition, color, 1, -1);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        // CHECK DOWN LEFT IF POSSIBLE
        if ((myPosition.getColumn() > 1) && (myPosition.getRow() > 1)) {
            ChessMove tempMove = move_one(board, myPosition, color, -1, -1);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        // CHECK UP IF POSSIBLE
        if ((myPosition.getRow() < BOARD_HEIGHT)) {
            ChessMove tempMove = move_one(board, myPosition, color, 0, 1);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }
        // CHECK DOWN IF POSSIBLE
        if ((myPosition.getRow() > 1)) {
            ChessMove tempMove = move_one(board, myPosition, color, 0, -1);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        // CHECK RIGHT IF POSSIBLE
        if ((myPosition.getColumn() < BOARD_WIDTH)) {
            ChessMove tempMove = move_one(board, myPosition, color, 1, 0);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        // CHECK LEFT IF POSSIBLE
        if ((myPosition.getColumn() > 1)) {
            ChessMove tempMove = move_one(board, myPosition, color,-1, 0);
            if (tempMove != null) {
                validMoves.add(tempMove);
            }
        }

        return validMoves;
    }
}
