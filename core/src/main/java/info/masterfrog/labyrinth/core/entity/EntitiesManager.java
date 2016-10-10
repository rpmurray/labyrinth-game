package info.masterfrog.labyrinth.core.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.sun.org.apache.xpath.internal.operations.Bool;
import info.masterfrog.labyrinth.core.enumeration.EntityHandle;
import info.masterfrog.labyrinth.core.enumeration.GameObjectManagerHandle;
import info.masterfrog.labyrinth.core.enumeration.LevelHandle;
import info.masterfrog.labyrinth.core.entity.builder.EntitiesBuilder;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.GameErrorCode;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.exception.TerminalGameException;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.hid.HIDEventEnum;
import info.masterfrog.pixelcat.engine.kernel.KernelState;
import info.masterfrog.pixelcat.engine.kernel.KernelStatePropertyEnum;
import info.masterfrog.pixelcat.engine.logic.animation.AnimationFactory;
import info.masterfrog.pixelcat.engine.logic.animation.AnimationSequence;
import info.masterfrog.pixelcat.engine.logic.camera.Camera;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObject;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObjectManager;
import info.masterfrog.pixelcat.engine.logic.gameobject.behavior.*;
import info.masterfrog.pixelcat.engine.logic.gameobject.feature.*;
import info.masterfrog.pixelcat.engine.logic.physics.screen.ScreenBoundsHandlingTypeEnum;
import info.masterfrog.pixelcat.engine.logic.resource.Resource;
import info.masterfrog.pixelcat.engine.logic.resource.ResourceFactory;
import info.masterfrog.pixelcat.engine.logic.resource.SpriteSheet;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EntitiesManager {
    private KernelState kernelState;
    private Map<EntityHandle, GameObjectIdentifier> gameObjects;
    private Map<GameObjectManagerHandle, String> gameObjectManagerHandles;
    private Map<String, GameObjectManager> gameObjectManagers;
    private Map<LevelHandle, List<String>> levels;
    private Map<String, Boolean> stateFlags;

    private static Printer PRINTER = PrinterFactory.getInstance().createPrinter(EntitiesManager.class);

    public static final String FLAG__START_INITIALIZED = "FLAG__START_INITIALIZED";
    public static final String FLAG__EXIT_KEY_BOUND = "FLAG__EXIT_KEY_BOUND";

    public EntitiesManager(KernelState kernelState) {
        this.kernelState = kernelState;
        this.gameObjects = new HashMap<>();
        this.gameObjectManagerHandles = new HashMap<>();
        this.gameObjectManagers = new HashMap<>();
        this.levels = new HashMap<>();
        this.stateFlags = new HashMap<>();
    }

    private EntitiesManager registerGameObject(EntityHandle handle, String objectId, String managerId) {
        // generate identifier
        GameObjectIdentifier gameObjectIdentifier = new GameObjectIdentifier(objectId, managerId);

        // store identifier against handle
        gameObjects.put(handle, gameObjectIdentifier);

        return this;
    }

    public GameObject getGameObject(EntityHandle entityHandle) throws TransientGameException {
        // fetch identifier
        GameObjectIdentifier gameObjectIdentifier = gameObjects.get(entityHandle);

        // fetch game object manager
        GameObjectManager gameObjectManager = getGameObjectManager(gameObjectIdentifier.getParentId());

        // fetch game object from game manager
        GameObject gameObject = gameObjectManager.get(gameObjectIdentifier.id);

        return gameObject;
    }

    public EntitiesManager registerGameObjectManager(GameObjectManagerHandle handle, GameObjectManager manager) {
        // store id against handle
        gameObjectManagerHandles.put(handle, manager.getId());

        // store manager against id
        gameObjectManagers.put(manager.getId(), manager);

        return this;
    }

    public GameObjectManager getGameObjectManager(String id) throws TransientGameException {
        // validate
        if (!gameObjectManagers.containsKey(id)) {
            throw new TransientGameException(GameErrorCode.LOGIC_ERROR);
        }

        // fetch manager from id
        GameObjectManager gameObjectManager = gameObjectManagers.get(id);

        return gameObjectManager;
    }

    public GameObjectManager getGameObjectManager(GameObjectManagerHandle handle) throws TransientGameException {
        // validate
        if (!gameObjectManagerHandles.containsKey(handle)) {
            throw new TransientGameException(GameErrorCode.LOGIC_ERROR);
        }

        // fetch id from handle
        String id = gameObjectManagerHandles.get(handle);

        // fetch manager from id
        GameObjectManager manager = getGameObjectManager(id);

        return manager;
    }

    private EntitiesManager registerGameObjectManagerList(LevelHandle handle, List<GameObjectManager> managers) {
        // setup
        List<String> managerIds = new ArrayList<>();

        // iterate through and build list of IDs from managers
        for (GameObjectManager manager : managers) {
            managerIds.add(manager.getId());
        }

        // store list of manager IDs
        levels.put(handle, managerIds);

        return this;
    }

    public List<GameObjectManager> getGameObjectManagerList(LevelHandle handle) throws TerminalErrorException {
        // setup
        List<GameObjectManager> managers = new ArrayList<>();

        try {
            // validate
            if (!levels.containsKey(handle)) {
                throw new TerminalGameException(GameErrorCode.LOGIC_ERROR);
            }

            // fetch list of manager IDs
            List<String> managerIds = levels.get(handle);

            // iterate through and build list of managers from IDs
            for (String managerId : managerIds) {
                try {
                    managers.add(getGameObjectManager(managerId));
                } catch (TransientGameException e) {
                    throw new TerminalGameException(e);
                }
            }
        } catch (TerminalGameException e) {
            throw new TerminalErrorException(ImmutableSet.of(e));
        }

        return managers;
    }

    public void init() throws TerminalErrorException {
        try {
            // common elements
            // N/A

            // start screen elements
            registerGameObjectManager(GameObjectManagerHandle.START_SCREEN, generateStartScreenElements());

            // start screen
            registerGameObjectManagerList(
                LevelHandle.START_SCREEN,
                ImmutableList.of(
                    getGameObjectManager(GameObjectManagerHandle.START_SCREEN)
                )
            );
        } catch (TransientGameException e) {
            throw new TerminalErrorException(ImmutableSet.of(e));
        }
    }

    private GameObjectManager generateStartScreenElements() throws TransientGameException {
        // setup
        Rectangle screenBounds = ((Rectangle) kernelState.getProperty(KernelStatePropertyEnum.SCREEN_BOUNDS));

        // init game object manager
        GameObjectManager gameObjectManager = GameObjectManager.create(2);

        // init game object builder
        EntitiesBuilder entitiesBuilder = EntitiesBuilder.getInstance(gameObjectManager, screenBounds);

        // start screen bg
        GameObject startScreenBG = entitiesBuilder.buildStartScreenBGEntity();

        // dev logo
        GameObject devLogo = entitiesBuilder.buildDevLogoEntity();

        // title
        GameObject title = entitiesBuilder.buildTitleEntity();

        // add game objects
        gameObjectManager.add(
            startScreenBG
        ).add(
            devLogo
        ).add(
            title
        );

        // store game object IDs for game processing
        registerGameObject(
            EntityHandle.START_SCREEN__BACKGROUND, startScreenBG.getId(), gameObjectManager.getId()
        ).registerGameObject(
            EntityHandle.START_SCREEN__DEV_LOGO, devLogo.getId(), gameObjectManager.getId()
        ).registerGameObject(
            EntityHandle.START_SCREEN__TITLE_TEXT, title.getId(), gameObjectManager.getId()
        );

        return gameObjectManager;
    }

    public Boolean getFlag(String flag) {
        return stateFlags.containsKey(flag) && stateFlags.get(flag);
    }

    public void setFlag(String flag, Boolean toggle) {
        stateFlags.put(flag, toggle);
    }

    private class GameObjectIdentifier {
        private String id;
        private String parentId;

        private GameObjectIdentifier(String id, String parentId) {
            this.id = id;
            this.parentId = parentId;
        }

        public String getId() {
            return id;
        }

        public String getParentId() {
            return parentId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof GameObjectIdentifier)) {
                return false;
            }

            GameObjectIdentifier that = (GameObjectIdentifier) o;

            if (!id.equals(that.id)) {
                return false;
            }

            return parentId.equals(that.parentId);
        }

        @Override
        public String toString() {
            return "GameObjectIdentifier{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
        }
    }
}
