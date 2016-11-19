package info.masterfrog.labyrinth.level;

import info.masterfrog.labyrinth.entity.EnvironmentManager;
import info.masterfrog.labyrinth.entity.builder.StartScreenEntitiesBuilder;
import info.masterfrog.labyrinth.enumeration.EntityManagerHandle;
import info.masterfrog.labyrinth.enumeration.EntityHandle;
import info.masterfrog.labyrinth.enumeration.LevelHandle;
import info.masterfrog.labyrinth.enumeration.ResourceHandle;
import info.masterfrog.labyrinth.exception.LabyrinthGameErrorCode;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.common.util.SetBuilder;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.exception.TerminalGameException;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.kernel.KernelState;
import info.masterfrog.pixelcat.engine.kernel.KernelStateProperty;
import info.masterfrog.pixelcat.engine.logic.clock.GameClock;
import info.masterfrog.pixelcat.engine.logic.clock.GameClockFactory;
import info.masterfrog.pixelcat.engine.logic.clock.SimpleGameClock;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.aspect.rendering.Rendering;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.feature.RenderingLibrary;
import info.masterfrog.pixelcat.engine.logic.gameobject.manager.GameObjectManager;
import info.masterfrog.pixelcat.engine.logic.gameobject.object.GameObject;
import info.masterfrog.pixelcat.engine.logic.resource.SpriteResource;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashSet;

public class StartScreenLevelManager implements LevelManager {
    private static StartScreenLevelManager instance;

    private SimpleGameClock clock;
    private SimpleGameClock stateTimer;
    private String state;

    private static EnvironmentManager environmentManager = EnvironmentManager.getInstance();
    private static LevelStateManager levelStateManager = LevelStateManager.getInstance();

    private static final String STATE__INIT = "STATE__INIT";
    private static final String STATE__WARM_UP = "STATE__WARM_UP";
    private static final String STATE__DEV_LOGO_FADE_IN = "STATE__DEV_LOGO_FADE_IN";
    private static final String STATE__DEV_LOGO = "STATE__DEV_LOGO";
    private static final String STATE__DEV_LOGO_FADE_OUT = "STATE__DEV_LOGO_FADE_OUT";
    private static final String STATE__DEV_LOGO_COOL_DOWN = "STATE__DEV_LOGO_COOL_DOWN";
    private static final String STATE__TITLE_FADE_IN = "STATE__TITLE_FADE_IN";
    private static final String STATE__TITLE = "STATE__TITLE";
    private static final String STATE__TITLE_FADE_OUT = "STATE__TITLE_FADE_OUT";
    private static final String STATE__TITLE_COOL_DOWN = "STATE__TITLE_COOL_DOWN";
    private static final String STATE__MAIN_SCREEN_FADE_IN = "STATE__MAIN_SCREEN_FADE_IN";
    private static final String STATE__MAIN_SCREEN = "STATE__MAIN_SCREEN";

    private static final Printer PRINTER = PrinterFactory.getInstance().createPrinter(StartScreenLevelManager.class);

    private StartScreenLevelManager() {
        state = STATE__INIT;
        clock = GameClockFactory.getInstance().createSimpleGameClock();
        stateTimer = GameClockFactory.getInstance().createSimpleGameClock();
    }

    public static StartScreenLevelManager getInstance() {
        if (instance == null) {
            instance = new StartScreenLevelManager();
        }

        return instance;
    }

