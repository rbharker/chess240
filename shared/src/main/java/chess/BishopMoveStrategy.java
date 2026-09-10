package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMoveStrategy implements MoveStrategy {
    List<ChessMove> validMoves = new ArrayList<>();
    @Override
    public List<ChessMove> getValidMoves(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor color) {

        // CHECK HOW FAR UP RIGHT IF POSSIBLE
        if ((myPosition.getColumn() < BOARD_WIDTH) && (myPosition.getRow() < BOARD_HEIGHT)) {
            Collection<ChessMove> tempMoves = move_multiple(board, myPosition, color, 1, 1);
            validMoves.addAll(tempMoves);
        }

        // CHECK HOW FAR UP LEFT IF POSSIBLE
        if ((myPosition.getColumn() > 1) && (myPosition.getRow() < BOARD_HEIGHT)) {
            Collection<ChessMove> tempMoves = move_multiple(board, myPosition, color, -1, 1);
            validMoves.addAll(tempMoves);
        }

        // CHECK HOW FAR DOWN RIGHT IF POSSIBLE
        if ((myPosition.getColumn() < BOARD_WIDTH) && (myPosition.getRow() > 1)) {
            Collection<ChessMove> tempMoves = move_multiple(board, myPosition, color, 1, -1);
            validMoves.addAll(tempMoves);
        }

        // CHECK HOW FAR DOWN LEFT IF POSSIBLE
        if ((myPosition.getColumn() > 1) && (myPosition.getRow() > 1)) {
            Collection<ChessMove> tempMoves = move_multiple(board, myPosition, color, -1, -1);
            validMoves.addAll(tempMoves);
        }

        return validMoves;
    }
}
