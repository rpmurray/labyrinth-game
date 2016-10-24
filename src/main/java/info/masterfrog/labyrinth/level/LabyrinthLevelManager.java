package info.masterfrog.labyrinth.level;

import info.masterfrog.labyrinth.entity.EnvironmentManager;
import info.masterfrog.labyrinth.entity.builder.EntitiesBuilder;
import info.masterfrog.labyrinth.enumeration.EntityHandle;
import info.masterfrog.labyrinth.enumeration.EntityManagerHandle;
import info.masterfrog.labyrinth.enumeration.LevelHandle;
import info.masterfrog.labyrinth.enumeration.ResourceHandle;
import info.masterfrog.labyrinth.exception.LabyrinthGameErrorCode;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.common.util.SetBuilder;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.exception.TerminalGameException;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.kernel.KernelState;
import info.masterfrog.pixelcat.engine.kernel.KernelStatePropertyEnum;
import info.masterfrog.pixelcat.engine.logic.clock.GameClock;
import info.masterfrog.pixelcat.engine.logic.clock.GameClockFactory;
import info.masterfrog.pixelcat.engine.logic.clock.SimpleGameClock;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObjectManager;
import info.masterfrog.pixelcat.engine.logic.gameobject.feature.Renderable;
import info.masterfrog.pixelcat.engine.logic.resource.SpriteResource;

import java.awt.*;
import java.util.HashSet;

public class LabyrinthLevelManager {
    private static LabyrinthLevelManager instance;

    private SimpleGameClock clock;
    private SimpleGameClock stateTimer;
    private String state;

    private static final String STATE__INIT = "STATE__INIT";

    private static final Printer PRINTER = PrinterFactory.getInstance().createPrinter(LabyrinthLevelManager.class);

    private LabyrinthLevelManager() {
        state = STATE__INIT;
        clock = GameClockFactory.getInstance().createSimpleGameClock();
        stateTimer = GameClockFactory.getInstance().createSimpleGameClock();
    }

    public static LabyrinthLevelManager getInstance() {
        if (instance == null) {
            instance = new LabyrinthLevelManager();
        }

        return instance;
    }

    public void init(KernelState kernelState, EnvironmentManager environmentManager) throws TransientGameException {
        // setup
        Rectangle screenBounds = ((Rectangle) kernelState.getProperty(KernelStatePropertyEnum.SCREEN_BOUNDS));

        // init game object manager
        GameObjectManager entityManager = GameObjectManager.create(1);

        // init game object builder
        EntitiesBuilder entitiesBuilder = EntitiesBuilder.getInstance(environmentManager, entityManager, screenBounds);

        // store entity manager
        environmentManager.registerEntityManager(
            EntityManagerHandle.LABYRINTH, entityManager
        );

        // register level entities
        environmentManager.registerLevelEntities(
            LevelHandle.LABYRINTH,
            new SetBuilder<HashSet, EntityManagerHandle>(HashSet.class).add(
                EntityManagerHandle.LABYRINTH
            ).get()
        );
    }

    public void run(KernelState kernelState, LevelStateManager levelStateManager, EnvironmentManager environmentManager)
                throws TransientGameException, TerminalErrorException {
        switch (state) {
            case STATE__INIT:
                // do init work

                // reset timer
                stateTimer.reset();
                break;
            default:
                throw new TerminalErrorException(new SetBuilder<HashSet, TerminalGameException>(HashSet.class, System.out, false).add(
                    new TerminalGameException(LabyrinthGameErrorCode.LEVEL__LABYRINTH__INVALID_STATE, state)
                ).get());
        }
    }
}