    public void init() throws TransientGameException {
               // setup
        Rectangle screenBounds = KernelState.getInstance().getProperty(KernelStateProperty.SCREEN_BOUNDS);

        // init game object manager
        GameObjectManager startScreenEntityManager = GameObjectManager.create(2);

        // init game object builder
        StartScreenEntitiesBuilder startScreenEntitiesBuilder = StartScreenEntitiesBuilder.getInstance();
        startScreenEntitiesBuilder.init(startScreenEntityManager, screenBounds);

        // start screen bg
        GameObject background = startScreenEntitiesBuilder.buildBackgroundEntity();

        // dev logo
        GameObject devLogo = startScreenEntitiesBuilder.buildDevLogoEntity();

        // title
        GameObject title = startScreenEntitiesBuilder.buildTitleEntity();

        // store entity manager
        environmentManager.registerEntityManager(
            EntityManagerHandle.START_SCREEN, startScreenEntityManager
        );

        // store entities for game processing
        environmentManager.registerEntity(
            EntityHandle.START_SCREEN__BACKGROUND, background, EntityManagerHandle.START_SCREEN
        ).registerEntity(
            EntityHandle.START_SCREEN__DEV_LOGO, devLogo
        ).registerEntity(
            EntityHandle.START_SCREEN__TITLE_TEXT, title
        );

        // set init flag
        environmentManager.setFlag(EnvironmentManager.FLAG__START_INITIALIZED, true);

        // start screen
        environmentManager.registerLevelEntities(
            LevelHandle.START_SCREEN,
            new SetBuilder<HashSet, EntityManagerHandle>(HashSet.class).add(
                EntityManagerHandle.START_SCREEN
            ).get()
        );
    }

