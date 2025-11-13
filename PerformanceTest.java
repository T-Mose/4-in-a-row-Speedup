// PerformanceDemo.java

import java.util.Random;

public class PerformanceTest {

    public static void main(String[] args) {
        int rows = 100;
        int columns = 100;
        int games = 1000; // number of random games to simulate

        System.out.println("Board size: " + rows + " x " + columns);
        System.out.println("Simulating " + games + " random games...\n");

        long naiveTime = runBenchmark(new NaiveBoard(rows, columns), games);
        long optTime   = runBenchmark(new Optimized(rows, columns), games);

        System.out.println("Naive total time:      " + naiveTime + " ms");
        System.out.println("Optimized total time:  " + optTime   + " ms");

        double ratio = (optTime == 0) ? Double.POSITIVE_INFINITY
                                      : (double) naiveTime / optTime;

        System.out.printf("Speedup (naive / opt): %.2f x%n", ratio);
    }

    private static long runBenchmark(ConnectFourBoard board, int games) {
        long start = System.currentTimeMillis();
        Random random = new Random(42); // fixed seed so runs are comparable

        for (int g = 0; g < games; g++) {
            board.reset();
            char current = 'X';
            boolean win = false;
            int moves = 0;
            int maxMoves = board.getRows() * board.getColumns();

            while (!win && !board.isFull() && moves < maxMoves) {
                int col = random.nextInt(board.getColumns());
                int row = board.dropDisc(col, current);
                if (row == -1) {
                    // column full, try another column
                    continue;
                }
                win = board.hasConnectFour();
                current = (current == 'X') ? 'O' : 'X';
                moves++;
            }
        }

        long end = System.currentTimeMillis();
        return end - start;
    }
}
