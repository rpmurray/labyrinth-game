package info.masterfrog.labyrinth.entity;

import info.masterfrog.labyrinth.enumeration.CanvasHandle;
import info.masterfrog.labyrinth.enumeration.EntityHandle;
import info.masterfrog.labyrinth.enumeration.EntityManagerHandle;
import info.masterfrog.labyrinth.enumeration.LevelHandle;
import info.masterfrog.labyrinth.enumeration.ResourceHandle;
import info.masterfrog.labyrinth.exception.LabyrinthGameErrorCode;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.common.util.SetBuilder;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.exception.TerminalGameException;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.kernel.CanvasManager;
import info.masterfrog.pixelcat.engine.kernel.KernelState;
import info.masterfrog.pixelcat.engine.kernel.KernelStateProperty;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.aspect.canvas.Canvas;
import info.masterfrog.pixelcat.engine.logic.gameobject.manager.GameObjectManager;
import info.masterfrog.pixelcat.engine.logic.gameobject.object.GameObject;
import info.masterfrog.pixelcat.engine.logic.resource.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnvironmentManager {
    private Map<ResourceHandle, Resource> resources;
    private Map<EntityHandle, GameObject> entities;
    private Map<EntityManagerHandle, GameObjectManager> entityManagers;
    private Map<LevelHandle, Set<EntityManagerHandle>> levels;
    private Map<CanvasHandle, String> canvases;
    private Map<String, Boolean> stateFlags;

    private static EnvironmentManager instance;

    private static final Printer PRINTER = PrinterFactory.getInstance().createPrinter(EnvironmentManager.class);

    public static final String FLAG__START_INITIALIZED = "FLAG__START_INITIALIZED";
    public static final String FLAG__LABYRINTH_INITIALIZED = "FLAG__LABYRINTH_INITIALIZED";
    public static final String FLAG__EXIT_KEY_BOUND = "FLAG__EXIT_KEY_BOUND";

    public static EnvironmentManager getInstance() {
        if (instance == null) {
            instance = new EnvironmentManager();
        }

        return instance;
    }

    private EnvironmentManager() {
        this.resources = new HashMap<>();
        this.entities = new HashMap<>();
        this.entityManagers = new HashMap<>();
        this.levels = new HashMap<>();
        this.canvases = new HashMap<>();
        this.stateFlags = new HashMap<>();
    }

    public EnvironmentManager registerResource(ResourceHandle handle, Resource resource) {
        // store resource
        resources.put(handle, resource);

        return this;
    }

    public Resource getResource(ResourceHandle handle) throws TransientGameException {
        // validate
        if (!hasResource(handle)) {
            throw new TransientGameException(LabyrinthGameErrorCode.ENVIRONMENT__MANAGEMENT__UNDEFINED_RESOURCE_ACCESS);
        }

        // fetch entity
        Resource resource = resources.get(handle);

        return resource;
    }

    public Boolean hasResource(ResourceHandle handle) {
        // lookup resource
        Boolean hasResource = resources.containsKey(handle);

        return hasResource;
    }

    public EnvironmentManager registerEntity(EntityHandle entityHandle, GameObject entity, EntityManagerHandle managerHandle) throws TransientGameException {
        // store entity against entity manager
        entityManagers.get(managerHandle).add(entity);

        // store entity
        registerEntity(entityHandle, entity);

        return this;
    }

    public EnvironmentManager registerEntity(EntityHandle entityHandle, GameObject entity) throws TransientGameException {
        // store entity
        entities.put(entityHandle, entity);

        return this;
    }

    public GameObject getEntity(EntityHandle handle) throws TransientGameException {
        // validate
        if (!hasEntity(handle)) {
            throw new TransientGameException(LabyrinthGameErrorCode.ENVIRONMENT__MANAGEMENT__UNDEFINED_ENTITY_ACCESS);
        }

        // fetch entity
        GameObject entity = entities.get(handle);

        return entity;
    }

    public Boolean hasEntity(EntityHandle handle) {
        // lookup entity
        Boolean hasEntity = entities.containsKey(handle);

        return hasEntity;
    }

    public Boolean hasEntityInManager(EntityHandle entityHandle, EntityManagerHandle managerHandle) {
        // fetch game object manager
        GameObjectManager gameObjectManager = entityManagers.get(managerHandle);

        // fetch game object from game manager
        Boolean hasEntity = gameObjectManager.has(entities.get(entityHandle).getId());

        return hasEntity;
    }

    public EnvironmentManager registerEntityManager(EntityManagerHandle handle, GameObjectManager manager) {
        // store manager against handle
        entityManagers.put(handle, manager);

        return this;
    }

    public GameObjectManager getEntityManager(EntityManagerHandle handle) throws TransientGameException {
        // validate
        if (!hasEntityManager(handle)) {
            throw new TransientGameException(LabyrinthGameErrorCode.ENVIRONMENT__MANAGEMENT__UNDEFINED_ENTITY_MANAGER_ACCESS);
        }

        // fetch manager from handle
        GameObjectManager manager = entityManagers.get(handle);

        return manager;
    }

    public Boolean hasEntityManager(EntityManagerHandle handle) {
        // lookup entity manager
        Boolean hasEntityManager = entityManagers.containsKey(handle);

        return hasEntityManager;
    }

    public EnvironmentManager registerLevelEntities(LevelHandle handle, Set<EntityManagerHandle> managerHandles) {
        // store list of manager IDs
        levels.put(handle, managerHandles);

        return this;
    }

    public List<GameObjectManager> getLevelEntities(LevelHandle handle) throws TerminalErrorException {
        // setup
        List<GameObjectManager> managers = new ArrayList<>();

        try {
            // validate
            if (!hasLevelEntities(handle)) {
                throw new TerminalGameException(LabyrinthGameErrorCode.ENVIRONMENT__MANAGEMENT__UNDEFINED_LEVEL_ENTITIES_ACCESS);
            }

            // fetch list of manager IDs
            Set<EntityManagerHandle> managerHandles = levels.get(handle);

            // iterate through and build list of managers from IDs
            for (EntityManagerHandle managerHandle : managerHandles) {
                managers.add(entityManagers.get(managerHandle));
            }
        } catch (TerminalGameException e) {
            throw new TerminalErrorException(new SetBuilder<HashSet, TerminalGameException>(HashSet.class, System.out, false).add(e).get());
        }

        return managers;
    }

    public Boolean hasLevelEntities(LevelHandle handle) {
        // lookup level entities
        Boolean hasLevelEntities = levels.containsKey(handle);

        return hasLevelEntities;
    }

    public void registerCanvas(CanvasHandle canvasHandle, String canvasId) {
        canvases.put(canvasHandle, canvasId);
    }

    public Canvas getCanvas(CanvasHandle canvasHandle) throws TransientGameException {
        Canvas canvas = KernelState.getInstance().<CanvasManager>getProperty(
            KernelStateProperty.CANVAS_MANAGER
        ).get(
            canvases.get(canvasHandle)
        );

        return canvas;
    }

    public Boolean getFlag(String flag) {
        return stateFlags.containsKey(flag) && stateFlags.get(flag);
    }

    public void setFlag(String flag, Boolean toggle) {
        stateFlags.put(flag, toggle);
    }
}
