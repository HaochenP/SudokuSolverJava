package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SudokuBoard {
    private int[][] board;

    public SudokuBoard(int[][] board) {
        this.board = board;
    }

    public int[][] getBoard() {
        return this.board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }


    private static boolean checkNumber(int[][] sudoku, int row, int column, int number) {
        int[] sudoku_row = sudoku[row];
        int[] sudoku_column = new int[9];
        for (int i = 0; i < 9; i++) {
            if (i == row) {
                continue;
            }
            sudoku_column[i] = sudoku[i][column];

        }
        ;
        int[] sudoku_square = new int[9];
        int square_row = row / 3;
        int square_column = column / 3;
        int square_index = 0;
        for (int i = square_row * 3; i < square_row * 3 + 3; i++) {
            for (int j = square_column * 3; j < square_column * 3 + 3; j++) {
                if (i == row && j == column) {
                    continue;
                }
                sudoku_square[square_index] = sudoku[i][j];
                square_index++;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (sudoku_row[i] == number && i != column || sudoku_column[i] == number || sudoku_square[i] == number) {
                return false;
            }
        }
        return true;
    }

    private static ArrayList<Integer> availableNumbers(int[][] sudoko, int row, int column) {
        ArrayList<Integer> availableNumbers = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            // System.out.printf("row: %d, column: %d, number: %d\n", row, column, i);
            if (checkNumber(sudoko, row, column, i)) {

                availableNumbers.add(i);
            }
        }
        return availableNumbers;

    }

    public static int[] nextEmpty(int[][] sudoku) {
        int[] empty = { -1, -1 };
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j] == 0) {
                    empty[0] = i;
                    empty[1] = j;
                    return empty;
                }
            }
        }
        return empty;
    }

    public static boolean initialCheck(int[][] sudoku) {

        long start = System.currentTimeMillis();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (sudoku[i][j] != 0) {
                    if (!checkNumber(sudoku, i, j, sudoku[i][j])) {
                        return false;
                    }
                }
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + "ms");
        // return true;
        long start1 = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(9);
        ArrayList<Callable<Boolean>> tasks = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            final int row = i;
            tasks.add(() -> checkRow(sudoku, row));
        }
        try {
            List<Future<Boolean>> results = executor.invokeAll(tasks);
            for (Future<Boolean> result : results) {
                if (!result.get()) {
                    return false;
                }
            }
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        } finally {
            executor.shutdown();
            long end1 = System.currentTimeMillis();
            System.out.println("Time taken: " + (end1 - start1) + "ms");
        }
    }

    private static boolean checkRow(int[][] sudoku, int row) {
        for (int j = 0; j < 9; j++) {
            if (sudoku[row][j] != 0) {
                if (!checkNumber(sudoku, row, j, sudoku[row][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    public int[][] sudokuSolver() {

        int[][] sudoku = this.board;
        int[] empty = nextEmpty(sudoku);
        if (empty[0] == -1) {
            return sudoku;
        }
        int row = empty[0];
        int column = empty[1];
        ArrayList<Integer> availableNumbers = availableNumbers(sudoku, row, column);
        for (int number : availableNumbers) {
            sudoku[row][column] = number;
            int[][] solvedSudoku = sudokuSolver();
            if (solvedSudoku != null) {
                return solvedSudoku;
            }
            sudoku[row][column] = 0;
        }
        return null;

    }
}
