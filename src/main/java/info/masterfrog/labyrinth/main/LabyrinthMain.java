package info.masterfrog.labyrinth.main;

import info.masterfrog.labyrinth.entity.EnvironmentConfigurator;
import info.masterfrog.labyrinth.entity.EnvironmentManager;
import info.masterfrog.labyrinth.enumeration.LevelHandle;
import info.masterfrog.labyrinth.level.LevelStateManager;
import info.masterfrog.labyrinth.main.kernel.PreProcessingKernelInjection;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.common.util.MapBuilder;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.hid.HIDEventEnum;
import info.masterfrog.pixelcat.engine.kernel.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LabyrinthMain {
    private static final Printer PRINTER = PrinterFactory.getInstance().createPrinter(LabyrinthMain.class);

    public static void main(String arg[]) {
        try {
            // create kernel
            Kernel kernel = KernelFactory.getInstance().createKernel();

            // set up general kernel state initialization properties
            HashMap<KernelStatePropertyEnum, Object> kernelStateInitProperties = new HashMap<>();
            kernelStateInitProperties.put(KernelStatePropertyEnum.LOG_LVL, Printer.getLogLevelInfo());
            kernelStateInitProperties.put(KernelStatePropertyEnum.SCREEN_BOUNDS, new Rectangle(50, 50, 1280, 800));

            // initialize
            kernel.init(kernelStateInitProperties);
            KernelState kernelState = kernel.getKernelState();

            // set up general kernel action mappings
            KernelActionBinder kernelActionBinder = (KernelActionBinder) kernelState.getProperty(KernelStatePropertyEnum.KERNEL_ACTION_BINDER);
            kernelActionBinder.unbind(HIDEventEnum.F);

            // init game objects
            LevelHandle startScreen = LevelHandle.START_SCREEN;
            LevelStateManager levelStateManager = new LevelStateManager(startScreen);
            EnvironmentManager environmentManager = new EnvironmentManager();
            EnvironmentConfigurator environmentConfigurator = new EnvironmentConfigurator(
                kernelState,
                environmentManager
            );
            environmentConfigurator.init();
            kernel.registerGameObjectManagers(environmentManager.getLevelEntities(startScreen));

            // define kernel injections
            Map<KernelInjectionEventEnum, KernelInjection> kernelInjectionMap = new MapBuilder<HashMap, KernelInjectionEventEnum, KernelInjection>(
                HashMap.class, System.out, false
            ).add(
                KernelInjectionEventEnum.PRE_PROCESSING, new PreProcessingKernelInjection(levelStateManager, environmentManager)
            ).get();

            // run the game kernel
            kernel.kernelMain(kernelInjectionMap);
        } catch (TerminalErrorException e) {
            PRINTER.printError(e);
        }

        // exit
        System.exit(0);
    }
}