    public void run() throws TransientGameException, TerminalErrorException {
        switch (state) {
            case STATE__INIT:
                // do init work
                /*((SoundResource) entitiesEnvironmentManager.getResource(
                    ResourceHandle.START_SCREEN__SOUND__INTRO_MUSIC
                )).play();*/

                // transition to WARM_UP
                state = STATE__WARM_UP;

                // reset timer
                stateTimer.reset();
                break;
            case STATE__WARM_UP:
                // transition to DEV_LOGO_FADE_IN after 1s
                if (GameClock.ns2s(stateTimer.getElapsed()) > 1) {
                    // update state
                    state = STATE__DEV_LOGO_FADE_IN;

                    // do transition work
                    environmentManager.getEntityManager(
                        EntityManagerHandle.START_SCREEN
                    ).add(
                        environmentManager.getEntity(
                            EntityHandle.START_SCREEN__DEV_LOGO
                        )
                    );

                    // reset timer
                    stateTimer.reset();
                }

                break;
            case STATE__DEV_LOGO_FADE_IN:
                // fade in
                ((SpriteResource) environmentManager.getResource(
                    ResourceHandle.START_SCREEN__SPRITE__DEV_LOGO
                )).setAlphaMask(Math.min((GameClock.ns2ms(stateTimer.getElapsed()) / 50) * 0.05f, 1.0f));

                // transition to DEV_LOGO after 1s
                if (GameClock.ns2s(stateTimer.getElapsed()) > 1) {
                    // update state
                    state = STATE__DEV_LOGO;

                    // reset timer
                    stateTimer.reset();
                }

                break;
            case STATE__DEV_LOGO:
                // transition to DEV_LOGO_FADE_OUT after 2s
                if (GameClock.ns2s(stateTimer.getElapsed()) > 2) {
                    state = STATE__DEV_LOGO_FADE_OUT;

                    // reset timer
                    stateTimer.reset();
                }

                break;
            case STATE__DEV_LOGO_FADE_OUT:
                // fade out
                ((SpriteResource) environmentManager.getResource(
                    ResourceHandle.START_SCREEN__SPRITE__DEV_LOGO
                )).setAlphaMask(Math.max(1.0f - (GameClock.ns2ms(stateTimer.getElapsed()) / 50) * 0.05f, 0.0f));

                // transition to DEV_LOGO_COOL_DOWN after 1s
                if (GameClock.ns2s(stateTimer.getElapsed()) > 1) {
                    // update state
                    state = STATE__DEV_LOGO_COOL_DOWN;

                    // reset timer
                    stateTimer.reset();
                }

                break;
            case STATE__DEV_LOGO_COOL_DOWN:
                // transition to TITLE_FADE_IN after 1s
                if (GameClock.ns2s(stateTimer.getElapsed()) > 1) {
                    state = STATE__TITLE_FADE_IN;

                    // do transition work
                    environmentManager.getEntityManager(
                        EntityManagerHandle.START_SCREEN
                    ).add(
                        environmentManager.getEntity(
                            EntityHandle.START_SCREEN__TITLE_TEXT
                        )
                    );

                    // reset timer
                    stateTimer.reset();
                }

                break;
            case STATE__TITLE_FADE_IN:
                // fade in
                ((SpriteResource) environmentManager.getResource(
                    ResourceHandle.START_SCREEN__SPRITE__TITLE
                )).setAlphaMask(Math.min((GameClock.ns2ms(stateTimer.getElapsed()) / 50) * 0.05f, 1.0f));

                // transition to TITLE after 1s
                if (GameClock.ns2s(stateTimer.getElapsed()) > 1) {
                    // update state
                    state = STATE__TITLE;

                    // reset timer
                    stateTimer.reset();
                }

                break;
            case STATE__TITLE:
                // transition to TITLE_FADE_OUT after 3s
                if (GameClock.ns2s(stateTimer.getElapsed()) > 2) {
                    state = STATE__TITLE_FADE_OUT;

                    // reset timer
                    stateTimer.reset();
                }
                break;
            case STATE__TITLE_FADE_OUT:
                // fade out
                ((SpriteResource) environmentManager.getResource(
                    ResourceHandle.START_SCREEN__SPRITE__TITLE
                )).setAlphaMask(Math.max(1.0f - (GameClock.ns2ms(stateTimer.getElapsed()) / 50) * 0.05f, 0.0f));

                // transition to TITLE_COOL_DOWN after 1s
                if (GameClock.ns2s(stateTimer.getElapsed()) > 1) {
                    // update state
                    state = STATE__TITLE_COOL_DOWN;

                    // reset timer
                    stateTimer.reset();
                }

                break;
            case STATE__TITLE_COOL_DOWN:
                // transition to MAIN_SCREEN_FADE_IN after 2s
                if (GameClock.ns2s(stateTimer.getElapsed()) > 2) {
                    state = STATE__MAIN_SCREEN_FADE_IN;

                    // reset timer
                    stateTimer.reset();
                }

                break;
            case STATE__MAIN_SCREEN_FADE_IN:
                // fade in
                ((SpriteResource) environmentManager.getResource(
                    ResourceHandle.START_SCREEN__SPRITE__BACKGROUND
                )).setAlphaMask(Math.min((GameClock.ns2ms(stateTimer.getElapsed()) / 25) * 0.025f, 1.0f));

                // transition to MAIN_SCREEN after 1.5s
                if (GameClock.ns2ms(stateTimer.getElapsed()) > 1500) {
                    // update state
                    state = STATE__MAIN_SCREEN;

                    // show title
                    ((SpriteResource) environmentManager.getResource(
                        ResourceHandle.START_SCREEN__SPRITE__TITLE
                    )).setAlphaMask(1.0f);
                    Rendering title = environmentManager.getEntity(
                        EntityHandle.START_SCREEN__TITLE_TEXT
                    ).getFeature(RenderingLibrary.class).getCurrent(
                        environmentManager.getEntity(EntityHandle.START_SCREEN__TITLE_TEXT).getCanvas()
                    );
                    title.setPosition(
                        new Point(title.getPosition().x, title.getPosition().y / 4)
                    );

                    // reset timer
                    stateTimer.reset();
                }

                break;
            case STATE__MAIN_SCREEN:
                // do nothing special

                break;
            default:
                throw new TerminalErrorException(new SetBuilder<HashSet, TerminalGameException>(HashSet.class, System.out, false).add(
                    new TerminalGameException(LabyrinthGameErrorCode.LEVEL__START_SCREEN__INVALID_STATE, state)
                ).get());
        }
    }
}
