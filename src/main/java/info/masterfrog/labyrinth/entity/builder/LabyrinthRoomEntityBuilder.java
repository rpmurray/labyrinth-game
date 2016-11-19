package info.masterfrog.labyrinth.entity.builder;

import info.masterfrog.labyrinth.entity.EnvironmentManager;
import info.masterfrog.labyrinth.enumeration.CanvasHandle;
import info.masterfrog.labyrinth.enumeration.ResourceHandle;
import info.masterfrog.labyrinth.level.LevelComplexityManager;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.aspect.rendering.Rendering;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.feature.RenderingLibrary;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.feature.ResourceLibrary;
import info.masterfrog.pixelcat.engine.logic.gameobject.manager.GameObjectManager;
import info.masterfrog.pixelcat.engine.logic.gameobject.object.GameObject;
import info.masterfrog.pixelcat.engine.logic.resource.ResourceFactory;

import java.awt.Point;
import java.awt.Rectangle;

public class LabyrinthRoomEntityBuilder {
    private Rectangle screenBounds;
    private GameObjectManager entityManager;

    private static EnvironmentManager environmentManager = EnvironmentManager.getInstance();
    private static ResourceFactory resourceFactory = ResourceFactory.getInstance();
    private static LabyrinthRoomResourceManager labyrinthRoomResourceManager = LabyrinthRoomResourceManager.getInstance();
    private static LabyrinthRoomEntityBuilder instance;

    private static Printer PRINTER = PrinterFactory.getInstance().createPrinter(LabyrinthRoomEntityBuilder.class);

    public static LabyrinthRoomEntityBuilder getInstance() {
        if (instance == null) {
            instance = new LabyrinthRoomEntityBuilder();
        }

        return instance;
    }

    private LabyrinthRoomEntityBuilder() {
        // do nothing
    }

    public void init(GameObjectManager entityManager, Rectangle screenBounds) throws TransientGameException {
        // setup
        this.screenBounds = screenBounds;
        this.entityManager = entityManager;
    }

    public GameObject generateEntity(Point position) throws TransientGameException {
        // generate initial entity
        GameObject entity = entityManager.createGameObject().setCanvas(
            EnvironmentManager.getInstance().getCanvas(CanvasHandle.ROOM)
        );

        // register rendering properties
        entity.registerFeature(
            RenderingLibrary.create().add(
                Rendering.create(
                    EnvironmentManager.getInstance().getCanvas(CanvasHandle.ROOM),
                    position,
                    0
                )
            )
        );

        // define resources
        defineResources(entity);

        return entity;
    }

    private void defineResources(GameObject entity) throws TransientGameException {
        // determine room background resource handle
        int puzzleScale = LevelComplexityManager.getInstance().getRoomPuzzleScale();
        String puzzleScaleCode = puzzleScale == 0 ? "BASE" : String.valueOf(puzzleScale) + "X" + String.valueOf(puzzleScale);
        ResourceHandle resourceHandle = ResourceHandle.valueOf("LABYRINTH__IMAGE__ROOM_BACKGROUND_" + puzzleScaleCode);

        // register resources
        entity.registerFeature(
            ResourceLibrary.create().add(
                labyrinthRoomResourceManager.getImageResource(resourceHandle)
            )
        );
    }
}
