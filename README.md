SudokuSolverJava
SudokuSolverJava is a high-performance, easy-to-use Java library designed to solve Sudoku puzzles of varying difficulties. Utilizing advanced algorithms, this solver is capable of handling everything from simple puzzles to the most challenging grids. Whether you're integrating Sudoku solving capabilities into your Java applications or looking for a tool to assist with puzzle creation and validation, SudokuSolverJava offers a robust solution.

Features
Fast Solving Algorithm: Quickly solves Sudoku puzzles of any difficulty level.
Easy Integration: Designed as a straightforward library, making it easy to incorporate into any Java project.
Puzzle Validation: Checks if a given Sudoku puzzle is valid and solvable.
Generate Puzzles: Capable of generating Sudoku puzzles with different difficulty levels.
Customizable Solutions: Offers flexibility to customize solving strategies based on specific needs.
Requirements
Java 8 or newer.
Installation
To use SudokuSolverJava in your project, follow these steps:

Clone the repository:
bash
Copy code
git clone https://github.com/HaochenP/SudokuSolverJava.git
Include the library in your Java project. If you're using an IDE, you can add the SudokuSolverJava folder as a library in your project settings.
Usage
Here's a simple example of how to use SudokuSolverJava to solve a Sudoku puzzle:

java
Copy code
import com.yourusername.sudokusolverjava.SudokuSolver;

public class Main {
    public static void main(String[] args) {
        int[][] puzzle = {
            {5,3,0,0,7,0,0,0,0},
            {6,0,0,1,9,5,0,0,0},
            {0,9,8,0,0,0,0,6,0},
            {8,0,0,0,6,0,0,0,3},
            {4,0,0,8,0,3,0,0,1},
            {7,0,0,0,2,0,0,0,6},
            {0,6,0,0,0,0,2,8,0},
            {0,0,0,4,1,9,0,0,5},
            {0,0,0,0,8,0,0,7,9}
        };

        SudokuSolver solver = new SudokuSolver();
        solver.solve(puzzle);

        System.out.println("Solved Puzzle:");
        solver.printSolution();
    }
}

