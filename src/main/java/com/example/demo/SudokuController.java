package com.example.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class SudokuController {
    
    private final SudokuService sudokuService;
    private final GetSudokuBoardApiService getSudokuBoardApiService;
    @Autowired
    public SudokuController(SudokuService sudokuService, GetSudokuBoardApiService getSudokuBoardApiService) {
        this.sudokuService = sudokuService;
        this.getSudokuBoardApiService = getSudokuBoardApiService;
    }

    @PostMapping("/solve")
    public ResponseEntity<int[][]> solve(@RequestBody int[][] board) {
        if(board == null) {
            return ResponseEntity.badRequest().build();
        }
        int[][] solvedBoard = sudokuService.solve(board);
        if (solvedBoard == null) {
            return ResponseEntity.badRequest().build();
        }
        
        return ResponseEntity.ok(solvedBoard);
    }

    @GetMapping("/getBoard")
    public ResponseEntity<int[][]> getBoard() {
        int[][] board = new int[9][9];
        board = getSudokuBoardApiService.fetchSudokuBoard("https://sudoku-api.vercel.app/api/dosuku").block();
        return ResponseEntity.ok(board);
    }


    @GetMapping("/getsolvedsudoku")
    public ResponseEntity<int[][]> getSolvedSudoku() {
        int[][] board = new int[9][9];
        board = getSudokuBoardApiService.fetchSudokuBoard(null).block();
        if(board == null){
            return ResponseEntity.badRequest().build();
        }
        int[][] solvedBoard = sudokuService.solve(board);
        if (solvedBoard == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(solvedBoard);
    }
}
