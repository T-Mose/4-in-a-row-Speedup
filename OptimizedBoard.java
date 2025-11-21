// OptimizedBoard.java
public class OptimizedBoard extends ConnectFourBoard {

    public OptimizedBoard(int rows, int columns) {
        super(rows, columns);
    }

    /**
     * Optimized win check:
     * - Uses the last move at (lastRow, lastCol).
     * - Looks along 4 lines (horizontal, vertical, 2 diagonals).
     * - Only needs to look up to 3 cells away in each direction (Connect-4).
     *
     * Asymptotically: O(1) work per call.
     */
    @Override
    public boolean hasConnectFour() {
        if (lastRow < 0 || lastCol < 0) {
            // No moves yet
            return false;
        }

        char symbol = grid[lastRow][lastCol];
        if (symbol == ' ') {
            return false;
        }

        // Horizontal: left + right
        if (countDirection(lastRow, lastCol, 0, 1, symbol) +
            countDirection(lastRow, lastCol, 0, -1, symbol) - 1 >= 4) {
            return true;
        }

        // Vertical: up + down
        if (countDirection(lastRow, lastCol, 1, 0, symbol) +
            countDirection(lastRow, lastCol, -1, 0, symbol) - 1 >= 4) {
            return true;
        }

        // Diagonal: down-right + up-left
        if (countDirection(lastRow, lastCol, 1, 1, symbol) +
            countDirection(lastRow, lastCol, -1, -1, symbol) - 1 >= 4) {
            return true;
        }

        // Anti-diagonal: up-right + down-left
        if (countDirection(lastRow, lastCol, -1, 1, symbol) +
            countDirection(lastRow, lastCol, 1, -1, symbol) - 1 >= 4) {
            return true;
        }

        return false;
    }

    /**
     * Count how many same-symbol cells we see starting from (row, col)
     * and walking in direction (dr, dc), including the starting cell.
     * We stop after 3 steps, because for connect-four we never need
     * to look further than 3 cells away.
     */
    private int countDirection(int row, int col, int dr, int dc, char symbol) {
        int count = 0;
        int r = row;
        int c = col;

        for (int step = 0; step < 4; step++) { // max 4 cells including start
            if (r < 0 || r >= rows || c < 0 || c >= columns) break;
            if (grid[r][c] != symbol) break;
            count++;
            r += dr;
            c += dc;
        }
        return count;
    }
}
