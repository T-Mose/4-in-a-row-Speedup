// NaiveBoard.java
public class NaiveBoard extends ConnectFourBoard {

    public NaiveBoard(int rows, int columns) {
        super(rows, columns);
    }

    /**
     * Naive win check:
     * - Checks each cell as a potential starting point.
     * - Looks along 4 lines (horizontal, vertical, 2 diagonals).
     * - Only needs to look up to 3 cells away in each direction (Connect-4).
     *
     * Asymptotically: O(rows * columns) work per call.
     */
    @Override
    public boolean hasConnectFour() {
        // Check each cell as a potential starting point
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                char symbol = grid[r][c];
                if (symbol == ' ') {
                    continue;
                }
                // Right
                if (hasRunFrom(r, c, 0, 1, symbol)) return true;
                // Down
                if (hasRunFrom(r, c, 1, 0, symbol)) return true;
                // Down-right
                if (hasRunFrom(r, c, 1, 1, symbol)) return true;
                // Up-right
                if (hasRunFrom(r, c, -1, 1, symbol)) return true;
            }
        }
        return false;
    }

    /**
     * Helper method to check if there is a run of 4 or more same-symbol cells starting from (row, col)
     * and walking in direction (dr, dc).
     * @param row starting row
     * @param col starting column
     * @param dr direction row increment
     * @param dc direction column increment
     * @param symbol the symbol to check for
     * @return true if there is a run of 4 or more same-symbol cells
     */
    private boolean hasRunFrom(int row, int col, int dr, int dc, char symbol) {
        // We need 4 in a row including (row, col)
        for (int k = 0; k < 4; k++) {
            int r = row + k * dr;
            int c = col + k * dc;
            if (r < 0 || r >= rows || c < 0 || c >= columns) {
                return false;
            }
            if (grid[r][c] != symbol) {
                return false;
            }
        }
        return true;
    }
}
