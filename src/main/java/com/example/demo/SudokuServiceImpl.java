package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class SudokuServiceImpl implements SudokuService{

    @Override
    public int [][] solve(int [][] board) {
        SudokuBoard sudokuBoard = new SudokuBoard(board);
        if (!SudokuBoard.initialCheck(board)){
            return null;
        }
        int[][] solvedBoard = sudokuBoard.sudokuSolver();
        return solvedBoard;
    }
}
