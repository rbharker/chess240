package client;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardUI {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final int BOARD_LINE_WIDTH_IN_PADDED_CHARS = 0;
    private static final int HEADER_LINE_WIDTH_IN_PADDED_CHARS = 1;

    // Padded characters.
    private static final String EMPTY1 = "  ";
    private static final String EMPTY2 = " ";
    private static final String K = " K ";
    private static final String Q = " Q ";
    private static final String N = " N ";
    private static final String B = " B ";
    private static final String R = " R ";
    private static final String P = " P ";

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeader(out);

        drawChessBoard(out, "WHITE", args[0]);

        drawHeader(out);

        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_DARK_GREY);
    }

    private static void drawHeader(PrintStream out) {

        setDarkGray(out);

        String[] headerFooter = {"a", "b", "c", "d", "e", "f", "g", "h"};
        out.print(EMPTY1.repeat(HEADER_LINE_WIDTH_IN_PADDED_CHARS));
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headerFooter[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(EMPTY1.repeat(HEADER_LINE_WIDTH_IN_PADDED_CHARS));
            }
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String headerText) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_YELLOW);

        out.print(headerText);

        setDarkGray(out);
    }

    private static void drawChessBoard(PrintStream out, String rowStartColor, String playerColor) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            if (boardRow == 0 ) {
                drawRowOfSquares(out, rowStartColor, playerColor, false, true, boardRow);
            }
            else if (boardRow == 7) {
                if (playerColor == "WHITE") {
                    drawRowOfSquares(out, rowStartColor, "BLACK", false, true, boardRow);
                }
                else {
                    drawRowOfSquares(out, rowStartColor, "WHITE", false, true, boardRow);
                }
            }
            else if (boardRow == 1) {
                drawRowOfSquares(out, rowStartColor, playerColor, true, false, boardRow);
            }
            else if (boardRow == 6) {
                if (playerColor == "WHITE") {
                    drawRowOfSquares(out, rowStartColor, "BLACK", true, false, boardRow);
                }
                else {
                    drawRowOfSquares(out, rowStartColor, "WHITE", true, false, boardRow);
                }
            }
            else {
                drawRowOfSquares(out, rowStartColor, playerColor, false, false, boardRow);
            }

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                // Draw horizontal row separator.
                drawHorizontalLine(out);
                setDarkGray(out);
            }

            if (rowStartColor == "WHITE") {
                rowStartColor = "BLACK";
            }
            else {
                rowStartColor = "WHITE";
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out, String rowStartColor, String playerColor,
                                         boolean pawns, boolean pieces, int rowNum) {
        int counter = 0;
        String currentSquareColor = "";
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_YELLOW);

        out.print(rowNum + 1);
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {

                if (rowStartColor.equals("WHITE")) {
                    if (counter % 2 == 0) {
                        currentSquareColor = "WHITE";
                        setWhite(out);
                    }
                    else {
                        currentSquareColor = "BLACK";
                        setDarkGray(out);
                    }
                } else {
                    if (counter % 2 == 0) {
                        setDarkGray(out);
                        currentSquareColor = "BLACK";
                    }
                    else {
                        setWhite(out);
                        currentSquareColor = "WHITE";
                    }
                }

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));
                    if (pawns == true) {
                        printPlayer(out, P, currentSquareColor, playerColor);
                    }
                    else if (pieces == true) {
                        switch (boardCol) {
                            case 0:
                                printPlayer(out, R, currentSquareColor, playerColor);
                                break;
                            case 1:
                                printPlayer(out, N, currentSquareColor, playerColor);
                                break;
                            case 2:
                                printPlayer(out, B, currentSquareColor, playerColor);
                                break;
                            case 3:
                                printPlayer(out, K, currentSquareColor, playerColor);
                                break;
                            case 4:
                                printPlayer(out, Q, currentSquareColor, playerColor);
                                break;
                            case 5:
                                printPlayer(out, B, currentSquareColor, playerColor);
                                break;
                            case 6:
                                printPlayer(out, N, currentSquareColor, playerColor);
                                break;
                            case 7:
                                printPlayer(out, R, currentSquareColor, playerColor);
                                break;
                            default:
                                printPlayer(out, EMPTY2, currentSquareColor, playerColor);
                                break;
                        }
                    }
                    else {
                        printPlayer(out, EMPTY, currentSquareColor, playerColor);
                    }
                    out.print(EMPTY.repeat(suffixLength));
                }
                else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // Draw vertical column separator.
                    setDarkGray(out);
                    out.print(EMPTY.repeat(BOARD_LINE_WIDTH_IN_PADDED_CHARS));
                }
                setLightGray(out);
                counter += 1;

            }
            out.print(SET_BG_COLOR_DARK_GREY);
            out.print(SET_TEXT_COLOR_YELLOW);

            out.print(rowNum + 1);
            out.println();
        }
    }

    private static void drawHorizontalLine(PrintStream out) {

        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_PADDED_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * BOARD_LINE_WIDTH_IN_PADDED_CHARS;

        for (int lineRow = 0; lineRow < BOARD_LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            setDarkGray(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            setLightGray(out);
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void setDarkGray(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_BLUE);
    }

    private static void setLightGray(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void printPlayer(PrintStream out, String player, String squareColor, String playerColor) {
        if (squareColor.equals("WHITE")) {
            out.print(SET_BG_COLOR_WHITE);
        }
        else {
            out.print(SET_BG_COLOR_DARK_GREY);
        }

        if (playerColor == "WHITE") {
            out.print(SET_TEXT_COLOR_BLUE);
        }
        else {
            out.print(SET_TEXT_COLOR_RED);
        }

        out.print(player);

        setWhite(out);
    }
}
