package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {

    }
    // set default board
    ChessBoard currentBoard = new ChessBoard();
    // set default color
    ChessGame.TeamColor currentTurn = TeamColor.WHITE;

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        ChessPiece piece = currentBoard.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> possibleValidMoves = piece.pieceMoves(currentBoard, startPosition);
        for (ChessMove move : possibleValidMoves) {
            // some logic to figure out if the move is legal or not
        }

        return validMoves;

    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = currentBoard.getPiece(start);

        currentBoard.addPiece(start, null);
        currentBoard.addPiece(end, piece);

        if (isInCheck(getTeamTurn())) {
            throw new InvalidMoveException("Invalid move!");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // try all of the opponent's pieces possible moves
        // if one of the moves allows the king to be captured
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // use isInCheck() to see if the king is in check
        // try all of the different possible moves for the king and
        // if each of them still result in the king being in check
        // return true for checkmate
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // check each piece
        // if each one returns null possible moves
        // that person is in stalemate
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currentBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(currentBoard, chessGame.currentBoard) && currentTurn == chessGame.currentTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentBoard, currentTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + currentBoard +
                ", currentTurn=" + currentTurn +
                '}';
    }
}