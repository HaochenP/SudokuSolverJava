package com.example.demo;

public interface SudokuService {
    int [][] solve(int [][] board);

    boolean isValid(int [][] board);
}
