package info.masterfrog.labyrinth.core.entity.builder;

import com.google.common.collect.ImmutableList;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.logic.animation.AnimationFactory;
import info.masterfrog.pixelcat.engine.logic.animation.AnimationSequence;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObject;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObjectManager;
import info.masterfrog.pixelcat.engine.logic.gameobject.feature.AnimationSequenceLibrary;
import info.masterfrog.pixelcat.engine.logic.gameobject.feature.Renderable;
import info.masterfrog.pixelcat.engine.logic.gameobject.feature.ResourceLibrary;
import info.masterfrog.pixelcat.engine.logic.gameobject.feature.SoundLibrary;
import info.masterfrog.pixelcat.engine.logic.resource.Resource;
import info.masterfrog.pixelcat.engine.logic.resource.ResourceFactory;
import info.masterfrog.pixelcat.engine.logic.resource.SoundResource;
import info.masterfrog.pixelcat.engine.logic.resource.SpriteSheet;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DevLogoEntityBuilder {
    private GameObject gameObject;
    private Rectangle screenBounds;
    private Map<String, List<Resource>> resourceMap = new HashMap<>();

    private static ResourceFactory resourceFactory = ResourceFactory.getInstance();

    private static Printer PRINTER = PrinterFactory.getInstance().createPrinter(DevLogoEntityBuilder.class);

    private static final String DEV_LOGO__RESOURCES__MAIN = "RESOURCES__MAIN";

    DevLogoEntityBuilder(GameObjectManager gameObjectManager, Rectangle screenBounds) throws TransientGameException {
        this.gameObject = gameObjectManager.createGameObject();
        this.screenBounds = screenBounds;
    }

    DevLogoEntityBuilder generate() throws TransientGameException {
        // register rendering properties
        gameObject.registerFeature(
            Renderable.create(new Point(screenBounds.width / 2 - 210, screenBounds.height / 2 - 210), 1, 0.25)
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
            "mfg-logo-text.png",
            1669, 1667
        );

        // define resources
        resourceMap.put(
            DEV_LOGO__RESOURCES__MAIN,
            ImmutableList.of(
                resourceFactory.createImageResource(resourceFactory.createSpriteResource(0, 0, mainSpriteSheet))
            )
        );

        // register resources
        gameObject.registerFeature(
            ResourceLibrary.create().add(
                resourceMap.get(DEV_LOGO__RESOURCES__MAIN).get(0)
            )
        );

        return resourceMap;
    }
}
