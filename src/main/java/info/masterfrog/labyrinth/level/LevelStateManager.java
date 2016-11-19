package info.masterfrog.labyrinth.level;

import info.masterfrog.labyrinth.enumeration.LevelHandle;
import info.masterfrog.labyrinth.exception.LabyrinthGameErrorCode;
import info.masterfrog.pixelcat.engine.common.util.MapBuilder;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;

import java.util.HashMap;
import java.util.Map;

public class LevelStateManager {
    private LevelHandle currentLevel;
    private Integer currentLabyrinthIndex;

    private static LevelStateManager instance;

    public static LevelStateManager getInstance() {
        if (instance == null) {
            instance = new LevelStateManager();
        }

        return instance;
    }

    private LevelStateManager() {
        // do nothing
    }

    public void init(LevelHandle currentLevel, int currentLabyrinthIndex) {
        this.currentLevel = currentLevel;
        this.currentLabyrinthIndex = currentLabyrinthIndex;
    }

    public LevelHandle getCurrentLevel() {
        return currentLevel;
    }

    public LevelManager getCurrentLevelManager() throws TransientGameException {
        LevelManager levelManager;
        switch (currentLevel) {
            case START_SCREEN:
                levelManager = StartScreenLevelManager.getInstance();
                break;
            case LABYRINTH:
                levelManager = LabyrinthLevelManager.getInstance();
                break;
            default:
                throw new TransientGameException(LabyrinthGameErrorCode.LEVEL__CORE__UNDEFINED_LEVEL);
        }

        return levelManager;
    }

    public LevelHandle getNextLevel() {
        // check for level transition
        switch (currentLevel) {
            case START_SCREEN:
                currentLevel = LevelHandle.LABYRINTH;
                currentLabyrinthIndex = 1;
                break;
            case LABYRINTH:
                currentLabyrinthIndex++;
        }

        return currentLevel;
    }

    public Integer getCurrentLabyrinthIndex() {
        return currentLabyrinthIndex;
    }
}
