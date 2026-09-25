package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private final ChessPiece[][] board = new ChessPiece[8][8];
    private final int BoardLength = 8;
    private final int BoardWidth = 8;

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 0; i<BoardLength; i++) {
            for (int j = 0; j<BoardWidth; j++) {
                board[i][j] = null;
            }
        }
        for (int i = 0; i<BoardLength; i++) {
            board[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            board[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }

        board[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        board[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        board[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        board[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

        board[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);

        board[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);

        board[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        board[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        board[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        board[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
    }

    /**
     * Gets all of the opposing team's pieces
     *
     * @param color The position to get the piece from
     * @return A map of positions and pieces
     */
    public Map<ChessPosition, ChessPiece> getOtherPieces(ChessGame.TeamColor color) {
        Map<ChessPosition, ChessPiece> pieces = new HashMap<>();

        // for each position on the board
        for (int i = 0; i<BoardLength; i++) {
            for (int j=0; j<BoardWidth; j++) {
                // if a piece exists on the opposing team
                if (board[i][j] != null && board[i][j].getTeamColor() != color) {
                    ChessPosition position = new ChessPosition(i+1, j+1);
                    // add that piece to the map
                    pieces.put(position, board[i][j]);
                }
            }
        }

        return pieces;
    }

    /**
     * Gets all of the current team's pieces
     *
     * @param color The position to get the piece from
     * @return A map of positions and pieces
     */
    public Map<ChessPosition, ChessPiece> getMyPieces(ChessGame.TeamColor color) {
        Map<ChessPosition, ChessPiece> pieces = new HashMap<>();

        // for each space on the board
        for (int i = 0; i<BoardLength; i++) {
            for (int j=0; j<BoardWidth; j++) {
                // if a piece exists on your team
                if (board[i][j] != null && board[i][j].getTeamColor() == color) {
                    ChessPosition position = new ChessPosition(i+1, j+1);
                    // add that piece to the map
                    pieces.put(position, board[i][j]);
                }
            }
        }

        return pieces;
    }

    /**
     * Gets the position of the king
     *
     * @param color The position to get the piece from
     * @return A map of positions and pieces
     */
    public ChessPosition getKingPosition(ChessGame.TeamColor color) {
        ChessPosition kingPosition = null;

        // check each space on the board
        for (int i = 0; i<BoardLength; i++) {
            for (int j=0; j<BoardWidth; j++) {
                // if you find that team's king
                if (board[i][j] != null && board[i][j].getTeamColor() == color) {
                    if (board[i][j].getPieceType() == ChessPiece.PieceType.KING) {
                        // give the king's position
                        kingPosition = new ChessPosition(i+1, j+1);
                    }
                }
            }
        }
        return kingPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder retVal = new StringBuilder("ChessBoard{");
        for (int i = 0; i<8; i++) {
            for (int j = 0; j<8; j++) {
                if (board[i][j] != null) {
                    retVal.append(board[i][j].toString());
                    retVal.append(", ");
                }
                else {
                    retVal.append("blank, ");
                }
            }
        }
        retVal.append("}");
        return retVal.toString();
    }
}
