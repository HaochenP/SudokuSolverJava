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


    private boolean checkNumber(int[][] sudoku, int row, int column, int number) {
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

    public int[] nextEmpty(int[][] sudoku) {
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

    public boolean isValidBoard(boolean concurrent) {
        if(!concurrent) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (this.board[i][j] != 0) {
                        if (!checkNumber(board, i, j, board[i][j])) {
                            return false;
                        }
                    }
                }
            }
        }
        
        ExecutorService executor = Executors.newFixedThreadPool(9);
        ArrayList<Callable<Boolean>> tasks = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            final int row = i;
            tasks.add(() -> checkRow( row));
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
        }
    }

    private boolean checkRow(int row) {
        for (int i = 0; i < 9; i++) {
            if (this.board[row][i] != 0) {
                if (!checkNumber(this.board, row, i, this.board[row][i])) {
                    return false;
                }
            }
        }
        return true;
    }


    public int[][] sudokuSolver() {
        return solveSudoku(0, 0);
    }

    private int[][] solveSudoku(int row, int col) {
        int[] emptyCell = findNextEmptyCell(this.board, row, col);
        if (emptyCell == null) {
            return this.board;
        }
        int nextRow = emptyCell[0];
        int nextCol = emptyCell[1];

        for (int num = 1; num <= 9; num++) {
            if (isValidPlacement(this.board, nextRow, nextCol, num)) {
                this.board[nextRow][nextCol] = num;
                int[][] solved = solveSudoku(nextRow, nextCol + 1);
                if (solved != null) {
                    return solved;
                }
                this.board[nextRow][nextCol] = 0; 
            }
        }
        return null; 
    }
    private boolean isValidPlacement(int[][] sudoku, int row, int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (sudoku[row][i] == num || sudoku[i][col] == num) {
                return false;
            }
            int gridRow = 3 * (row / 3) + i / 3;
            int gridCol = 3 * (col / 3) + i % 3;
            if (sudoku[gridRow][gridCol] == num) {
                return false;
            }
        }
        return true; 
    }

    private int[] findNextEmptyCell(int[][] sudoku, int startRow, int startCol) {
        for (int row = startRow; row < 9; row++) {
            for (int col = (row == startRow ? startCol : 0); col < 9; col++) {
                if (sudoku[row][col] == 0) {
                    return new int[]{row, col};
                }
            }
        }
        return null; 
    }

}
