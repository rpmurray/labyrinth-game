package info.masterfrog.labyrinth.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import info.masterfrog.labyrinth.entity.builder.EntitiesBuilder;
import info.masterfrog.labyrinth.enumeration.EntitiesManagerHandle;
import info.masterfrog.labyrinth.enumeration.EntityHandle;
import info.masterfrog.labyrinth.enumeration.LevelHandle;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.kernel.KernelState;
import info.masterfrog.pixelcat.engine.kernel.KernelStatePropertyEnum;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObject;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObjectManager;

import java.awt.*;

public class EntitiesEnvironmentConfigurator {
    private KernelState kernelState;
    private EntitiesEnvironmentManager entitiesEnvironmentManager;

    private static Printer PRINTER = PrinterFactory.getInstance().createPrinter(EntitiesEnvironmentConfigurator.class);

    public EntitiesEnvironmentConfigurator(KernelState kernelState, EntitiesEnvironmentManager entitiesEnvironmentManager) {
        this.kernelState = kernelState;
        this.entitiesEnvironmentManager = entitiesEnvironmentManager;
    }

    public void init() throws TerminalErrorException {
        try {
            // common elements
            // N/A

            // start screen entities
            generateStartScreenEntities();

            // start screen
            entitiesEnvironmentManager.registerLevelEntities(
                LevelHandle.START_SCREEN,
                ImmutableList.of(
                    entitiesEnvironmentManager.getEntitiesManager(EntitiesManagerHandle.START_SCREEN)
                )
            );
        } catch (TransientGameException e) {
            throw new TerminalErrorException(ImmutableSet.of(e));
        }
    }

    private void generateStartScreenEntities() throws TransientGameException {
        // setup
        Rectangle screenBounds = ((Rectangle) kernelState.getProperty(KernelStatePropertyEnum.SCREEN_BOUNDS));

        // init game object manager
        GameObjectManager startScreenEntitiesManager = GameObjectManager.create(2);

        // init game object builder
        EntitiesBuilder entitiesBuilder = EntitiesBuilder.getInstance(startScreenEntitiesManager, screenBounds);

        // start screen bg
        GameObject startScreenBG = entitiesBuilder.buildStartScreenBGEntity();

        // dev logo
        GameObject devLogo = entitiesBuilder.buildDevLogoEntity();

        // title
        GameObject title = entitiesBuilder.buildTitleEntity();

        // store entity manager
        entitiesEnvironmentManager.registerEntitiesManager(
            EntitiesManagerHandle.START_SCREEN, startScreenEntitiesManager
        );

        // store entities for game processing
        entitiesEnvironmentManager.registerEntity(
            EntityHandle.START_SCREEN__BACKGROUND, startScreenBG, EntitiesManagerHandle.START_SCREEN
        ).registerEntity(
            EntityHandle.START_SCREEN__DEV_LOGO, devLogo
        ).registerEntity(
            EntityHandle.START_SCREEN__TITLE_TEXT, title
        );
    }
}
