package info.masterfrog.labyrinth.core.level;

import com.google.common.collect.ImmutableSet;
import info.masterfrog.pixelcat.engine.exception.GameErrorCode;
import info.masterfrog.pixelcat.engine.exception.GameException;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.exception.TerminalGameException;
import info.masterfrog.pixelcat.engine.kernel.KernelState;
import info.masterfrog.pixelcat.engine.logic.clock.GameClock;
import info.masterfrog.pixelcat.engine.logic.clock.GameClockFactory;
import info.masterfrog.pixelcat.engine.logic.clock.SimpleGameClock;

public class StartScreenLevelManager {
    private static StartScreenLevelManager instance;

    private SimpleGameClock startScreenClock;
    private String state;

    private static final String STATE__WARM_UP = "STATE__WARM_UP";
    private static final String STATE__DEV_LOGO = "STATE__DEV_LOGO";
    private static final String STATE__DEV_LOGO_COOL_DOWN = "STATE__DEV_LOGO_COOL_DOWN";
    private static final String STATE__TITLE = "STATE__TITLE";
    private static final String STATE__TITLE_COOL_DOWN = "STATE__TITLE_COOL_DOWN";
    private static final String STATE__MAIN_SCREEN = "STATE__MAIN_SCREEN";

    private StartScreenLevelManager() {
        state = STATE__WARM_UP;
        startScreenClock = GameClockFactory.getInstance().createSimpleGameClock();
    }

    public static StartScreenLevelManager getInstance() {
        if (instance == null) {
            instance = new StartScreenLevelManager();
        }

        return instance;
    }

    public void run(KernelState kernelState) throws GameException {
        switch (state) {
            case STATE__WARM_UP:
                // transition to DEV_LOGO after 5s
                if (GameClock.toMS(startScreenClock.getElapsed()) > 5000) {
                    state = STATE__DEV_LOGO;
                }
                break;
            case STATE__DEV_LOGO:
                // transition to DEV_LOGO_COOL_DOWN after 10s (15s = 5s + 10s)
                if (GameClock.toMS(startScreenClock.getElapsed()) > 15000) {
                    state = STATE__DEV_LOGO_COOL_DOWN;
                }
                break;
            case STATE__DEV_LOGO_COOL_DOWN:
                // transition to TITLE after 5s (20s = 15s + 5s)
                if (GameClock.toMS(startScreenClock.getElapsed()) > 20000) {
                    state = STATE__DEV_LOGO_COOL_DOWN;
                }
                break;
            case STATE__TITLE:
                // transition to TITLE_COOL_DOWN after 10s (30s = 20s + 10s)
                if (GameClock.toMS(startScreenClock.getElapsed()) > 30000) {
                    state = STATE__DEV_LOGO_COOL_DOWN;
                }
                break;
            case STATE__TITLE_COOL_DOWN:
                // transition to MAIN_SCREEN after 5s (35s = 30s + 5s)
                if (GameClock.toMS(startScreenClock.getElapsed()) > 35000) {
                    state = STATE__DEV_LOGO_COOL_DOWN;
                }
                break;
            case STATE__MAIN_SCREEN:

                break;
            default:
                throw new TerminalErrorException(ImmutableSet.of(new TerminalGameException(GameErrorCode.LOGIC_ERROR, "Invalid start screen state")));
        }
    }
}
