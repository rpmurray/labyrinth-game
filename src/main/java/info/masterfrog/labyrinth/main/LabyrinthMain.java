package info.masterfrog.labyrinth.main;

import info.masterfrog.labyrinth.entity.EnvironmentConfigurator;
import info.masterfrog.labyrinth.entity.EnvironmentManager;
import info.masterfrog.labyrinth.enumeration.CanvasHandle;
import info.masterfrog.labyrinth.enumeration.LevelHandle;
import info.masterfrog.labyrinth.level.LevelStateManager;
import info.masterfrog.labyrinth.main.kernel.PreProcessingKernelInjection;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.common.util.MapBuilder;
import info.masterfrog.pixelcat.engine.exception.GameException;
import info.masterfrog.pixelcat.engine.hid.HIDEvent;
import info.masterfrog.pixelcat.engine.kernel.CanvasManager;
import info.masterfrog.pixelcat.engine.kernel.Kernel;
import info.masterfrog.pixelcat.engine.kernel.KernelActionBinder;
import info.masterfrog.pixelcat.engine.kernel.KernelFactory;
import info.masterfrog.pixelcat.engine.kernel.KernelInjection;
import info.masterfrog.pixelcat.engine.kernel.KernelInjectionEventEnum;
import info.masterfrog.pixelcat.engine.kernel.KernelState;
import info.masterfrog.pixelcat.engine.kernel.KernelStateProperty;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.aspect.canvas.Canvas;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

public class LabyrinthMain {
    private static final Printer PRINTER = PrinterFactory.getInstance().createPrinter(LabyrinthMain.class);

    public static void main(String arg[]) {
        try {
            // create kernel
            Kernel kernel = KernelFactory.getInstance().createKernel();

            // set up general kernel state initialization properties
            HashMap<KernelStateProperty, Object> kernelStateInitProperties = new HashMap<>();
            kernelStateInitProperties.put(KernelStateProperty.LOG_LVL, Printer.getLogLevelInfo());
            kernelStateInitProperties.put(KernelStateProperty.SCREEN_BOUNDS, new Rectangle(50, 50, 1600, 900));
            kernelStateInitProperties.put(KernelStateProperty.BACKGROUND_COLOR, Color.BLACK);

            // initialize
            kernel.init(kernelStateInitProperties);

            // set up general kernel action mappings
            KernelActionBinder kernelActionBinder = KernelState.getInstance().getProperty(KernelStateProperty.KERNEL_ACTION_BINDER);
            kernelActionBinder.unbind(HIDEvent.F);

            // init game objects
            EnvironmentConfigurator.getInstance().init();
            LevelHandle initLevel = LevelHandle.LABYRINTH;//START_SCREEN;
            int currentLabyrinthIndex = 6;
            LevelStateManager.getInstance().init(initLevel, currentLabyrinthIndex);
            LevelStateManager.getInstance().getCurrentLevelManager().init();
            kernel.registerGameObjectManagers(
                EnvironmentManager.getInstance().getLevelEntities(LevelStateManager.getInstance().getCurrentLevel())
            );

            // define kernel injections
            Map<KernelInjectionEventEnum, KernelInjection> kernelInjectionMap = new MapBuilder<HashMap, KernelInjectionEventEnum, KernelInjection>(
                HashMap.class, System.out, false
            ).add(
                KernelInjectionEventEnum.PRE_PROCESSING, new PreProcessingKernelInjection()
            ).get();

            // run the game kernel
            kernel.kernelMain(kernelInjectionMap);
        } catch (GameException e) {
            PRINTER.printError(e);
        }

        // exit
        System.exit(0);
    }
}
