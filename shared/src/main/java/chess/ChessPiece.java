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

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    static final int BOARD_HEIGHT = 8;
    static final int BOARD_WIDTH = 8;

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

        // switch statement?
        // needs to be a loop inside of each switch case
        // loop can be in range of theoretical possible moves
        // in each switch case:
            // use your position and ChessBoard's getPiece function to determine what moves you can make
            // have to take into account board limits
            // create a ChessMove object for each move
            // return the ChessMove
            // knight will be hardest
            // add each ChessMove to the collection
        // outside of switch statement:
        // return collection

        Collection<ChessMove> validMoves = new ArrayList<>();

        // make functions that move single and move multiple
        // move multiple you give it which direction x and y and it will repeatedly move that way
            // for bishop you'd have to call it four times
        // move single just moves once

        switch (type) {
            case BISHOP:
                // CHECK HOW FAR UP RIGHT IF POSSIBLE
                if ((myPosition.getColumn() < BOARD_WIDTH) && (myPosition.getRow() < BOARD_HEIGHT)) {
                    Collection<ChessMove> tempMoves = move_multiple(board, myPosition, 1, 1);
                    validMoves.addAll(tempMoves);
                }

                // CHECK HOW FAR UP LEFT IF POSSIBLE
                if ((myPosition.getColumn() > 1) && (myPosition.getRow() < BOARD_HEIGHT)) {
                    Collection<ChessMove> tempMoves = move_multiple(board, myPosition, -1, 1);
                    validMoves.addAll(tempMoves);
                }

                // CHECK HOW FAR DOWN RIGHT IF POSSIBLE
                if ((myPosition.getColumn() < BOARD_WIDTH) && (myPosition.getRow() > 1)) {
                    Collection<ChessMove> tempMoves = move_multiple(board, myPosition, 1, -1);
                    validMoves.addAll(tempMoves);
                }

                // CHECK HOW FAR DOWN LEFT IF POSSIBLE
                if ((myPosition.getColumn() > 1) && (myPosition.getRow() > 1)) {
                    Collection<ChessMove> tempMoves = move_multiple(board, myPosition, -1, -1);
                    validMoves.addAll(tempMoves);
                }
                break;

            case KING:
                break;
            case QUEEN:
                break;
            case PAWN:
                break;
            case KNIGHT:
                break;
            case ROOK:
                break;
        }

        return validMoves;

    }

    //    private ChessMove move_one(ChessBoard board, ChessPosition myPosition, int x, int y) {
    //
    //    }

    private Collection<ChessMove> move_multiple(ChessBoard board, ChessPosition myPosition, int x, int y) {
        int tempCol = myPosition.getColumn() + x;
        int tempRow = myPosition.getRow() + y;
        Collection<ChessMove> validMoves = new ArrayList<>();
        // while you're still on the board
        while ((tempCol <= BOARD_WIDTH) && (tempRow <= BOARD_HEIGHT)
                && (tempCol > 0) && (tempRow > 0)) {
            ChessPosition tempPos = new ChessPosition(tempRow, tempCol);
            if (board.getPiece(tempPos) != null) {
                // if the piece is your color you can't move there
                // the farthest you can go this way is the previous iteration
                if (board.getPiece(tempPos).pieceColor == pieceColor) {
                    break;
                }
                // if the piece is the other color you can move there
                else {
                    ChessMove validMove = new ChessMove(myPosition, tempPos, null);
                    validMoves.add(validMove);
                    break;
                }
            }
            else {
                ChessMove validMove = new ChessMove(myPosition, tempPos, null);
                validMoves.add(validMove);
            }
            tempCol += x;
            tempRow += y;
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
