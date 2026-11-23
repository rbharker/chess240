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

        drawHeaderText(out, args[0]);

        drawChessBoard(out, "WHITE", args[0]);

        drawHeaderText(out, args[0]);

        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_BG_COLOR_DARK_GREY);
    }

    private static void drawHeaderText(PrintStream out, String playerColor) {

        setDarkGray(out);

        String[] headerFooterWhite = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] headerFooterBlack = {"h", "g", "f", "e", "d", "c", "b", "a"};
        if (playerColor.equals("WHITE")) {
            out.print(EMPTY1.repeat(HEADER_LINE_WIDTH_IN_PADDED_CHARS));
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, headerFooterWhite[boardCol]);

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    out.print(EMPTY1.repeat(HEADER_LINE_WIDTH_IN_PADDED_CHARS));
                }
            }

            out.println();
        } else {
            out.print(EMPTY1.repeat(HEADER_LINE_WIDTH_IN_PADDED_CHARS));
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, headerFooterBlack[boardCol]);

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    out.print(EMPTY1.repeat(HEADER_LINE_WIDTH_IN_PADDED_CHARS));
                }
            }

            out.println();
        }

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
        int colorBoardRow = 1;
        String rowColor = "";
        if (playerColor.equals("WHITE")) {
            colorBoardRow = 8;
        }
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            if (boardRow == 0 ) {
                rowColor = playerColor;
                drawRowOfSquares(out, rowStartColor, rowColor, false, true, colorBoardRow, playerColor);
            }
            else if (boardRow == 7) {
                if (playerColor == "WHITE") {
                    drawRowOfSquares(out, rowStartColor, "BLACK", false, true, colorBoardRow, playerColor);
                }
                else {
                    drawRowOfSquares(out, rowStartColor, "WHITE", false, true, colorBoardRow, playerColor);
                }
            }
            else if (boardRow == 1) {
                rowColor = playerColor;
                drawRowOfSquares(out, rowStartColor, rowColor, true, false, colorBoardRow, playerColor);
            }
            else if (boardRow == 6) {
                if (playerColor == "WHITE") {
                    drawRowOfSquares(out, rowStartColor, "BLACK", true, false, colorBoardRow, playerColor);
                }
                else {
                    drawRowOfSquares(out, rowStartColor, "WHITE", true, false, colorBoardRow, playerColor);
                }
            }
            else {
                rowColor = playerColor;
                drawRowOfSquares(out, rowStartColor, rowColor, false, false, colorBoardRow, playerColor);
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

            if (playerColor.equals("BLACK")) {
                colorBoardRow++;
            } else {
                colorBoardRow--;
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out, String rowStartColor, String rowColor,
                                         boolean pawns, boolean pieces, int rowNum, String playerColor) {
        int counter = 0;
        String currentSquareColor = "";
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_YELLOW);

        out.print(rowNum);
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
                        printPlayer(out, P, currentSquareColor, rowColor);
                    }
                    else if (pieces == true) {
                        switch (boardCol) {
                            case 0:
                                printPlayer(out, R, currentSquareColor, rowColor);
                                break;
                            case 1:
                                printPlayer(out, N, currentSquareColor, rowColor);
                                break;
                            case 2:
                                printPlayer(out, B, currentSquareColor, rowColor);
                                break;
                            case 3:
                                if (playerColor.equals("WHITE")) { printPlayer(out, Q, currentSquareColor, rowColor);}
                                else { printPlayer(out, K, currentSquareColor, rowColor);}
                                break;
                            case 4:
                                if (playerColor.equals("WHITE")) {printPlayer(out, K, currentSquareColor, rowColor);}
                                else {printPlayer(out, Q, currentSquareColor, rowColor);}
                                break;
                            case 5:
                                printPlayer(out, B, currentSquareColor, rowColor);
                                break;
                            case 6:
                                printPlayer(out, N, currentSquareColor, rowColor);
                                break;
                            case 7:
                                printPlayer(out, R, currentSquareColor, rowColor);
                                break;
                            default:
                                printPlayer(out, EMPTY2, currentSquareColor, rowColor);
                                break;
                        }
                    }
                    else {
                        printPlayer(out, EMPTY, currentSquareColor, rowColor);
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

            out.print(rowNum);
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
