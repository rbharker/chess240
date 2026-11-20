package client;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static client.EscapeSequences.*;

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

    private static Random rand = new Random();

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeader(out);

        drawChessBoard(out, args[0]);

        drawHeader(out);

        out.print(SET_TEXT_COLOR_WHITE);
        out.print(RESET_BG_COLOR);
    }

    private static void drawHeader(PrintStream out) {

        setDarkGray(out);

        String[] headerFooter = {"a", "b", "c", "d", "e", "f", "g", "h"};
        out.print(EMPTY2.repeat(HEADER_LINE_WIDTH_IN_PADDED_CHARS));
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

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_YELLOW);

        out.print(player);

        setDarkGray(out);
    }

    private static void drawChessBoard(PrintStream out, String playerColor) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out, playerColor);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                // Draw horizontal row separator.
                drawHorizontalLine(out);
                setDarkGray(out);
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out, String color) {
        int counter = 0;
        String currentColor = "";
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            if (color.equals("WHITE")) {
                if (counter % 2 == 0) {
                    currentColor = "WHITE";
                    setWhite(out);
                }
                else {
                    currentColor = "BLACK";
                    setDarkGray(out);
                }
            } else {
                if (counter % 2 == 0) {
                    setDarkGray(out);
                    currentColor = "BLACK";
                }
                else {
                    setWhite(out);
                    currentColor = "WHITE";
                }
            }

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));
                    printPlayer(out, rand.nextBoolean() ? K : Q, currentColor);
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

            }
            out.println();
            counter++;
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

    private static void resetTerminal(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void printPlayer(PrintStream out, String player, String playerColor) {
        if (playerColor.equals("WHITE")) {
            out.print(SET_BG_COLOR_WHITE);
        }
        else {
            out.print(SET_BG_COLOR_DARK_GREY);
        }
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLUE);

        out.print(player);

        setWhite(out);
    }
}
