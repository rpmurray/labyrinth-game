package info.masterfrog.labyrinth.core.level;

import com.google.common.collect.ImmutableSet;
import info.masterfrog.labyrinth.core.entity.EntitiesManager;
import info.masterfrog.labyrinth.core.enumeration.EntitiesManagerHandle;
import info.masterfrog.labyrinth.core.enumeration.EntityHandle;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.*;
import info.masterfrog.pixelcat.engine.kernel.KernelState;
import info.masterfrog.pixelcat.engine.logic.clock.GameClock;
import info.masterfrog.pixelcat.engine.logic.clock.GameClockFactory;
import info.masterfrog.pixelcat.engine.logic.clock.SimpleGameClock;
import info.masterfrog.pixelcat.engine.logic.gameobject.feature.AnimationSequenceLibrary;
import info.masterfrog.pixelcat.engine.logic.gameobject.feature.SoundLibrary;

public class StartScreenLevelManager {
    private static StartScreenLevelManager instance;

    private SimpleGameClock startScreenClock;
    private String state;

    private static final String STATE__INIT = "STATE__INIT";
    private static final String STATE__WARM_UP = "STATE__WARM_UP";
    private static final String STATE__DEV_LOGO = "STATE__DEV_LOGO";
    private static final String STATE__DEV_LOGO_COOL_DOWN = "STATE__DEV_LOGO_COOL_DOWN";
    private static final String STATE__TITLE = "STATE__TITLE";
    private static final String STATE__TITLE_COOL_DOWN = "STATE__TITLE_COOL_DOWN";
    private static final String STATE__MAIN_SCREEN = "STATE__MAIN_SCREEN";

    private static final Printer PRINTER = PrinterFactory.getInstance().createPrinter(StartScreenLevelManager.class);

    private StartScreenLevelManager() {
        state = STATE__INIT;
        startScreenClock = GameClockFactory.getInstance().createSimpleGameClock();
    }

    public static StartScreenLevelManager getInstance() {
        if (instance == null) {
            instance = new StartScreenLevelManager();
        }

        return instance;
    }

    public void run(KernelState kernelState, LevelManager levelManager, EntitiesManager entitiesManager)
                throws TransientGameException, TerminalErrorException {
        switch (state) {
            case STATE__INIT:
                // do init work
                entitiesManager.getEntity(
                    EntityHandle.START_SCREEN__BACKGROUND
                ).getFeature(
                    SoundLibrary.class
                ).getCurrent().play();

                // transition to WARM_UP
                state = STATE__WARM_UP;
                break;
            case STATE__WARM_UP:
                // transition to DEV_LOGO after 5s
                if (GameClock.ns2s(startScreenClock.getElapsed()) > 5) {
                    // update state
                    state = STATE__DEV_LOGO;

                    // do transition work
                    entitiesManager.getEntitiesManager(
                        EntitiesManagerHandle.START_SCREEN
                    ).add(
                        entitiesManager.getEntity(
                            EntityHandle.START_SCREEN__DEV_LOGO
                        )
                    );
                }
                break;
            case STATE__DEV_LOGO:
                // transition to DEV_LOGO_COOL_DOWN after 10s (15s = 5s + 10s)
                if (GameClock.ns2s(startScreenClock.getElapsed()) > 15) {
                    state = STATE__DEV_LOGO_COOL_DOWN;

                    // do transition work
                    entitiesManager.getEntitiesManager(
                        EntitiesManagerHandle.START_SCREEN
                    ).remove(
                        entitiesManager.getEntity(
                            EntityHandle.START_SCREEN__DEV_LOGO
                        ).getId()
                    );
                }
                break;
            case STATE__DEV_LOGO_COOL_DOWN:
                // transition to TITLE after 5s (20s = 15s + 5s)
                if (GameClock.ns2s(startScreenClock.getElapsed()) > 20) {
                    state = STATE__DEV_LOGO_COOL_DOWN;

                    // do transition work
                    entitiesManager.getEntitiesManager(
                        EntitiesManagerHandle.START_SCREEN
                    ).add(
                        entitiesManager.getEntity(
                            EntityHandle.START_SCREEN__TITLE_TEXT
                        )
                    );
                }
                break;
            case STATE__TITLE:
                // transition to TITLE_COOL_DOWN after 10s (30s = 20s + 10s)
                if (GameClock.ns2s(startScreenClock.getElapsed()) > 30) {
                    state = STATE__DEV_LOGO_COOL_DOWN;

                    // do transition work
                    entitiesManager.getEntitiesManager(
                        EntitiesManagerHandle.START_SCREEN
                    ).remove(
                        entitiesManager.getEntity(
                            EntityHandle.START_SCREEN__TITLE_TEXT
                        ).getId()
                    );
                }
                break;
            case STATE__TITLE_COOL_DOWN:
                // transition to MAIN_SCREEN after 5s (35s = 30s + 5s)
                if (GameClock.ns2s(startScreenClock.getElapsed()) > 35) {
                    state = STATE__DEV_LOGO_COOL_DOWN;

                    // do transition work
                    entitiesManager.getEntity(
                        EntityHandle.START_SCREEN__BACKGROUND
                    ).getFeature(
                        AnimationSequenceLibrary.class
                    ).getCurrent().advanceSequence();
                }
                break;
            case STATE__MAIN_SCREEN:
                entitiesManager.setFlag(EntitiesManager.FLAG__START_INITIALIZED, true);
                break;
            default:
                throw new TerminalErrorException(ImmutableSet.of(new TerminalGameException(GameErrorCode.LOGIC_ERROR, "Invalid start screen state")));
        }
    }
}
