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

class StartScreenTitleEntityBuilder {
    private EnvironmentManager environmentManager;
    private GameObject entity;
    private Rectangle screenBounds;

    private static ResourceFactory resourceFactory = ResourceFactory.getInstance();

    private static Printer PRINTER = PrinterFactory.getInstance().createPrinter(StartScreenTitleEntityBuilder.class);

    StartScreenTitleEntityBuilder(EnvironmentManager environmentManager,
                                  GameObjectManager entityManager,
                                  Rectangle screenBounds) throws TransientGameException {
        this.environmentManager = environmentManager;
        this.entity = entityManager.createGameObject();
        this.screenBounds = screenBounds;
    }

    StartScreenTitleEntityBuilder generate() throws TransientGameException {
        // register rendering properties
        entity.registerFeature(
            RenderingLibrary.create().add(
                Rendering.create(
                    EnvironmentManager.getInstance().getCanvas(CanvasHandle.MAIN),
                    new Point(screenBounds.width / 2 - 632, screenBounds.height / 2 - 128),
                    1
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
        // generate sprite sheet
        SpriteSheet mainSpriteSheet = resourceFactory.createSpriteSheet(
            "title.png",
            1264, 256
        );

        // define resources
        environmentManager.registerResource(
            ResourceHandle.START_SCREEN__SPRITE__TITLE,
            resourceFactory.createSpriteResource(0, 0, mainSpriteSheet, 0.0f)
        );
        environmentManager.registerResource(
            ResourceHandle.START_SCREEN__IMAGE__TITLE,
            resourceFactory.createImageResource(
                (SpriteResource) environmentManager.getResource(ResourceHandle.START_SCREEN__SPRITE__TITLE)
            )
        );

        // register resources
        entity.registerFeature(
            ResourceLibrary.create().add(
                environmentManager.getResource(ResourceHandle.START_SCREEN__IMAGE__TITLE)
            )
        );
    }
}
