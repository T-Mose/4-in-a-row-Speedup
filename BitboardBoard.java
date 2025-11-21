import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Connect-four board that keeps per-player bitboards for fast win detection.
 * For small boards (up to 64 cells) we also maintain packed 64-bit masks to
 * leverage inexpensive bit operations.
 */
public class BitboardBoard extends ConnectFourBoard {

    private final boolean usePacked;
    private long packedX;
    private long packedO;

    private Map<Character, BitSet> bitBoards;

    public BitboardBoard(int rows, int columns) {
        super(rows, columns);
        this.usePacked = rows * columns <= Long.SIZE;
    }

    @Override
    public void reset() {
        super.reset();
        packedX = 0L;
        packedO = 0L;
        if (bitBoards == null) {
            bitBoards = new HashMap<>();
        } else {
            bitBoards.clear();
        }
    }

    @Override
    public int dropDisc(int column, char symbol) {
        int row = super.dropDisc(column, symbol);
        if (row >= 0) {
            int idx = index(row, column);
            if (usePacked) {
                updatePacked(symbol, idx, true);
            }
            bitBoardFor(symbol).set(idx);
        }
        return row;
    }

    @Override
    public boolean hasConnectFour() {
        if (lastRow < 0 || lastCol < 0) {
            return false;
        }

        char symbol = grid[lastRow][lastCol];
        if (symbol == ' ') {
            return false;
        }

        if (usePacked && (symbol == 'X' || symbol == 'O')) {
            long bits = symbol == 'X' ? packedX : packedO;
            return hasPackedConnectFour(bits, lastRow, lastCol);
        }

        BitSet bits = bitBoards.get(symbol);
        if (bits == null) {
            return false;
        }

        return hasBitSetConnectFour(bits, lastRow, lastCol);
    }

    private boolean hasPackedConnectFour(long bits, int row, int col) {
        int horizontal = 1 + countPacked(bits, row, col, 0, 1)
                           + countPacked(bits, row, col, 0, -1);
        if (horizontal >= 4) return true;

        int vertical = 1 + countPacked(bits, row, col, 1, 0)
                         + countPacked(bits, row, col, -1, 0);
        if (vertical >= 4) return true;

        int diag1 = 1 + countPacked(bits, row, col, 1, 1)
                      + countPacked(bits, row, col, -1, -1);
        if (diag1 >= 4) return true;

        int diag2 = 1 + countPacked(bits, row, col, 1, -1)
                      + countPacked(bits, row, col, -1, 1);
        return diag2 >= 4;
    }

    private int countPacked(long bits, int row, int col, int dr, int dc) {
        int count = 0;
        int r = row + dr;
        int c = col + dc;
        while (r >= 0 && r < rows && c >= 0 && c < columns) {
            int idx = index(r, c);
            if (((bits >>> idx) & 1L) == 0L) {
                break;
            }
            count++;
            r += dr;
            c += dc;
        }
        return count;
    }

    private boolean hasBitSetConnectFour(BitSet bits, int row, int col) {
        int horizontal = 1 + countBitSet(bits, row, col, 0, 1)
                           + countBitSet(bits, row, col, 0, -1);
        if (horizontal >= 4) return true;

        int vertical = 1 + countBitSet(bits, row, col, 1, 0)
                         + countBitSet(bits, row, col, -1, 0);
        if (vertical >= 4) return true;

        int diag1 = 1 + countBitSet(bits, row, col, 1, 1)
                      + countBitSet(bits, row, col, -1, -1);
        if (diag1 >= 4) return true;

        int diag2 = 1 + countBitSet(bits, row, col, 1, -1)
                      + countBitSet(bits, row, col, -1, 1);
        return diag2 >= 4;
    }

    private int countBitSet(BitSet bits, int row, int col, int dr, int dc) {
        int count = 0;
        int r = row + dr;
        int c = col + dc;
        while (r >= 0 && r < rows && c >= 0 && c < columns) {
            int idx = index(r, c);
            if (!bits.get(idx)) {
                break;
            }
            count++;
            r += dr;
            c += dc;
        }
        return count;
    }

    private BitSet bitBoardFor(char symbol) {
        if (bitBoards == null) {
            bitBoards = new HashMap<>();
        }
        BitSet bits = bitBoards.get(symbol);
        if (bits == null) {
            bits = new BitSet(rows * columns);
            bitBoards.put(symbol, bits);
        }
        return bits;
    }

    private void updatePacked(char symbol, int idx, boolean set) {
        long mask = 1L << idx;
        if (symbol == 'X') {
            if (set) packedX |= mask;
            else packedX &= ~mask;
        } else if (symbol == 'O') {
            if (set) packedO |= mask;
            else packedO &= ~mask;
        } else {
            // No packed representation for other symbols.
        }
    }

    private int index(int row, int col) {
        return row * columns + col;
    }
}

