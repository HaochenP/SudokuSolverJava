package com.example.demo;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> solve(@RequestBody int[][] board) {
        if (board == null || board.length != 9 || board[0].length != 9) {
            return ResponseEntity.badRequest().body("Invalid board size.");
        }
        int[][] solvedBoard = sudokuService.solve(board);
        if (solvedBoard == null) {
            return ResponseEntity.badRequest().body("Could not solve the board.");
        }

        return ResponseEntity.ok(solvedBoard);
    }

    @GetMapping("/getBoard")
    public ResponseEntity<?> getBoard() {
        try{
            int[][] board = getSudokuBoardApiService.fetchSudokuBoard("https://sudoku-api.vercel.app/api/dosuku").block();
            if(board == null){
                return ResponseEntity.badRequest().body(null);
            }
            return ResponseEntity.ok(board);
        } catch (Exception e){
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching the Sudoku board. Please try again later.");
        }
        
    }

    @GetMapping("/getsolvedsudoku")
    public ResponseEntity<?> getSolvedSudoku() {

        int[][] board = new int[9][9];

        board = getSudokuBoardApiService.fetchSudokuBoard(null).block();

        if (board == null) {
            return ResponseEntity.badRequest().body("Failed to fetch the board.");
        }

        int[][] solvedBoard = sudokuService.solve(board);

        if (solvedBoard == null) {
            return ResponseEntity.badRequest().body("Could not solve the fetched board.");
        }

        return ResponseEntity.ok(solvedBoard);
    }

    @PostMapping("/isValid")
    public ResponseEntity<String> IsValid(@RequestBody int[][] board) {

        if (board == null) {

            return ResponseEntity.badRequest().build();

        }

        boolean isValidPuzzle = sudokuService.isValid(board);

        if (isValidPuzzle == false) {

            return ResponseEntity.ok("Nah, the puzzle you sent is not solved correctly");

        }

        return ResponseEntity.ok("well done buddy, that puzzle was well solved");

    }

    @GetMapping("/getConcurrency")
    public ResponseEntity<String> getConcurrency() {
        ArrayList<int[][]> boards = new ArrayList<>();
        int numberOfBoards = 15;
        for (int i = 0; i < numberOfBoards; i++) {
            boards.add(getSudokuBoardApiService.fetchSudokuBoard("https://sudoku-api.vercel.app/api/dosuku").block());
        }

        long start = System.currentTimeMillis();
        boards.forEach(sudokuService::solve);
        long end = System.currentTimeMillis();

        long startCon = System.currentTimeMillis();
        ForkJoinPool customThreadPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        customThreadPool.submit(() ->
                boards.parallelStream().forEach(sudokuService::solve)).join();
        long endCon = System.currentTimeMillis();

        return ResponseEntity.ok(String.format("Solved total of %d. Time taken to solve all without concurrency: %dms\n" +
                "Time taken with concurrency: %dms", numberOfBoards, (end - start), (endCon - startCon)));
    }
}
