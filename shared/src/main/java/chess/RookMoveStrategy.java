package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMoveStrategy implements MoveStrategy {
    List<ChessMove> validMoves = new ArrayList<>();
    @Override
    public List<ChessMove> getValidMoves(ChessPosition myPosition, ChessBoard board, ChessGame.TeamColor color) {
        // CHECK HOW FAR UP IF POSSIBLE
        if ((myPosition.getRow() < BOARD_HEIGHT)) {
            Collection<ChessMove> tempMoves = move_multiple(board, myPosition, color, 0, 1);
            validMoves.addAll(tempMoves);
        }
        // CHECK HOW FAR DOWN IF POSSIBLE
        if ((myPosition.getRow() > 1)) {
            Collection<ChessMove> tempMoves = move_multiple(board, myPosition, color, 0, -1);
            validMoves.addAll(tempMoves);
        }

        // CHECK HOW FAR RIGHT IF POSSIBLE
        if ((myPosition.getColumn() < BOARD_WIDTH)) {
            Collection<ChessMove> tempMoves = move_multiple(board, myPosition, color, 1, 0);
            validMoves.addAll(tempMoves);
        }

        // CHECK HOW FAR LEFT IF POSSIBLE
        if ((myPosition.getColumn() > 1)) {
            Collection<ChessMove> tempMoves = move_multiple(board, myPosition, color, -1, 0);
            validMoves.addAll(tempMoves);
        }

        return validMoves;
    }
}
