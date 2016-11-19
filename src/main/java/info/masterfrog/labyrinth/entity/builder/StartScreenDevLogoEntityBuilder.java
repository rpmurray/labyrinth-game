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

class StartScreenDevLogoEntityBuilder {
    private EnvironmentManager environmentManager;
    private GameObject entity;
    private Rectangle screenBounds;

    private static ResourceFactory resourceFactory = ResourceFactory.getInstance();

    private static Printer PRINTER = PrinterFactory.getInstance().createPrinter(StartScreenDevLogoEntityBuilder.class);

    StartScreenDevLogoEntityBuilder(EnvironmentManager environmentManager,
                                    GameObjectManager entityManager,
                                    Rectangle screenBounds) throws TransientGameException {
        this.environmentManager = environmentManager;
        this.entity = entityManager.createGameObject();
        this.screenBounds = screenBounds;
    }

    StartScreenDevLogoEntityBuilder generate() throws TransientGameException {
        // register rendering properties
        entity.registerFeature(
            RenderingLibrary.create().add(
                Rendering.create(
                    EnvironmentManager.getInstance().getCanvas(CanvasHandle.MAIN),
                    new Point(screenBounds.width / 2 - 210, screenBounds.height / 2 - 210),
                    1,
                    0.25
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
            "mfg-logo-text.png",
            1669, 1667
        );

        // define resources
        environmentManager.registerResource(
            ResourceHandle.START_SCREEN__SPRITE__DEV_LOGO,
            resourceFactory.createSpriteResource(0, 0, mainSpriteSheet, 0.0f)
        );
        environmentManager.registerResource(
            ResourceHandle.START_SCREEN__IMAGE__DEV_LOGO,
            resourceFactory.createImageResource(
                (SpriteResource) environmentManager.getResource(ResourceHandle.START_SCREEN__SPRITE__DEV_LOGO)
            )
        );

        // register resources
        entity.registerFeature(
            ResourceLibrary.create().add(
                environmentManager.getResource(ResourceHandle.START_SCREEN__IMAGE__DEV_LOGO)
            )
        );
    }
}
