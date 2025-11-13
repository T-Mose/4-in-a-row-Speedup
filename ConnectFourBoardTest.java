// ConnectFourBoardTest.java

import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectFourBoardTest {

    private static final int ROWS = 6;
    private static final int COLUMNS = 7;

    @Test
    public void emptyBoardHasNoWin() {
        ConnectFourBoard naive = new NaiveBoard(ROWS, COLUMNS);
        ConnectFourBoard opt   = new Optimized(ROWS, COLUMNS);

        assertFalse("Naive: empty board should have no win", naive.hasConnectFour());
        assertFalse("Optimized: empty board should have no win", opt.hasConnectFour());
    }

    @Test
    public void horizontalWinDetected() {
        ConnectFourBoard naive = new NaiveBoard(ROWS, COLUMNS);
        ConnectFourBoard opt   = new Optimized(ROWS, COLUMNS);

        playHorizontalWin(naive, 'X');
        playHorizontalWin(opt,   'X');

        assertTrue("Naive: should detect horizontal win", naive.hasConnectFour());
        assertTrue("Optimized: should detect horizontal win", opt.hasConnectFour());
    }

    @Test
    public void verticalWinDetected() {
        ConnectFourBoard naive = new NaiveBoard(ROWS, COLUMNS);
        ConnectFourBoard opt   = new Optimized(ROWS, COLUMNS);

        playVerticalWin(naive, 'O');
        playVerticalWin(opt,   'O');

        assertTrue("Naive: should detect vertical win", naive.hasConnectFour());
        assertTrue("Optimized: should detect vertical win", opt.hasConnectFour());
    }

    @Test
    public void diagonalWinDetected() {
        ConnectFourBoard naive = new NaiveBoard(ROWS, COLUMNS);
        ConnectFourBoard opt   = new Optimized(ROWS, COLUMNS);

        playDiagonalWin(naive, 'X');
        playDiagonalWin(opt,   'X');

        assertTrue("Naive: should detect diagonal win", naive.hasConnectFour());
        assertTrue("Optimized: should detect diagonal win", opt.hasConnectFour());
    }

    @Test
    public void almostFourInARowIsNotWin() {
        ConnectFourBoard naive = new NaiveBoard(ROWS, COLUMNS);
        ConnectFourBoard opt   = new Optimized(ROWS, COLUMNS);

        // Three in a row only
        naive.dropDisc(0, 'X');
        naive.dropDisc(1, 'X');
        naive.dropDisc(2, 'X');

        opt.dropDisc(0, 'X');
        opt.dropDisc(1, 'X');
        opt.dropDisc(2, 'X');

        assertFalse("Naive: three in a row is not a win", naive.hasConnectFour());
        assertFalse("Optimized: three in a row is not a win", opt.hasConnectFour());
    }

    // --- Helper methods to set up scenarios ---

    private void playHorizontalWin(ConnectFourBoard b, char symbol) {
        // Bottom row, columns 0..3
        b.dropDisc(0, symbol);
        b.dropDisc(1, symbol);
        b.dropDisc(2, symbol);
        b.dropDisc(3, symbol);
    }

    private void playVerticalWin(ConnectFourBoard b, char symbol) {
        int col = 0;
        b.dropDisc(col, symbol);
        b.dropDisc(col, symbol);
        b.dropDisc(col, symbol);
        b.dropDisc(col, symbol);
    }

    private void playDiagonalWin(ConnectFourBoard b, char symbol) {
        // (row 5, col 0) X
        // (row 4, col 1) X
        // (row 3, col 2) X
        // (row 2, col 3) X

        // Column 0
        b.dropDisc(0, symbol); // bottom

        // Column 1
        b.dropDisc(1, 'O');
        b.dropDisc(1, symbol); // on top of O

        // Column 2
        b.dropDisc(2, 'O');
        b.dropDisc(2, 'O');
        b.dropDisc(2, symbol); // above two O's

        // Column 3
        b.dropDisc(3, 'O');
        b.dropDisc(3, 'O');
        b.dropDisc(3, 'O');
        b.dropDisc(3, symbol); // above three O's
    }
}
