// ConnectFourBoardTest.java

import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectFourBoardTest {

    private static final int ROWS = 6;
    private static final int COLUMNS = 7;

    private ConnectFourBoard[] boardsUnderTest() {
        return new ConnectFourBoard[] {
            new NaiveBoard(ROWS, COLUMNS),
            new Optimized(ROWS, COLUMNS)
        };
    }

    @Test
    public void emptyBoardHasNoWin() {
        for (ConnectFourBoard board : boardsUnderTest()) {
            assertFalse(label(board, "empty board should have no win"), board.hasConnectFour());
        }
    }

    @Test
    public void horizontalWinDetected() {
        for (ConnectFourBoard board : boardsUnderTest()) {
            playHorizontalWin(board, 'X');
            assertTrue(label(board, "should detect horizontal win"), board.hasConnectFour());
        }
    }

    @Test
    public void verticalWinDetected() {
        for (ConnectFourBoard board : boardsUnderTest()) {
            playVerticalWin(board, 'O');
            assertTrue(label(board, "should detect vertical win"), board.hasConnectFour());
        }
    }

    @Test
    public void verticalWinDetectedAboveFillers() {
        for (ConnectFourBoard board : boardsUnderTest()) {
            playRaisedVerticalWin(board, 'X');
            assertTrue(label(board, "should detect elevated vertical win"), board.hasConnectFour());
        }
    }

    @Test
    public void diagonalWinDetected() {
        for (ConnectFourBoard board : boardsUnderTest()) {
            playDiagonalWin(board, 'X');
            assertTrue(label(board, "should detect diagonal win"), board.hasConnectFour());
        }
    }

    @Test
    public void antiDiagonalWinDetected() {
        for (ConnectFourBoard board : boardsUnderTest()) {
            playAntiDiagonalWin(board, 'O');
            assertTrue(label(board, "should detect anti-diagonal win"), board.hasConnectFour());
        }
    }

    @Test
    public void almostFourInARowIsNotWin() {
        for (ConnectFourBoard board : boardsUnderTest()) {
            board.dropDisc(0, 'X');
            board.dropDisc(1, 'X');
            board.dropDisc(2, 'X');
            assertFalse(label(board, "three in a row is not a win"), board.hasConnectFour());
        }
    }

    @Test
    public void horizontalWinDetectedInMiddleRows() {
        for (ConnectFourBoard board : boardsUnderTest()) {
            playMidRowHorizontalWin(board, 'X');
            assertTrue(label(board, "should detect mid-row horizontal win"), board.hasConnectFour());
        }
    }

    @Test
    public void diagonalWinThroughCenterDetected() {
        for (ConnectFourBoard board : boardsUnderTest()) {
            playCenteredDiagonalWin(board, 'O');
            assertTrue(label(board, "should detect centered diagonal win"), board.hasConnectFour());
        }
    }

    @Test
    public void horizontalGapFillCompletesWin() {
        for (ConnectFourBoard board : boardsUnderTest()) {
            board.dropDisc(0, 'X');
            board.dropDisc(1, 'X');
            board.dropDisc(3, 'X');
            assertFalse(label(board, "gap should prevent horizontal win"), board.hasConnectFour());
            board.dropDisc(2, 'X');
            assertTrue(label(board, "filling gap should create horizontal win"), board.hasConnectFour());
        }
    }

    @Test
    public void multiDirectionWinDetected() {
        for (ConnectFourBoard board : boardsUnderTest()) {
            playDualLineWin(board, 'X');
            assertTrue(label(board, "should detect simultaneous line win"), board.hasConnectFour());
        }
    }

    @Test
    public void resetClearsBoardState() {
        for (ConnectFourBoard board : boardsUnderTest()) {
            playVerticalWin(board, 'X');
            assertTrue(label(board, "sanity check win before reset"), board.hasConnectFour());

            board.reset();

            assertFalse(label(board, "no win after reset"), board.hasConnectFour());
            assertEquals(label(board, "lastRow reset"), -1, board.getLastRow());
            assertEquals(label(board, "lastCol reset"), -1, board.getLastCol());
            assertEquals(label(board, "cells cleared after reset"), ' ', board.getCell(0, 0));
        }
    }

    private String label(ConnectFourBoard board, String message) {
        return board.getClass().getSimpleName() + ": " + message;
    }

    private void playHorizontalWin(ConnectFourBoard b, char symbol) {
        for (int c = 0; c < 4; c++) {
            b.dropDisc(c, symbol);
        }
    }

    private void playMidRowHorizontalWin(ConnectFourBoard b, char symbol) {
        char filler = other(symbol);
        for (int c = 0; c < 4; c++) {
            b.dropDisc(c, filler);
            b.dropDisc(c, filler);
            b.dropDisc(c, symbol);
        }
    }

    private void playVerticalWin(ConnectFourBoard b, char symbol) {
        for (int i = 0; i < 4; i++) {
            b.dropDisc(0, symbol);
        }
    }

    private void playDiagonalWin(ConnectFourBoard b, char symbol) {
        char filler = other(symbol);

        b.dropDisc(0, symbol);

        b.dropDisc(1, filler);
        b.dropDisc(1, symbol);

        b.dropDisc(2, filler);
        b.dropDisc(2, filler);
        b.dropDisc(2, symbol);

        b.dropDisc(3, filler);
        b.dropDisc(3, filler);
        b.dropDisc(3, filler);
        b.dropDisc(3, symbol);
    }

    private void playCenteredDiagonalWin(ConnectFourBoard b, char symbol) {
        char filler = other(symbol);

        b.dropDisc(1, symbol);

        b.dropDisc(2, filler);
        b.dropDisc(2, symbol);

        b.dropDisc(3, filler);
        b.dropDisc(3, filler);
        b.dropDisc(3, symbol);

        b.dropDisc(4, filler);
        b.dropDisc(4, filler);
        b.dropDisc(4, filler);
        b.dropDisc(4, symbol);
    }

    private void playAntiDiagonalWin(ConnectFourBoard b, char symbol) {
        char filler = other(symbol);

        b.dropDisc(3, symbol);

        b.dropDisc(2, filler);
        b.dropDisc(2, symbol);

        b.dropDisc(1, filler);
        b.dropDisc(1, filler);
        b.dropDisc(1, symbol);

        b.dropDisc(0, filler);
        b.dropDisc(0, filler);
        b.dropDisc(0, filler);
        b.dropDisc(0, symbol);
    }

    private void playRaisedVerticalWin(ConnectFourBoard b, char symbol) {
        char filler = other(symbol);
        int column = 3;

        b.dropDisc(column, filler);
        b.dropDisc(column, filler);

        for (int i = 0; i < 4; i++) {
            b.dropDisc(column, symbol);
        }
    }

    private void playDualLineWin(ConnectFourBoard b, char symbol) {
        char filler = other(symbol);

        for (int c = 0; c < 3; c++) {
            for (int i = 0; i < 3; i++) {
                b.dropDisc(c, filler);
            }
            b.dropDisc(c, symbol);
        }

        b.dropDisc(3, symbol);
        b.dropDisc(3, symbol);
        b.dropDisc(3, symbol);
        b.dropDisc(3, symbol);
    }

    private char other(char symbol) {
        return symbol == 'X' ? 'O' : 'X';
    }
}
