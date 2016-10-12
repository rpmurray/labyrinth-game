package info.masterfrog.labyrinth.main;

import info.masterfrog.labyrinth.level.puzzle.SlidePuzzle;

import java.util.Arrays;

public class SlidePuzzleDemo {
    public static void main(String arg[]) {
        // setup
        SlidePuzzle slidePuzzle;

        // generate slide puzzles
        for (int i = 3; i < 10; i += 2) {
            System.out.println("===" + i + "x"+ i + " Slide Puzzles===");
            for (int j = 0; j < 10; j++) {
                System.out.println("Puzzle #" + (j + 1) + ":");
                slidePuzzle = new SlidePuzzle(i, i);
                System.out.println("Solved     = " + Arrays.toString(slidePuzzle.getTiles()));
                slidePuzzle.randomize();
                System.out.println("Randomized = " + Arrays.toString(slidePuzzle.getTiles()));
            }
        }
    }
}
