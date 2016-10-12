package info.masterfrog.labyrinth.core.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import info.masterfrog.labyrinth.core.enumeration.EntityHandle;
import info.masterfrog.labyrinth.core.enumeration.EntitiesManagerHandle;
import info.masterfrog.labyrinth.core.enumeration.LevelHandle;
import info.masterfrog.labyrinth.core.entity.builder.EntitiesBuilder;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.GameErrorCode;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.exception.TerminalGameException;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.kernel.KernelState;
import info.masterfrog.pixelcat.engine.kernel.KernelStatePropertyEnum;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObject;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObjectManager;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EntitiesManager {
    private KernelState kernelState;
    private Map<EntityHandle, EntityIdentifier> entities;
    private Map<EntitiesManagerHandle, String> entitiesManagerHandles;
    private Map<String, GameObjectManager> entitiesManagers;
    private Map<LevelHandle, List<String>> levels;
    private Map<String, Boolean> stateFlags;

    private static Printer PRINTER = PrinterFactory.getInstance().createPrinter(EntitiesManager.class);

    public static final String FLAG__START_INITIALIZED = "FLAG__START_INITIALIZED";
    public static final String FLAG__EXIT_KEY_BOUND = "FLAG__EXIT_KEY_BOUND";

    public EntitiesManager(KernelState kernelState) {
        this.kernelState = kernelState;
        this.entities = new HashMap<>();
        this.entitiesManagerHandles = new HashMap<>();
        this.entitiesManagers = new HashMap<>();
        this.levels = new HashMap<>();
        this.stateFlags = new HashMap<>();
    }

    private EntitiesManager registerEntity(EntityHandle handle, String objectId, String managerId) {
        // generate identifier
        EntityIdentifier entityIdentifier = new EntityIdentifier(objectId, managerId);

        // store identifier against handle
        entities.put(handle, entityIdentifier);

        return this;
    }

    public GameObject getEntity(EntityHandle entityHandle) throws TransientGameException {
        // fetch identifier
        EntityIdentifier entityIdentifier = entities.get(entityHandle);

        // fetch game object manager
        GameObjectManager gameObjectManager = getEntitiesManager(entityIdentifier.getParentId());

        // fetch game object from game manager
        GameObject gameObject = gameObjectManager.get(entityIdentifier.id);

        return gameObject;
    }

    public EntitiesManager registerEntitiesManager(EntitiesManagerHandle handle, GameObjectManager manager) {
        // store id against handle
        entitiesManagerHandles.put(handle, manager.getId());

        // store manager against id
        entitiesManagers.put(manager.getId(), manager);

        return this;
    }

    public GameObjectManager getEntitiesManager(String id) throws TransientGameException {
        // validate
        if (!entitiesManagers.containsKey(id)) {
            throw new TransientGameException(GameErrorCode.LOGIC_ERROR);
        }

        // fetch manager from id
        GameObjectManager gameObjectManager = entitiesManagers.get(id);

        return gameObjectManager;
    }

    public GameObjectManager getEntitiesManager(EntitiesManagerHandle handle) throws TransientGameException {
        // validate
        if (!entitiesManagerHandles.containsKey(handle)) {
            throw new TransientGameException(GameErrorCode.LOGIC_ERROR);
        }

        // fetch id from handle
        String id = entitiesManagerHandles.get(handle);

        // fetch manager from id
        GameObjectManager manager = getEntitiesManager(id);

        return manager;
    }

    private EntitiesManager registerEntitiesManagerList(LevelHandle handle, List<GameObjectManager> managers) {
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

    public List<GameObjectManager> getEntitiesManagerList(LevelHandle handle) throws TerminalErrorException {
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
                    managers.add(getEntitiesManager(managerId));
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
            registerEntitiesManager(EntitiesManagerHandle.START_SCREEN, generateStartScreenElements());

            // start screen
            registerEntitiesManagerList(
                LevelHandle.START_SCREEN,
                ImmutableList.of(
                    getEntitiesManager(EntitiesManagerHandle.START_SCREEN)
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
        );

        // store game object IDs for game processing
        registerEntity(
            EntityHandle.START_SCREEN__BACKGROUND, startScreenBG.getId(), gameObjectManager.getId()
        ).registerEntity(
            EntityHandle.START_SCREEN__DEV_LOGO, devLogo.getId(), gameObjectManager.getId()
        ).registerEntity(
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

    private class EntityIdentifier {
        private String id;
        private String parentId;

        private EntityIdentifier(String id, String parentId) {
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
            if (!(o instanceof EntityIdentifier)) {
                return false;
            }

            EntityIdentifier that = (EntityIdentifier) o;

            if (!id.equals(that.id)) {
                return false;
            }

            return parentId.equals(that.parentId);
        }

        @Override
        public String toString() {
            return "EntityIdentifier{" +
                "id='" + id + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
        }
    }
}
