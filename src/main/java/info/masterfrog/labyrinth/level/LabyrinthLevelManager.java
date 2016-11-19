package info.masterfrog.labyrinth.level;

import info.masterfrog.labyrinth.entity.EnvironmentManager;
import info.masterfrog.labyrinth.entity.builder.CharacterEntityBuilder;
import info.masterfrog.labyrinth.entity.builder.LabyrinthRoomEntityBuilder;
import info.masterfrog.labyrinth.enumeration.CanvasHandle;
import info.masterfrog.labyrinth.enumeration.EntityHandle;
import info.masterfrog.labyrinth.enumeration.EntityManagerHandle;
import info.masterfrog.labyrinth.enumeration.LevelHandle;
import info.masterfrog.labyrinth.exception.LabyrinthGameErrorCode;
import info.masterfrog.labyrinth.level.layout.GraphLayoutManager;
import info.masterfrog.labyrinth.level.model.Graph;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.common.util.SetBuilder;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.exception.TerminalGameException;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.kernel.KernelState;
import info.masterfrog.pixelcat.engine.kernel.KernelStateProperty;
import info.masterfrog.pixelcat.engine.logic.clock.GameClockFactory;
import info.masterfrog.pixelcat.engine.logic.clock.SimpleGameClock;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.aspect.canvas.Canvas;
import info.masterfrog.pixelcat.engine.logic.gameobject.manager.GameObjectManager;
import info.masterfrog.pixelcat.engine.logic.gameobject.object.GameObject;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;

public class LabyrinthLevelManager implements LevelManager {
    private static LabyrinthLevelManager instance;

    private SimpleGameClock clock;
    private SimpleGameClock stateTimer;
    private String state;
    private int roomCount;
    private int startRoom;
    private Graph roomGraph;
    private EnvironmentManager environmentManager;
    private LevelStateManager levelStateManager;
    private LabyrinthRoomEntityBuilder labyrinthRoomEntityBuilder;
    private CharacterEntityBuilder characterEntityBuilder;
    private GameObjectManager entityManager;

    private static final String STATE__INIT = "STATE__INIT";
    private static final String STATE__RUN = "STATE__RUN";

    private static final Printer PRINTER = PrinterFactory.getInstance().createPrinter(LabyrinthLevelManager.class);

    private LabyrinthLevelManager() throws TransientGameException {
        // general init
        this.state = STATE__INIT;
        this.clock = GameClockFactory.getInstance().createSimpleGameClock();
        this.stateTimer = GameClockFactory.getInstance().createSimpleGameClock();
        this.levelStateManager = LevelStateManager.getInstance();
        this.environmentManager = EnvironmentManager.getInstance();
        this.labyrinthRoomEntityBuilder = LabyrinthRoomEntityBuilder.getInstance();
        this.characterEntityBuilder = CharacterEntityBuilder.getInstance();

        // room init
        this.roomCount = 0;
        this.startRoom = 0;
        this.roomGraph = null;
    }

    public static LabyrinthLevelManager getInstance() throws TransientGameException {
        if (instance == null) {
            instance = new LabyrinthLevelManager();
        }

        return instance;
    }

    public void init() throws TransientGameException {
        // setup
        Rectangle screenBounds = KernelState.getInstance().getProperty(KernelStateProperty.SCREEN_BOUNDS);

        // init game object manager
        entityManager = GameObjectManager.create(2);

        // init entity builders
        labyrinthRoomEntityBuilder.init(entityManager, screenBounds);
        characterEntityBuilder.init(entityManager, screenBounds);

        // store entity manager
        environmentManager.registerEntityManager(
            EntityManagerHandle.LABYRINTH, entityManager
        );

        // register level entities
        environmentManager.registerLevelEntities(
            LevelHandle.LABYRINTH,
            new SetBuilder<HashSet, EntityManagerHandle>(HashSet.class).add(
                EntityManagerHandle.LABYRINTH
            ).get()
        );

        // set init flag
        environmentManager.setFlag(EnvironmentManager.FLAG__LABYRINTH_INITIALIZED + levelStateManager.getCurrentLabyrinthIndex(), true);
    }

    public void run() throws TransientGameException, TerminalErrorException {
        switch (state) {
            case STATE__INIT:
                // setup
                Canvas canvas = EnvironmentManager.getInstance().getCanvas(CanvasHandle.ROOM);
                Rectangle screenBounds = KernelState.getInstance().getProperty(KernelStateProperty.SCREEN_BOUNDS);
                int roomSize = 256;
                int roomGap = 32;

                // do init work
                roomCount = LevelComplexityManager.getInstance().getRoomCount();
                startRoom = (int) Math.floor(Math.random() * roomCount);
                roomGraph = Graph.generateByPathWalk(roomCount, 0.5, -1, -1, roomCount / 2);
                GraphLayoutManager.getInstance().generateRandomGridLayout(
                    roomGraph,
                    canvas.getBounds(),
                    roomSize, roomSize,
                    roomGap, roomGap,
                    3.0, 2.0
                );
                Point startRoomPosition = roomGraph.getVertexLayout(startRoom);

                // generate room entities
                for (int i = 0; i < roomGraph.getVertexCount(); i++) {
                    GameObject roomBackground = labyrinthRoomEntityBuilder.generateEntity(roomGraph.getVertexLayout(i));
                    environmentManager.registerEntity(EntityHandle.valueOf("LABYRINTH_ROOM_" + (i + 1)), roomBackground);
                    entityManager.add(roomBackground);
                }

                // generate character entity
                GameObject characterEntity = characterEntityBuilder.generateEntity(startRoomPosition);
                environmentManager.registerEntity(EntityHandle.LABYRINTH__CHARACTER, characterEntity);
                entityManager.add(characterEntity);

                // reposition canvas accordingly
                Point screenCenter = new Point(
                    screenBounds.x / 2,
                    screenBounds.y / 2
                );
                Point startRoomDesiredScreenPosition = new Point(
                    screenCenter.x - (roomSize / 2),
                    screenCenter.y - (roomSize / 2)
                );
                Point startRoomActualScreenPosition = startRoomPosition;
                canvas.setPosition(
                    new Point(
                        (int) ((startRoomDesiredScreenPosition.x) / canvas.getScaleFactor()),
                        (int) ((startRoomDesiredScreenPosition.y) / canvas.getScaleFactor())
                    )
                );

                // move on to run state
                state = STATE__RUN;

                // reset timer
                stateTimer.reset();
                break;
            case STATE__RUN:
                break;
            default:
                throw new TerminalErrorException(new SetBuilder<HashSet, TerminalGameException>(HashSet.class, System.out, false).add(
                    new TerminalGameException(LabyrinthGameErrorCode.LEVEL__LABYRINTH__INVALID_STATE, state)
                ).get());
        }
    }
}
