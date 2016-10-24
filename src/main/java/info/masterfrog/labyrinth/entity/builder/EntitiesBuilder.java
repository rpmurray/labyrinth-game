package info.masterfrog.labyrinth.entity.builder;

import info.masterfrog.labyrinth.entity.EnvironmentManager;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObject;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObjectManager;

import java.awt.*;

public class EntitiesBuilder {
    private static EntitiesBuilder instance = null;

    private EnvironmentManager environmentManager;
    private GameObjectManager entityManager;
    private Rectangle screenBounds;

    private EntitiesBuilder(EnvironmentManager environmentManager,
                            GameObjectManager entityManager,
                            Rectangle screenBounds) {
        this.environmentManager = environmentManager;
        this.entityManager = entityManager;
        this.screenBounds = screenBounds;
    }

    public static EntitiesBuilder getInstance(EnvironmentManager environmentManager,
                                              GameObjectManager entityManager,
                                              Rectangle screenBounds) {
        if (instance == null) {
            instance = new EntitiesBuilder(environmentManager, entityManager, screenBounds);
        }

        return instance;
    }

    public GameObject buildStartScreenBackgroundEntity() throws TransientGameException {
        return new StartScreenBackgroundEntityBuilder(environmentManager, entityManager, screenBounds).generate().getEntity();
    }

    public GameObject buildStartScreenDevLogoEntity() throws TransientGameException {
        return new StartScreenDevLogoEntityBuilder(environmentManager, entityManager, screenBounds).generate().getEntity();
    }

    public GameObject buildStartScrenTitleEntity() throws TransientGameException {
        return new StartScreenTitleEntityBuilder(environmentManager, entityManager, screenBounds).generate().getEntity();
    }
}
