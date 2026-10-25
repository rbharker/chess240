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
                if (MoveStrategy.movePawn(board, myPosition, 2) != null) {
                    validMoves.addAll(MoveStrategy.movePawn(board, myPosition, 2));
                }
                if (MoveStrategy.movePawn(board, myPosition, 1) != null) {
                    validMoves.addAll(MoveStrategy.movePawn(board, myPosition, 1));
                }
            }
            else {
                if (MoveStrategy.movePawn(board, myPosition, 1) != null) {
                    validMoves.addAll(MoveStrategy.movePawn(board, myPosition, 1));
                }
            }
            if (MoveStrategy.capturePawn(color, board, myPosition, 1, 1) != null) {
                validMoves.addAll(MoveStrategy.capturePawn(color, board, myPosition, 1, 1)) ;
            }
            if (MoveStrategy.capturePawn(color, board, myPosition, -1, 1) != null) {
                validMoves.addAll(MoveStrategy.capturePawn(color, board, myPosition, -1, 1)) ;
            }
        }
        // black moves
        else if (color == ChessGame.TeamColor.BLACK) {
            if (myPosition.getRow() == 7) {
                if (MoveStrategy.movePawn(board, myPosition, -2) != null) {
                    validMoves.addAll(MoveStrategy.movePawn(board, myPosition, -2));
                }
                if (MoveStrategy.movePawn(board, myPosition, -1) != null) {
                    validMoves.addAll(MoveStrategy.movePawn(board, myPosition, -1));
                }
            }
            else {
                if (MoveStrategy.movePawn(board, myPosition, -1) != null) {
                    validMoves.addAll(MoveStrategy.movePawn(board, myPosition, -1));
                }
            }
            if (MoveStrategy.capturePawn(color, board, myPosition, 1, -1) != null) {
                validMoves.addAll(MoveStrategy.capturePawn(color, board, myPosition, 1, -1)) ;
            }
            if (MoveStrategy.capturePawn(color, board, myPosition, -1, -1) != null) {
                validMoves.addAll(MoveStrategy.capturePawn(color, board, myPosition, -1, -1)) ;
            }
        }

        return validMoves;
    }
}
