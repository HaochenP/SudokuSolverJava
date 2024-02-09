package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class SudokuResponse {
    @JsonProperty("newboard") 
    private NewBoard newBoard;

    public static class NewBoard{
        private List<Grid> grids;
        private int result;
        private String message;

        // Getters and Setters
        public List<Grid> getGrids() {
            return grids;
        }

        public void setGrids(List<Grid> grids) {
            this.grids = grids;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class Grid{
        @JsonProperty("value")
        List<List<Integer>> value;
        @JsonProperty("solution")
        List<List<Integer>> solution;
        @JsonProperty("difficulty")
        String difficulty;


        public List<List<Integer>> getValue() {
            return value;
        }

        public void setValue(List<List<Integer>> value) {
            this.value = value;
        }

        public List<List<Integer>> getSolution() {
            return solution;
        }

        public void setSolution(List<List<Integer>> solution) {
            this.solution = solution;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(String difficulty) {
            this.difficulty = difficulty;
        }
    }


    public int[][] toBoard() {
        if (newBoard != null && !newBoard.getGrids().isEmpty()) {
            List<List<Integer>> values = newBoard.getGrids().get(0).getValue();
            return values.stream()
                         .map(list -> list.stream().mapToInt(i -> i).toArray())
                         .toArray(int[][]::new);
        }
        return new int[0][0]; 
    }

    public NewBoard getNewBoard() {
        return newBoard;
    }

    public void setNewBoard(NewBoard newBoard) {
        this.newBoard = newBoard;
    }
}
