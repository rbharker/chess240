package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveStrategy implements MoveStrategy{
    @Override
    public Collection<ChessMove> validMoves(ChessGame.TeamColor color, ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        validMoves.addAll(moveMultiple(color, 1, 1, myPosition, board));
        validMoves.addAll(moveMultiple(color, -1, 1, myPosition, board));
        validMoves.addAll(moveMultiple(color, 1, -1, myPosition, board));
        validMoves.addAll(moveMultiple(color, -1, -1, myPosition, board));

        return validMoves;
    }
}
