package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    // set default board
    private ChessBoard currentBoard = new ChessBoard();
    // set default color
    private ChessGame.TeamColor currentTurn = TeamColor.WHITE;
    public ChessGame() {
        currentBoard.resetBoard();
    }

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

        // if there isn't a piece there return null
        if (piece == null) {
            return null;
        }

        // get the possible valid moves from phase 0
        Collection<ChessMove> possibleValidMoves = piece.pieceMoves(currentBoard, startPosition);
        // for each move use checkMove to determine if it's actually legal
        for (ChessMove move : possibleValidMoves) {
            if (checkMove(move, piece.getTeamColor())) {
                validMoves.add(move);
            }
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
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        boolean inValidMoves = false;

        // if there are no valid moves throw exception
        if (validMoves == null) {throw new InvalidMoveException("Invalid move!");}

        // check if the move the player wants to make
        // is in the list of valid moves
        for (ChessMove tempMove : validMoves) {
            if (tempMove.equals(move)) {
                inValidMoves = true;
            }
        }

        // declare variables and get positions
        ChessPiece movePiece;
        ChessPiece capturePiece;
        ChessGame.TeamColor pieceColor;
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();

        // if there is a piece at that space
        // get the values
        if (currentBoard.getPiece(start) != null) {
            movePiece = currentBoard.getPiece(start);
            capturePiece = currentBoard.getPiece(end);
            pieceColor = currentBoard.getPiece(move.getStartPosition()).getTeamColor();
        }
        // otherwise throw error
        else {
            throw new InvalidMoveException("Invalid move!");
        }

        // if the proposed move is a valid move
        // and if the color is allowed to move right now (it's their turn)
        // and there is a piece to move
        if (inValidMoves && (pieceColor == currentTurn) && (movePiece != null)) {
            // make the move
            currentBoard.addPiece(start, null);
            currentBoard.addPiece(end, movePiece);

            // now check if that move puts the player in check
            // if so, reverse the move and throw an error
            if (isInCheck(getTeamTurn())) {
                currentBoard.addPiece(start, movePiece);
                currentBoard.addPiece(end, capturePiece);
                throw new InvalidMoveException("Invalid move!");
            }
            // if the move requires a promotion
            // replace the piece with the promotion piece
            if (move.getPromotionPiece() != null) {
                ChessPiece promotion = new ChessPiece(getTeamTurn(), move.getPromotionPiece());
                currentBoard.addPiece(end, promotion);
            }
            // set the current team's color to the next color
            if (currentTurn == TeamColor.BLACK) {
                currentTurn = TeamColor.WHITE;
            }
            else {
                currentTurn = TeamColor.BLACK;
            }
        }
        // otherwise throw an error
        else {
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
        // if one of the moves ends in the kings position
        // that person is in check

        // get a map of position : piece for the other team
        Map<ChessPosition, ChessPiece> pieces = getBoard().getOtherPieces(teamColor);
        ChessPosition kingPosition = currentBoard.getKingPosition(teamColor);
        // for each piece in the map
        // check all of the valid moves for that piece
        // if any of those moves captures the king: return true
        // else: return false
        for (Map.Entry<ChessPosition, ChessPiece> piece : pieces.entrySet()) {
            Collection<ChessMove> tempMoves = piece.getValue().pieceMoves(currentBoard, piece.getKey());
            for (ChessMove move : tempMoves) {
                ChessPosition endPosition = move.getEndPosition();
                if (endPosition.equals(kingPosition)) {
                    return true;
                }
            }
        }
        return false;
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
        ChessPosition kingPosition = currentBoard.getKingPosition(teamColor);
        ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        Collection<ChessMove> kingMoves = king.pieceMoves(currentBoard, kingPosition);
        if (kingMoves.isEmpty()) {
            return false;
        }
        for (ChessMove move : kingMoves) {
            if (checkMove(move, teamColor)) {
                return false;
            }
        }

        // if another piece can move and get the player out of check
        // return false for checkmate
        Map<ChessPosition, ChessPiece> pieces = getBoard().getMyPieces(teamColor);
        for (Map.Entry<ChessPosition, ChessPiece> piece : pieces.entrySet()) {
            Collection<ChessMove> tempMoves = piece.getValue().pieceMoves(currentBoard, piece.getKey());
            for (ChessMove move : tempMoves) {
                if (checkMove(move, teamColor)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean retVal = true;
        // get a map of pieces for your own team
        // for each piece of yours on the board
        // if that piece has possible moves, you are not in stalemate
        Map<ChessPosition, ChessPiece> pieces = getBoard().getMyPieces(teamColor);
        for (Map.Entry<ChessPosition, ChessPiece> piece : pieces.entrySet()) {
            Collection<ChessMove> tempMoves = validMoves(piece.getKey());
            if (!tempMoves.isEmpty()) {
                retVal =  false;
            }
        }
        // otherwise if you are in checkmate and if it is not your turn
        // or if you are in check
        // you are not in stalemate
        if ((isInCheckmate(teamColor) && (currentTurn != teamColor)) || isInCheck(teamColor)) {
            retVal = false;
        }
        return retVal;
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

    /**
     * Tests if a move is valid without changing the board
     *
     * @param move the move
     * @return whether the move is valid
     */
    public boolean checkMove(ChessMove move, ChessGame.TeamColor color) {
        // get positions and pieces
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece movePiece = currentBoard.getPiece(start);
        ChessPiece capturePiece = currentBoard.getPiece(end);
        boolean retVal;

        // set the board as if you're moving the piece there
        currentBoard.addPiece(start, null);
        currentBoard.addPiece(end, movePiece);

        // now check if that person is in check
        // if so the move is not valid
        if (isInCheck(color)) {
            retVal = false;
        }
        else {
            retVal = true;
        }

        // set the board back to how it was before
        currentBoard.addPiece(start, movePiece);
        currentBoard.addPiece(end, capturePiece);
        return retVal;
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