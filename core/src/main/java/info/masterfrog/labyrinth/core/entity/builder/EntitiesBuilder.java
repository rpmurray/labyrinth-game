package info.masterfrog.labyrinth.core.entity.builder;

import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObject;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObjectManager;

import java.awt.*;

public class EntitiesBuilder {
    private static EntitiesBuilder instance = null;

    private GameObjectManager gameObjectManager;
    private Rectangle screenBounds;

    private EntitiesBuilder(GameObjectManager gameObjectManager, Rectangle screenBounds) {
        this.gameObjectManager = gameObjectManager;
        this.screenBounds = screenBounds;
    }

    public static EntitiesBuilder getInstance(GameObjectManager gameObjectManager, Rectangle screenBounds) {
        if (instance == null) {
            instance = new EntitiesBuilder(gameObjectManager, screenBounds);
        }

        return instance;
    }

    public GameObject buildStartScreenBGEntity() throws TransientGameException {
        return new StartScreenBGEntityBuilder(gameObjectManager, screenBounds).generate().getGameObject();
    }

    public GameObject buildDevLogoEntity() throws TransientGameException {
        return new DevLogoEntityBuilder(gameObjectManager, screenBounds).generate().getGameObject();
    }

    public GameObject buildTitleEntity() throws TransientGameException {
        return new TitleEntityBuilder(gameObjectManager, screenBounds).generate().getGameObject();
    }
}
