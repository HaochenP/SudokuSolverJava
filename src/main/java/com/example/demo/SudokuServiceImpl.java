package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class SudokuServiceImpl implements SudokuService{

    @Override
    public int [][] solve(int [][] board) {
        SudokuBoard sudokuBoard = new SudokuBoard(board);
        if (!sudokuBoard.isValidBoard( false)){
            return null;
        }
        int[][] solvedBoard = sudokuBoard.sudokuSolver();
        return solvedBoard;
    }

    @Override
    public boolean isValid(int [][] board) {
        SudokuBoard sudokuBoard = new SudokuBoard(board);
        return sudokuBoard.isValidBoard(false);
    }
}
