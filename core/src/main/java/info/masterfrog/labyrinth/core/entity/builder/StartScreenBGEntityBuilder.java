package info.masterfrog.labyrinth.core.entity.builder;

import com.google.common.collect.ImmutableList;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.logic.animation.AnimationFactory;
import info.masterfrog.pixelcat.engine.logic.animation.AnimationSequence;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObject;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObjectManager;
import info.masterfrog.pixelcat.engine.logic.gameobject.feature.*;
import info.masterfrog.pixelcat.engine.logic.resource.Resource;
import info.masterfrog.pixelcat.engine.logic.resource.ResourceFactory;
import info.masterfrog.pixelcat.engine.logic.resource.SoundResource;
import info.masterfrog.pixelcat.engine.logic.resource.SpriteSheet;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class StartScreenBGEntityBuilder {
    private GameObject gameObject;
    private Rectangle screenBounds;
    private Map<String, List<Resource>> resourceMap = new HashMap<>();
    private Map<String, AnimationSequence> animationMap = new HashMap<>();
    private Map<String, SoundResource> soundMap = new HashMap<>();

    private static ResourceFactory resourceFactory = ResourceFactory.getInstance();
    private static AnimationFactory animationFactory = AnimationFactory.getInstance();

    private static Printer PRINTER = PrinterFactory.getInstance().createPrinter(StartScreenBGEntityBuilder.class);

    private static final String START_SCREEN_BG__RESOURCES__PIXELS = "RESOURCES__PIXELS";
    private static final String START_SCREEN_BG__RESOURCES__MAIN = "RESOURCES__MAIN";
    private static final String START_SCREEN_BG__ANIMATIONS__MAIN = "ANIMATIONS__MAIN";
    private static final String START_SCREEN_BG__SOUNDS__MAIN = "SOUNDS__MAIN";

    StartScreenBGEntityBuilder(GameObjectManager gameObjectManager, Rectangle screenBounds) throws TransientGameException {
        this.gameObject = gameObjectManager.createGameObject();
        this.screenBounds = screenBounds;
    }

    StartScreenBGEntityBuilder generate() throws TransientGameException {
        // register rendering properties
        gameObject.registerFeature(
            Renderable.create(new Point(screenBounds.width, screenBounds.height), 0)
        );

        // define resources
        defineResources();

        // define animations
        defineAnimations();

        // define sounds
        defineSounds();

        return this;
    }

    GameObject getGameObject() {
        return gameObject;
    }

    private Map<String, List<Resource>> defineResources() throws TransientGameException {
        // generate sprite sheet
        SpriteSheet pixelSpriteSheet = resourceFactory.createSpriteSheet(
            "pixel-sprite-sheet.png",
            1, 1,
            0, 0, 0, 0
        );
        SpriteSheet startScreenBGSpriteSheet = resourceFactory.createSpriteSheet(
            "start-screen-bg.png",
            1280, 800,
            0, 0, 0, 0
        );

        // define resources
        resourceMap.put(
            START_SCREEN_BG__RESOURCES__PIXELS,
            ImmutableList.of(
                resourceFactory.createImageResource(resourceFactory.createSpriteResource(0, 0, pixelSpriteSheet)),
                resourceFactory.createImageResource(resourceFactory.createSpriteResource(1, 0, pixelSpriteSheet))
            )
        );
        resourceMap.put(
            START_SCREEN_BG__RESOURCES__MAIN,
            ImmutableList.of(
                resourceFactory.createImageResource(resourceFactory.createSpriteResource(0, 0, startScreenBGSpriteSheet))
            )
        );

        // register resources
        gameObject.registerFeature(
            ResourceLibrary.create().add(
                resourceMap.get(START_SCREEN_BG__RESOURCES__PIXELS).get(0)
            ).add(
                resourceMap.get(START_SCREEN_BG__RESOURCES__PIXELS).get(1)
            ).add(
                resourceMap.get(START_SCREEN_BG__RESOURCES__MAIN).get(0)
            ).setCurrent(
                resourceMap.get(START_SCREEN_BG__RESOURCES__PIXELS).get(1).getId()
            )
        );

        return resourceMap;
    }

    private Map<String, AnimationSequence> defineAnimations() throws TransientGameException {
        // define animation
        animationMap.put(START_SCREEN_BG__ANIMATIONS__MAIN, animationFactory.createAnimationSequence(0L));
        // white pixel for cel 0
        animationMap.get(START_SCREEN_BG__ANIMATIONS__MAIN).addCel(resourceMap.get(START_SCREEN_BG__RESOURCES__PIXELS).get(1).getId());
        // bg for cell 1
        animationMap.get(START_SCREEN_BG__ANIMATIONS__MAIN).addCel(resourceMap.get(START_SCREEN_BG__RESOURCES__MAIN).get(0).getId());

        // register animations
        gameObject.registerFeature(
            AnimationSequenceLibrary.create().add(
                animationMap.get(START_SCREEN_BG__ANIMATIONS__MAIN)
            )
        );

        return animationMap;
    }

    private Map<String, SoundResource> defineSounds() throws TransientGameException {
        // define sounds
        soundMap.put(START_SCREEN_BG__SOUNDS__MAIN, resourceFactory.createSoundResource("start-screen.mp3", 30.0f));

        // register sounds
        gameObject.registerFeature(
            SoundLibrary.create().add(
                soundMap.get(START_SCREEN_BG__SOUNDS__MAIN)
            )
        );

        return soundMap;
    }
}
