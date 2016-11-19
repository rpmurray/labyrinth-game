package info.masterfrog.labyrinth.entity.builder;

import info.masterfrog.labyrinth.entity.EnvironmentManager;
import info.masterfrog.labyrinth.enumeration.CanvasHandle;
import info.masterfrog.labyrinth.enumeration.ResourceHandle;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.aspect.rendering.Rendering;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.feature.RenderingLibrary;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.feature.ResourceLibrary;
import info.masterfrog.pixelcat.engine.logic.gameobject.manager.GameObjectManager;
import info.masterfrog.pixelcat.engine.logic.gameobject.object.GameObject;
import info.masterfrog.pixelcat.engine.logic.resource.ResourceFactory;
import info.masterfrog.pixelcat.engine.logic.resource.SpriteResource;
import info.masterfrog.pixelcat.engine.logic.resource.SpriteSheet;

import java.awt.Point;
import java.awt.Rectangle;

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
            RenderingLibrary.create().add(
                Rendering.create(
                    EnvironmentManager.getInstance().getCanvas(CanvasHandle.MAIN),
                    new Point(0, 0),
                    0
                )
            )
        );

        // define resources
        defineResources();

        return this;
    }

    GameObject getEntity() {
        return entity;
    }

    private void defineResources() throws TransientGameException {
        // generate resource library
        ResourceLibrary resourceLibrary = ResourceLibrary.create();

        // generate sprite sheet
        SpriteSheet startScreenBGSpriteSheet = resourceFactory.createSpriteSheet(
            "start-screen-bg.png",
            1280, 800
        );

        // define image resources
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

        // add image resources
        resourceLibrary.add(
            environmentManager.getResource(ResourceHandle.START_SCREEN__IMAGE__BACKGROUND)
        );

        // define sound resources
        environmentManager.registerResource(
            ResourceHandle.START_SCREEN__SOUND__INTRO_MUSIC,
            resourceFactory.createSoundResource("start-screen.ogg", 20.0f)
        );

        // add sound resources
        resourceLibrary.add(
            environmentManager.getResource(ResourceHandle.START_SCREEN__SOUND__INTRO_MUSIC)
        );

        // register resources
        entity.registerFeature(
            resourceLibrary
        );
    }
}
