package info.masterfrog.labyrinth.entity.builder;

import com.google.common.collect.ImmutableList;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObject;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObjectManager;
import info.masterfrog.pixelcat.engine.logic.gameobject.feature.Renderable;
import info.masterfrog.pixelcat.engine.logic.gameobject.feature.ResourceLibrary;
import info.masterfrog.pixelcat.engine.logic.resource.Resource;
import info.masterfrog.pixelcat.engine.logic.resource.ResourceFactory;
import info.masterfrog.pixelcat.engine.logic.resource.SpriteSheet;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TitleEntityBuilder {
    private GameObject gameObject;
    private Rectangle screenBounds;
    private Map<String, List<Resource>> resourceMap = new HashMap<>();

    private static ResourceFactory resourceFactory = ResourceFactory.getInstance();

    private static Printer PRINTER = PrinterFactory.getInstance().createPrinter(TitleEntityBuilder.class);

    private static final String TITLE__RESOURCES__MAIN = "RESOURCES__MAIN";

    TitleEntityBuilder(GameObjectManager gameObjectManager, Rectangle screenBounds) throws TransientGameException {
        this.gameObject = gameObjectManager.createGameObject();
        this.screenBounds = screenBounds;
    }

    TitleEntityBuilder generate() throws TransientGameException {
        // register rendering properties
        gameObject.registerFeature(
            Renderable.create(new Point(screenBounds.width / 2 - 768, screenBounds.height / 2 - 128), 1)
        );

        // define resources
        defineResources();

        return this;
    }

    GameObject getGameObject() {
        return gameObject;
    }

    private Map<String, List<Resource>> defineResources() throws TransientGameException {
        // generate sprite sheet
        SpriteSheet mainSpriteSheet = resourceFactory.createSpriteSheet(
            "title.png",
            256, 1536
        );

        // define resources
        resourceMap.put(
            TITLE__RESOURCES__MAIN,
            ImmutableList.of(
                resourceFactory.createImageResource(resourceFactory.createSpriteResource(0, 0, mainSpriteSheet))
            )
        );

        // register resources
        gameObject.registerFeature(
            ResourceLibrary.create().add(
                resourceMap.get(TITLE__RESOURCES__MAIN).get(0)
            )
        );

        return resourceMap;
    }
}
