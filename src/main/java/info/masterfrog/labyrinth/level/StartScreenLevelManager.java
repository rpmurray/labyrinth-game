package info.masterfrog.labyrinth.level;

import com.google.common.collect.ImmutableSet;
import info.masterfrog.labyrinth.entity.EntitiesEnvironmentManager;
import info.masterfrog.labyrinth.enumeration.EntitiesManagerHandle;
import info.masterfrog.labyrinth.enumeration.EntityHandle;
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
    private SimpleGameClock timer;
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
        timer = GameClockFactory.getInstance().createSimpleGameClock();
    }

    public static StartScreenLevelManager getInstance() {
        if (instance == null) {
            instance = new StartScreenLevelManager();
        }

        return instance;
    }

    public void run(KernelState kernelState, LevelManager levelManager, EntitiesEnvironmentManager entitiesEnvironmentManager)
                throws TransientGameException, TerminalErrorException {
        switch (state) {
            case STATE__INIT:
                // do init work
                entitiesEnvironmentManager.getEntity(
                    EntityHandle.START_SCREEN__BACKGROUND
                ).getFeature(
                    SoundLibrary.class
                ).getCurrent().play();

                // transition to WARM_UP
                state = STATE__WARM_UP;

                // reset timer
                timer.reset();
                break;
            case STATE__WARM_UP:
                // transition to DEV_LOGO after 5s
                if (GameClock.ns2s(timer.getElapsed()) > 5) {
                    // update state
                    state = STATE__DEV_LOGO;

                    // do transition work
                    entitiesEnvironmentManager.getEntitiesManager(
                        EntitiesManagerHandle.START_SCREEN
                    ).add(
                        entitiesEnvironmentManager.getEntity(
                            EntityHandle.START_SCREEN__DEV_LOGO
                        )
                    );

                    // reset timer
                    timer.reset();
                }
                break;
            case STATE__DEV_LOGO:
                // transition to DEV_LOGO_COOL_DOWN after 10s
                if (GameClock.ns2s(timer.getElapsed()) > 10) {
                    state = STATE__DEV_LOGO_COOL_DOWN;

                    // do transition work
                    entitiesEnvironmentManager.getEntitiesManager(
                        EntitiesManagerHandle.START_SCREEN
                    ).remove(
                        entitiesEnvironmentManager.getEntity(
                            EntityHandle.START_SCREEN__DEV_LOGO
                        ).getId()
                    );

                    // reset timer
                    timer.reset();
                }
                break;
            case STATE__DEV_LOGO_COOL_DOWN:
                // transition to TITLE after 5s
                if (GameClock.ns2s(timer.getElapsed()) > 5) {
                    state = STATE__DEV_LOGO_COOL_DOWN;

                    // do transition work
                    entitiesEnvironmentManager.getEntitiesManager(
                        EntitiesManagerHandle.START_SCREEN
                    ).add(
                        entitiesEnvironmentManager.getEntity(
                            EntityHandle.START_SCREEN__TITLE_TEXT
                        )
                    );

                    // reset timer
                    timer.reset();
                }
                break;
            case STATE__TITLE:
                // transition to TITLE_COOL_DOWN after 10s
                if (GameClock.ns2s(timer.getElapsed()) > 10) {
                    state = STATE__DEV_LOGO_COOL_DOWN;

                    // do transition work
                    entitiesEnvironmentManager.getEntitiesManager(
                        EntitiesManagerHandle.START_SCREEN
                    ).remove(
                        entitiesEnvironmentManager.getEntity(
                            EntityHandle.START_SCREEN__TITLE_TEXT
                        ).getId()
                    );

                    // reset timer
                    timer.reset();
                }
                break;
            case STATE__TITLE_COOL_DOWN:
                // transition to MAIN_SCREEN after 5s
                if (GameClock.ns2s(timer.getElapsed()) > 5) {
                    state = STATE__DEV_LOGO_COOL_DOWN;

                    // do transition work
                    entitiesEnvironmentManager.getEntity(
                        EntityHandle.START_SCREEN__BACKGROUND
                    ).getFeature(
                        AnimationSequenceLibrary.class
                    ).getCurrent().advanceSequence();

                    // reset timer
                    timer.reset();
                }
                break;
            case STATE__MAIN_SCREEN:
                entitiesEnvironmentManager.setFlag(EntitiesEnvironmentManager.FLAG__START_INITIALIZED, true);
                break;
            default:
                throw new TerminalErrorException(ImmutableSet.of(new TerminalGameException(GameErrorCode.LOGIC_ERROR, "Invalid start screen state")));
        }
    }
}
