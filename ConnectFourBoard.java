// ConnectFourBoard.java
public abstract class ConnectFourBoard {

    protected final int rows;
    protected final int columns;
    protected final char[][] grid;

    // Last move (used by optimized versions)
    protected int lastRow = -1;
    protected int lastCol = -1;

    public ConnectFourBoard(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            throw new IllegalArgumentException("rows and columns must be positive");
        }
        this.rows = rows;
        this.columns = columns;
        this.grid = new char[rows][columns];
        reset();
    }

    /** Sets all cells to empty and clears last move info. */
    public void reset() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                grid[r][c] = ' ';
            }
        }
        lastRow = -1;
        lastCol = -1;
    }

    /**
     * Drops a disc into the given column.
     * @param column 0-based column index
     * @param symbol player symbol, e.g. 'X' or 'O'
     * @return row index where disc landed, or -1 if column is full
     */
    public int dropDisc(int column, char symbol) {
        if (column < 0 || column >= columns) {
            throw new IllegalArgumentException("Invalid column: " + column);
        }
        if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol cannot be space");
        }

        for (int r = rows - 1; r >= 0; r--) {
            if (grid[r][column] == ' ') {
                grid[r][column] = symbol;
                lastRow = r;
                lastCol = column;
                return r;
            }
        }
        return -1; // column full
    }

    /** True if the board has no empty cells left. */
    public boolean isFull() {
        for (int c = 0; c < columns; c++) {
            if (grid[0][c] == ' ') {
                return false;
            }
        }
        return true;
    }

    /** Subclasses implement their own win check logic. */
    public abstract boolean hasConnectFour();

    // --- Accessors (handy for tests / demos / advanced variants) ---

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getLastRow() {
        return lastRow;
    }

    public int getLastCol() {
        return lastCol;
    }

    public char getCell(int row, int col) {
        return grid[row][col];
    }

    public void printBoard() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                System.out.print("[" + grid[r][c] + "]");
            }
            System.out.println();
        }
        System.out.print(" ");
        for (int c = 0; c < columns; c++) {
            System.out.print(" " + c + " ");
        }
        System.out.println();
    }
}
