package info.masterfrog.labyrinth.entity.builder;

import info.masterfrog.labyrinth.entity.EnvironmentManager;
import info.masterfrog.labyrinth.enumeration.CanvasHandle;
import info.masterfrog.labyrinth.enumeration.EntityHandle;
import info.masterfrog.labyrinth.enumeration.ResourceHandle;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.common.util.SetBuilder;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.hid.HIDEvent;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.aspect.rendering.Rendering;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.behavior.Behavior;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.behavior.BehaviorBinding;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.behavior.BehaviorEnum;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.behavior.BehaviorParameter;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.behavior.BehaviorParameterFactory;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.feature.BehaviorBindingLibrary;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.feature.RenderingLibrary;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.feature.ResourceLibrary;
import info.masterfrog.pixelcat.engine.logic.gameobject.manager.GameObjectManager;
import info.masterfrog.pixelcat.engine.logic.gameobject.object.GameObject;
import info.masterfrog.pixelcat.engine.logic.resource.ResourceFactory;
import info.masterfrog.pixelcat.engine.logic.resource.SpriteResource;
import info.masterfrog.pixelcat.engine.logic.resource.SpriteSheet;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;

public class CharacterEntityBuilder {
    private static CharacterEntityBuilder instance;

    private GameObjectManager entityManager;
    private Rectangle screenBounds;

    private EnvironmentManager environmentManager = EnvironmentManager.getInstance();
    private static ResourceFactory resourceFactory = ResourceFactory.getInstance();

    private static Printer PRINTER = PrinterFactory.getInstance().createPrinter(CharacterEntityBuilder.class);

    public static CharacterEntityBuilder getInstance() {
        if (instance == null) {
            instance = new CharacterEntityBuilder();
        }

        return instance;
    }

    private CharacterEntityBuilder() {
        // do nothing
    }

    public void init(GameObjectManager entityManager,
                     Rectangle screenBounds) throws TransientGameException {
        this.entityManager = entityManager;
        this.screenBounds = screenBounds;
    }

    public GameObject generateEntity(Point roomPosition) throws TransientGameException {
        // setup
        GameObject entity = entityManager.createGameObject().setCanvas(
            EnvironmentManager.getInstance().getCanvas(CanvasHandle.ROOM)
        );

        // calculate render position
        Point characterPosition = new Point(roomPosition.x + 124, roomPosition.y + 113);

        // register rendering properties
        entity.registerFeature(
            RenderingLibrary.create().add(
                Rendering.create(
                    EnvironmentManager.getInstance().getCanvas(CanvasHandle.ROOM),
                    characterPosition,
                    1,
                    1.0
                )
            )
        );

        // define resources
        defineResources(entity);

        // define bindings
        defineBindings(entity);

        return entity;
    }

    private void defineResources(GameObject entity) throws TransientGameException {
        // generate sprite sheet
        SpriteSheet mainSpriteSheet = resourceFactory.createSpriteSheet(
            "character.png",
            12, 17
        );

        // define resources
        environmentManager.registerResource(
            ResourceHandle.LABYRINTH__SPRITE__CHARACTER,
            resourceFactory.createSpriteResource(0, 0, mainSpriteSheet)
        );
        environmentManager.registerResource(
            ResourceHandle.LABYRINTH__IMAGE__CHARACTER,
            resourceFactory.createImageResource(
                (SpriteResource) environmentManager.getResource(ResourceHandle.LABYRINTH__SPRITE__CHARACTER)
            )
        );

        // register resources
        entity.registerFeature(
            ResourceLibrary.create().add(
                environmentManager.getResource(ResourceHandle.LABYRINTH__IMAGE__CHARACTER)
            )
        );
    }

    private void defineBindings(GameObject entity) throws TransientGameException {
        entity.registerFeature(
            BehaviorBindingLibrary.create().add(
                BehaviorBinding.create(
                    new Behavior(
                        BehaviorEnum.MOVE_UP,
                        new SetBuilder<HashSet, BehaviorParameter>(HashSet.class).add(
                            BehaviorParameterFactory.getInstance().createMagnitudeParameter(2.0)
                        ).get()
                    ),
                    HIDEvent.W,
                    100L
                )
            ).add(
                BehaviorBinding.create(
                    new Behavior(
                        BehaviorEnum.MOVE_RIGHT,
                        new SetBuilder<HashSet, BehaviorParameter>(HashSet.class).add(
                            BehaviorParameterFactory.getInstance().createMagnitudeParameter(2.0)
                        ).get()
                    ),
                    HIDEvent.D,
                    100L
                )
            ).add(
                BehaviorBinding.create(
                    new Behavior(
                        BehaviorEnum.MOVE_DOWN,
                        new SetBuilder<HashSet, BehaviorParameter>(HashSet.class).add(
                            BehaviorParameterFactory.getInstance().createMagnitudeParameter(2.0)
                        ).get()
                    ),
                    HIDEvent.S,
                    100L
                )
            ).add(
                BehaviorBinding.create(
                    new Behavior(
                        BehaviorEnum.MOVE_LEFT,
                        new SetBuilder<HashSet, BehaviorParameter>(HashSet.class).add(
                            BehaviorParameterFactory.getInstance().createMagnitudeParameter(2.0)
                        ).get()
                    ),
                    HIDEvent.A,
                    100L
                )
            )
        );
    }
}
