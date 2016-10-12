package info.masterfrog.labyrinth.main.kernel;

import com.google.common.collect.ImmutableSet;
import info.masterfrog.labyrinth.entity.EntitiesEnvironmentManager;
import info.masterfrog.labyrinth.level.LevelManager;
import info.masterfrog.labyrinth.level.StartScreenLevelManager;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.GameErrorCode;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.exception.TerminalGameException;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.hid.HIDEventEnum;
import info.masterfrog.pixelcat.engine.kernel.*;

public class PreProcessingKernelInjection implements KernelInjection {
    private LevelManager levelManager;
    private EntitiesEnvironmentManager entitiesEnvironmentManager;

    private static final Printer PRINTER = PrinterFactory.getInstance().createPrinter(PreProcessingKernelInjection.class);

    public PreProcessingKernelInjection(LevelManager levelManager, EntitiesEnvironmentManager entitiesEnvironmentManager) {
        this.levelManager = levelManager;
        this.entitiesEnvironmentManager = entitiesEnvironmentManager;
    }

    public void run(KernelState kernelState) throws TransientGameException {
        try {
            PRINTER.printTrace("Labyrinth kernel-injected pre-processor started...");

            // handle exit trigger binding
            if (entitiesEnvironmentManager.getFlag(EntitiesEnvironmentManager.FLAG__START_INITIALIZED) && !entitiesEnvironmentManager.getFlag(EntitiesEnvironmentManager.FLAG__EXIT_KEY_BOUND)) {
                KernelActionBinder kernelActionBinder = (KernelActionBinder) kernelState.getProperty(KernelStatePropertyEnum.KERNEL_ACTION_BINDER);
                kernelActionBinder.bind(HIDEventEnum.ESC, KernelActionEnum.EXIT);
                entitiesEnvironmentManager.setFlag(EntitiesEnvironmentManager.FLAG__EXIT_KEY_BOUND, true);
            }

            // level management
            switch (levelManager.getCurrentLevel()) {
                case START_SCREEN:
                    StartScreenLevelManager.getInstance().run(kernelState, levelManager, entitiesEnvironmentManager);
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
