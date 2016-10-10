package info.masterfrog.labyrinth.core.main.kernel;

import com.google.common.collect.ImmutableSet;
import info.masterfrog.labyrinth.core.enumeration.EntityHandle;
import info.masterfrog.labyrinth.core.enumeration.LevelHandle;
import info.masterfrog.labyrinth.core.entity.EntitiesManager;
import info.masterfrog.labyrinth.core.level.LevelManager;
import info.masterfrog.labyrinth.core.level.StartScreenLevelManager;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.GameErrorCode;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.exception.TerminalGameException;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.hid.HIDEventEnum;
import info.masterfrog.pixelcat.engine.kernel.*;
import info.masterfrog.pixelcat.engine.logic.gameobject.feature.Renderable;
import info.masterfrog.labyrinth.core.enumeration.GameObjectManagerHandle;

import java.awt.*;

public class PreProcessingKernelInjection implements KernelInjection {
    private LevelManager levelManager;
    private EntitiesManager entitiesManager;

    private static final Printer PRINTER = PrinterFactory.getInstance().createPrinter(PreProcessingKernelInjection.class);

    public PreProcessingKernelInjection(LevelManager levelManager, EntitiesManager entitiesManager) {
        this.levelManager = levelManager;
        this.entitiesManager = entitiesManager;
    }

    public void run(KernelState kernelState) throws TransientGameException {
        try {
            PRINTER.printTrace("Labyrinth kernel-injected pre-processor started...");

            // handle exit trigger binding
            if (entitiesManager.getFlag(EntitiesManager.FLAG__START_INITIALIZED) && !entitiesManager.getFlag(EntitiesManager.FLAG__EXIT_KEY_BOUND)) {
                KernelActionBinder kernelActionBinder = (KernelActionBinder) kernelState.getProperty(KernelStatePropertyEnum.KERNEL_ACTION_BINDER);
                kernelActionBinder.bind(HIDEventEnum.ESC, KernelActionEnum.EXIT);
                entitiesManager.setFlag(EntitiesManager.FLAG__EXIT_KEY_BOUND, true);
            }

            // level management
            switch (levelManager.getCurrentLevel()) {
                case START_SCREEN:
                    StartScreenLevelManager.getInstance().run(kernelState);
                    break;
                default:
                    throw new TerminalErrorException(ImmutableSet.of(new TerminalGameException(GameErrorCode.LOGIC_ERROR, "Invalid level transition")));
            }
        } catch (TerminalErrorException e) {
            PRINTER.printError(e);

            throw new TransientGameException(GameErrorCode.KERNEL_INJECTION_ERROR, e);
        }

        PRINTER.printTrace("Labyrinth kernel-injected pre-processor ended...");
    }

}
