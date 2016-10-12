package info.masterfrog.labyrinth.entity;

import com.google.common.collect.ImmutableSet;
import info.masterfrog.labyrinth.enumeration.EntityHandle;
import info.masterfrog.labyrinth.enumeration.EntitiesManagerHandle;
import info.masterfrog.labyrinth.enumeration.LevelHandle;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.GameErrorCode;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.exception.TerminalGameException;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObject;
import info.masterfrog.pixelcat.engine.logic.gameobject.GameObjectManager;

import java.util.*;
import java.util.List;

public class EntitiesEnvironmentManager {
    private Map<EntityHandle, String> entities;
    private Map<EntitiesManagerHandle, String> entitiesManagerHandles;
    private Map<String, GameObjectManager> entitiesManagers;
    private Map<LevelHandle, List<String>> levels;
    private Map<String, Boolean> stateFlags;

    private static Printer PRINTER = PrinterFactory.getInstance().createPrinter(EntitiesEnvironmentManager.class);

    public static final String FLAG__START_INITIALIZED = "FLAG__START_INITIALIZED";
    public static final String FLAG__EXIT_KEY_BOUND = "FLAG__EXIT_KEY_BOUND";

    public EntitiesEnvironmentManager(Integer catalogLayers) throws TerminalErrorException {
        // init
        this.entities = new HashMap<>();
        this.entitiesManagerHandles = new HashMap<>();
        this.entitiesManagers = new HashMap<>();
        this.levels = new HashMap<>();
        this.stateFlags = new HashMap<>();

        // setup
        try {
            registerEntitiesManager(EntitiesManagerHandle.CATALOG, GameObjectManager.create(catalogLayers));
        } catch (TransientGameException e) {
            throw new TerminalErrorException(ImmutableSet.of(e));
        }
    }

    public EntitiesEnvironmentManager registerEntity(EntityHandle entityHandle, GameObject entity, EntitiesManagerHandle managerHandle) throws TransientGameException {
        // store entity against entity manager
        getEntitiesManager(managerHandle).add(entity);

        // store entity against catalog + register entity id
        registerEntity(entityHandle, entity);

        return this;
    }

    public EntitiesEnvironmentManager registerEntity(EntityHandle handle, GameObject entity) throws TransientGameException {
        // get catalog entities manager
        GameObjectManager catalogEntitiesManager = getEntitiesManager(EntitiesManagerHandle.CATALOG);

        // store entity against catalog
        catalogEntitiesManager.add(entity);

        // store identifier against catalog
        entities.put(handle, entity.getId());

        return this;
    }

    public String getEntityId(EntityHandle entityHandle) throws TransientGameException {
        // fetch entity ID
        String entityId = entities.get(entityHandle);

        return entityId;
    }

    public GameObject getEntity(EntityHandle entityHandle) throws TransientGameException {
        // fetch entity manager
        GameObjectManager entitiesManager = getEntitiesManager(EntitiesManagerHandle.CATALOG);

        // fetch entity from entity manager
        GameObject entity = entitiesManager.get(entities.get(entityHandle));

        return entity;
    }

    public GameObject getEntity(EntityHandle entityHandle, EntitiesManagerHandle managerHandle) throws TransientGameException {
        // fetch game object manager
        GameObjectManager gameObjectManager = getEntitiesManager(managerHandle);

        // fetch game object from game manager
        GameObject gameObject = gameObjectManager.get(entities.get(entityHandle));

        return gameObject;
    }

    public EntitiesEnvironmentManager registerEntitiesManager(EntitiesManagerHandle handle, GameObjectManager manager) {
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

    public EntitiesEnvironmentManager registerLevelEntities(LevelHandle handle, List<GameObjectManager> managers) {
        List<String> managerIds = new ArrayList<>();

        // iterate through and build list of IDs from managers
        for (GameObjectManager manager : managers) {
            managerIds.add(manager.getId());
        }

        // store list of manager IDs
        levels.put(handle, managerIds);

        return this;
    }

    public List<GameObjectManager> getLevelEntities(LevelHandle handle) throws TerminalErrorException {
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

    public Boolean getFlag(String flag) {
        return stateFlags.containsKey(flag) && stateFlags.get(flag);
    }

    public void setFlag(String flag, Boolean toggle) {
        stateFlags.put(flag, toggle);
    }
}
