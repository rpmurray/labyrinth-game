package info.masterfrog.labyrinth.entity.builder;

import info.masterfrog.labyrinth.entity.EnvironmentManager;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.logic.gameobject.manager.GameObjectManager;
import info.masterfrog.pixelcat.engine.logic.gameobject.object.GameObject;

import java.awt.Rectangle;

public class StartScreenEntitiesBuilder {
    private static StartScreenEntitiesBuilder instance = null;

    private static EnvironmentManager environmentManager = EnvironmentManager.getInstance();
    private GameObjectManager entityManager;
    private Rectangle screenBounds;

    private StartScreenEntitiesBuilder() {
        // do nothing
    }

    public static StartScreenEntitiesBuilder getInstance() {
        if (instance == null) {
            instance = new StartScreenEntitiesBuilder();
        }

        return instance;
    }

    public void init(GameObjectManager entityManager,
                     Rectangle screenBounds) {
        this.entityManager = entityManager;
        this.screenBounds = screenBounds;
    }

    public GameObject buildBackgroundEntity() throws TransientGameException {
        return new StartScreenBackgroundEntityBuilder(environmentManager, entityManager, screenBounds).generate().getEntity();
    }

    public GameObject buildDevLogoEntity() throws TransientGameException {
        return new StartScreenDevLogoEntityBuilder(environmentManager, entityManager, screenBounds).generate().getEntity();
    }

    public GameObject buildTitleEntity() throws TransientGameException {
        return new StartScreenTitleEntityBuilder(environmentManager, entityManager, screenBounds).generate().getEntity();
    }
}
