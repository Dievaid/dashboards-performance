package app;

import java.util.Arrays;

import io.prometheus.client.Counter;
import io.prometheus.client.exporter.HTTPServer;

import java.time.Instant;
import java.time.Duration;

public class Main {
    final int N = 32;

    void printSolution(int[][] board)
    {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == 1)
                    System.out.print("Q ");
                else
                    System.out.print(". ");
            }
            System.out.println();
        }
    }
    boolean isSafe(int[][] board, int row, int col)
    {
        int i, j;
        for (i = 0; i < col; i++)
            if (board[row][i] == 1)
                return false;
        for (i = row, j = col; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 1)
                return false;
        for (i = row, j = col; j >= 0 && i < N; i++, j--)
            if (board[i][j] == 1)
                return false;
        return true;
    }

    boolean solveNQUtil(int[][] board, int col)
    {
        if (col >= N)
            return true;
        for (int i = 0; i < N; i++) {
            if (isSafe(board, i, col)) {

                board[i][col] = 1;

                if (solveNQUtil(board, col + 1))
                    return true;
                board[i][col] = 0;
            }
        }
        return false;
    }
    boolean solveNQ()
    {
        Instant start = Instant.now();
        int[][] board = new int[N][N];
        for (int[] row: board)
            Arrays.fill(row, 0);

        if (!solveNQUtil(board, 0)) {
            System.out.print("Solution does not exist");
            return false;
        }
        Instant end = Instant.now();

        System.out.println("Time elapsed " + Duration.between(start, end));

        // printSolution(board);
        return true;
    }
    public static void main(String args[])
    {
        Main Queen = new Main();

        try (HTTPServer server = new HTTPServer(8080)) {
            Counter requests = Counter.build().name("nqueen_solved_java").help("The number of times the problem was solved").register();
            while (true) {
                Queen.solveNQ();
                requests.inc();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

