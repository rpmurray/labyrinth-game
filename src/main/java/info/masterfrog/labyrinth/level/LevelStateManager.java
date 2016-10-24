package info.masterfrog.labyrinth.level;

import info.masterfrog.labyrinth.enumeration.LevelHandle;
import info.masterfrog.pixelcat.engine.common.util.MapBuilder;

import java.util.HashMap;
import java.util.Map;

public class LevelStateManager {
    private LevelHandle currentLevel;

    public static final String START_SCREEN = "start screen";
    public static final String LEVEL_ONE = "level one";

    private static final Map<LevelHandle, LevelHandle> LEVEL_TRANSITIONS = new MapBuilder<HashMap, LevelHandle, LevelHandle>(
        HashMap.class, System.out, false
    ).add(
        LevelHandle.START_SCREEN, LevelHandle.LABYRINTH
    ).get();


    public LevelStateManager(LevelHandle currentLevel) {
        this.currentLevel = currentLevel;
    }

    public LevelHandle getCurrentLevel() {
        return currentLevel;
    }

    public LevelHandle getNextLevel() {
        // check for level transition
        if (!LEVEL_TRANSITIONS.containsKey(currentLevel)) {
            return null;
        }

        // update current level
        currentLevel = LEVEL_TRANSITIONS.get(currentLevel);

        return currentLevel;
    }
}
