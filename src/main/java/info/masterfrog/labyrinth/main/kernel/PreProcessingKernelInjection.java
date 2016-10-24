package info.masterfrog.labyrinth.main.kernel;

import info.masterfrog.labyrinth.entity.EnvironmentManager;
import info.masterfrog.labyrinth.exception.LabyrinthGameErrorCode;
import info.masterfrog.labyrinth.level.LevelStateManager;
import info.masterfrog.labyrinth.level.StartScreenLevelManager;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.common.util.SetBuilder;
import info.masterfrog.pixelcat.engine.exception.*;
import info.masterfrog.pixelcat.engine.hid.HIDEventEnum;
import info.masterfrog.pixelcat.engine.kernel.*;

import java.util.HashSet;

public class PreProcessingKernelInjection implements KernelInjection {
    private LevelStateManager levelStateManager;
    private EnvironmentManager environmentManager;

    private static final Printer PRINTER = PrinterFactory.getInstance().createPrinter(PreProcessingKernelInjection.class);

    public PreProcessingKernelInjection(LevelStateManager levelStateManager, EnvironmentManager environmentManager) {
        this.levelStateManager = levelStateManager;
        this.environmentManager = environmentManager;
    }

    public void run(KernelState kernelState) throws TransientGameException {
        try {
            PRINTER.printTrace("Labyrinth kernel-injected pre-processor started...");

            // handle exit trigger binding
            if (environmentManager.getFlag(EnvironmentManager.FLAG__START_INITIALIZED) && !environmentManager.getFlag(EnvironmentManager.FLAG__EXIT_KEY_BOUND)) {
                KernelActionBinder kernelActionBinder = (KernelActionBinder) kernelState.getProperty(KernelStatePropertyEnum.KERNEL_ACTION_BINDER);
                kernelActionBinder.bind(HIDEventEnum.ESC, KernelActionEnum.EXIT);
                environmentManager.setFlag(EnvironmentManager.FLAG__EXIT_KEY_BOUND, true);
            }

            // level management
            switch (levelStateManager.getCurrentLevel()) {
                case START_SCREEN:
                    if (!environmentManager.getFlag(EnvironmentManager.FLAG__START_INITIALIZED)) {
                        StartScreenLevelManager.getInstance().init(kernelState, environmentManager);
                    }
                    StartScreenLevelManager.getInstance().run(kernelState, levelStateManager, environmentManager);
                    break;
                default:
                    throw new TerminalErrorException(new SetBuilder<HashSet, TerminalGameException>(HashSet.class, System.out, false).add(
                        new TerminalGameException(LabyrinthGameErrorCode.LEVEL__CORE__UNDEFINED_LEVEL, levelStateManager.getCurrentLevel())
                    ).get());
            }
        } catch (TerminalErrorException e) {
            PRINTER.printError(e);

            throw new TransientGameException(GameEngineErrorCode.KERNEL_INJECTION_ERROR, e);
        }

        PRINTER.printTrace("Labyrinth kernel-injected pre-processor ended...");
    }

}
