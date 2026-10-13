package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveStrategy implements MoveStrategy {
    @Override
    public Collection<ChessMove> validMoves(ChessGame.TeamColor color, ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        if (MoveStrategy.moveOne(color, 1, 1, myPosition, board) != null) {
            validMoves.add(MoveStrategy.moveOne(color, 1, 1, myPosition, board));
        }
        if (MoveStrategy.moveOne(color, -1, -1, myPosition, board) != null) {
            validMoves.add(MoveStrategy.moveOne(color, -1, -1, myPosition, board));
        }
        if (MoveStrategy.moveOne(color, 1, -1, myPosition, board) != null) {
            validMoves.add(MoveStrategy.moveOne(color, 1, -1, myPosition, board));
        }
        if (MoveStrategy.moveOne(color, -1, 1, myPosition, board) != null) {
            validMoves.add(MoveStrategy.moveOne(color, -1, 1, myPosition, board));
        }
        if (MoveStrategy.moveOne(color, 1, 0, myPosition, board) != null) {
            validMoves.add(MoveStrategy.moveOne(color, 1, 0, myPosition, board));
        }
        if (MoveStrategy.moveOne(color, -1, 0, myPosition, board) != null) {
            validMoves.add(MoveStrategy.moveOne(color, -1, 0, myPosition, board));
        }
        if (MoveStrategy.moveOne(color, 0, 1, myPosition, board) != null) {
            validMoves.add(MoveStrategy.moveOne(color, 0, 1, myPosition, board));
        }
        if (MoveStrategy.moveOne(color, 0, -1, myPosition, board) != null) {
            validMoves.add(MoveStrategy.moveOne(color, 0, -1, myPosition, board));
        }

        return validMoves;
    }
}
