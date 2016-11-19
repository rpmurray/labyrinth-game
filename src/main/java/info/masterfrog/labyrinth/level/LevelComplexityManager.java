package info.masterfrog.labyrinth.level;

import info.masterfrog.labyrinth.enumeration.LevelHandle;
import info.masterfrog.labyrinth.exception.LabyrinthGameErrorCode;
import info.masterfrog.labyrinth.level.model.LevelComplexity;
import info.masterfrog.pixelcat.engine.common.util.ListBuilder;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;

import java.util.ArrayList;
import java.util.List;

public class LevelComplexityManager {
    private static LevelStateManager levelStateManager = LevelStateManager.getInstance();
    private static LevelComplexityManager instance;

    private LevelComplexityManager() {
        // do nothing
    }

    public static LevelComplexityManager getInstance() {
        if (instance == null) {
            instance = new LevelComplexityManager();
        }

        return instance;
    }

    public int getRoomCount() throws TransientGameException {
        // look up labyrinth index
        int labyrinthIndex = levelStateManager.getCurrentLabyrinthIndex();

        // validation
        if (labyrinthIndex < 0 || getComplexityDefinition().size() < labyrinthIndex) {
            throw new TransientGameException(LabyrinthGameErrorCode.LEVEL__LABYRINTH__INVALID_STATE);
        }

        // look up mapping
        int roomCount = getComplexityDefinition().get(labyrinthIndex).getRoomCount();

        return roomCount;
    }

    public int getRoomPuzzleScale() throws TransientGameException {
        // look up labyrinth index
        int labyrinthIndex = levelStateManager.getCurrentLabyrinthIndex();

        // validation
        if (labyrinthIndex < 0 || getComplexityDefinition().size() < labyrinthIndex) {
            throw new TransientGameException(LabyrinthGameErrorCode.LEVEL__LABYRINTH__INVALID_STATE);
        }

        // look up mapping
        int puzzleScale = getComplexityDefinition().get(labyrinthIndex).getPuzzleScale();

        return puzzleScale;
    }

    private List<LevelComplexity> getComplexityDefinition() throws TransientGameException {
        List<LevelComplexity> complexityDefinition = new ListBuilder<ArrayList, LevelComplexity>(ArrayList.class).add(
            new LevelComplexity(1, 0) // index 0 -- init level
        ).add(
            new LevelComplexity(3, 0) // index 1 -- intro to graphs
        ).add(
            new LevelComplexity(3, 3) // index 2 -- intro to puzzles
        //== bracket I -- 2x [a(n)] g(q), p(r) | 2x [b(n)] g(q + 1), p(r) | => [a(n + 1)]: g(q + 1), p(r + 2) ==//
        // n = 1, q = 3, r = 3
        ).add(
            new LevelComplexity(3, 3) // index 3 -- {g(q), p(r)} -- level progression starts
        ).add(
            new LevelComplexity(3, 3) // index 4 -- {g(q), p(r)}
        ).add(
            new LevelComplexity(4, 3) // index 5 -- {g(q + 1), p(r)}
        ).add(
            new LevelComplexity(4, 3) // index 6 -- {g(q + 1), p(r)}
        // n = 2, q = 4, r = 5
        ).add(
            new LevelComplexity(4, 5) // index 7
        ).add(
            new LevelComplexity(4, 5) // index 8
        ).add(
            new LevelComplexity(5, 5) // index 9
        ).add(
            new LevelComplexity(5, 5) // index 10
        // n = 3, q = 5, r = 7
        ).add(
            new LevelComplexity(5, 7) // index 11
        ).add(
            new LevelComplexity(5, 7) // index 12
        ).add(
            new LevelComplexity(6, 7) // index 13
        ).add(
            new LevelComplexity(6, 7) // index 14
        // n = 4, q = 6, r = 9
        ).add(
            new LevelComplexity(6, 9) // index 15
        ).add(
            new LevelComplexity(6, 9) // index 16
        ).add(
            new LevelComplexity(7, 9) // index 17
        ).add(
            new LevelComplexity(7, 9) // index 18
        //== bracket II -- 1x [a(n)] g(q), p(r) | 1x [b(n)] g(q + 2), p(r) | => [a(n + 1)]: g(q + 4), p(r + 2) ==//
        // n = 5, q = 8, r = 11
        ).add(
            new LevelComplexity(8, 11) // index 19
        ).add(
            new LevelComplexity(10, 11) // index 20
        // n = 6, q = 12, r = 13
        ).add(
            new LevelComplexity(12, 13) // index 21
        ).add(
            new LevelComplexity(14, 13) // index 22
        //== bracket III -- 1x [a(n)] g(q), p(r) ==//
        // n = 7, q = 16, r = 15
        ).add(
            new LevelComplexity(16, 15) // index 23
        ).get();

        return complexityDefinition;
    }
}
