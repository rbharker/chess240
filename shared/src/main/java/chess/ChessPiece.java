package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public final ChessGame.TeamColor pieceColor;
    public final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> tempMoves;

        switch (type) {
            case BISHOP:
                BishopMoveStrategy bishop = new BishopMoveStrategy();
                tempMoves = bishop.getValidMoves(myPosition, board, pieceColor);
                validMoves.addAll(tempMoves);
                break;
            case KING:
                KingMoveStrategy king = new KingMoveStrategy();
                tempMoves = king.getValidMoves(myPosition, board, pieceColor);
                validMoves.addAll(tempMoves);
                break;
           case QUEEN:
               QueenMoveStrategy queen = new QueenMoveStrategy();
               tempMoves = queen.getValidMoves(myPosition, board, pieceColor);
               validMoves.addAll(tempMoves);
                break;
            case PAWN:
                break;
            case KNIGHT:
                KnightMoveStrategy knight = new KnightMoveStrategy();
                tempMoves = knight.getValidMoves(myPosition, board, pieceColor);
                validMoves.addAll(tempMoves);
                break;
            case ROOK:
                RookMoveStrategy rook = new RookMoveStrategy();
                tempMoves = rook.getValidMoves(myPosition, board, pieceColor);
                validMoves.addAll(tempMoves);
                break;
        }

        return validMoves;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
