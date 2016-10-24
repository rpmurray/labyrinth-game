package info.masterfrog.labyrinth.entity.builder;

import info.masterfrog.labyrinth.entity.EnvironmentManager;
import info.masterfrog.labyrinth.enumeration.ResourceHandle;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObject;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObjectManager;
import info.masterfrog.pixelcat.engine.logic.gameobject.feature.*;
import info.masterfrog.pixelcat.engine.logic.resource.*;

import java.awt.*;

class StartScreenBackgroundEntityBuilder {
    private EnvironmentManager environmentManager;
    private Rectangle screenBounds;
    private GameObject entity;

    private static ResourceFactory resourceFactory = ResourceFactory.getInstance();

    private static Printer PRINTER = PrinterFactory.getInstance().createPrinter(StartScreenBackgroundEntityBuilder.class);

    StartScreenBackgroundEntityBuilder(EnvironmentManager environmentManager,
                                       GameObjectManager entityManager,
                                       Rectangle screenBounds)
        throws TransientGameException {
        this.environmentManager = environmentManager;
        this.screenBounds = screenBounds;
        this.entity = entityManager.createGameObject();
    }

    StartScreenBackgroundEntityBuilder generate() throws TransientGameException {
        // register rendering properties
        entity.registerFeature(
            Renderable.create(new Point(0, 0), 0)
        );

        // define resources
        defineResources();

        // define sounds
        defineSounds();

        return this;
    }

    GameObject getEntity() {
        return entity;
    }

    private void defineResources() throws TransientGameException {
        // generate sprite sheet
        SpriteSheet startScreenBGSpriteSheet = resourceFactory.createSpriteSheet(
            "start-screen-bg.png",
            1280, 800
        );

        // define resources
        environmentManager.registerResource(
            ResourceHandle.START_SCREEN__SPRITE__BACKGROUND,
            resourceFactory.createSpriteResource(0, 0, startScreenBGSpriteSheet, 0.0f)
        );
        environmentManager.registerResource(
            ResourceHandle.START_SCREEN__IMAGE__BACKGROUND,
            resourceFactory.createImageResource(
                (SpriteResource) environmentManager.getResource(ResourceHandle.START_SCREEN__SPRITE__BACKGROUND)
            )
        );

        // register resources
        entity.registerFeature(
            ResourceLibrary.create().add(
                environmentManager.getResource(ResourceHandle.START_SCREEN__IMAGE__BACKGROUND)
            )
        );
    }

    private void defineSounds() throws TransientGameException {
        // define sounds
        environmentManager.registerResource(
            ResourceHandle.START_SCREEN__SOUND__INTRO_MUSIC,
            resourceFactory.createSoundResource("start-screen.ogg", 20.0f)
        );

        // register sounds
        entity.registerFeature(
            SoundLibrary.create().add(
                (SoundResource) environmentManager.getResource(ResourceHandle.START_SCREEN__SOUND__INTRO_MUSIC)
            )
        );
    }
}
