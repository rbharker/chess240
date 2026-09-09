package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    static final int BOARD_LENGTH = 8;
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

        switch (type) {
            case BISHOP:

                // check how far up right

                // check how far up left

                // check how far down right

                // check how far down left
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

    }
}
