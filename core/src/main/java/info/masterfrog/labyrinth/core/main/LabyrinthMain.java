package info.masterfrog.labyrinth.core.main;

import com.google.common.collect.ImmutableMap;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.hid.HIDEventEnum;
import info.masterfrog.labyrinth.core.entity.EntitiesManager;
import info.masterfrog.labyrinth.core.level.LevelManager;
import info.masterfrog.labyrinth.core.main.kernel.PreProcessingKernelInjection;
import info.masterfrog.labyrinth.core.enumeration.LevelHandle;
import info.masterfrog.pixelcat.engine.kernel.*;

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

            // initialize
            kernel.init(kernelStateInitProperties);
            KernelState kernelState = kernel.getKernelState();

            // set up general kernel action mappings
            KernelActionBinder kernelActionBinder = (KernelActionBinder) kernelState.getProperty(KernelStatePropertyEnum.KERNEL_ACTION_BINDER);
            kernelActionBinder.unbind(HIDEventEnum.F);

            // init game objects
            LevelHandle startingScreen = LevelHandle.START_SCREEN;
            LevelManager levelManager = new LevelManager(startingScreen);
            EntitiesManager entitiesManager = new EntitiesManager(kernelState);
            entitiesManager.init();
            kernel.registerGameObjectManagers(entitiesManager.getGameObjectManagerList(startingScreen));

            // define kernel injections
            Map<KernelInjectionEventEnum, KernelInjection> kernelInjectionMap = ImmutableMap.<KernelInjectionEventEnum, KernelInjection>of(
                KernelInjectionEventEnum.PRE_PROCESSING, new PreProcessingKernelInjection(levelManager, entitiesManager)
            );

            // run the game kernel
            kernel.kernelMain(kernelInjectionMap);
        } catch (TerminalErrorException e) {
            PRINTER.printError(e);
        }

        // exit
        System.exit(0);
    }
}
