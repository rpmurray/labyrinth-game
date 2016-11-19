package info.masterfrog.labyrinth.main.kernel;

import info.masterfrog.labyrinth.entity.EnvironmentManager;
import info.masterfrog.labyrinth.exception.LabyrinthGameErrorCode;
import info.masterfrog.labyrinth.level.LabyrinthLevelManager;
import info.masterfrog.labyrinth.level.LevelStateManager;
import info.masterfrog.labyrinth.level.StartScreenLevelManager;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.common.util.SetBuilder;
import info.masterfrog.pixelcat.engine.exception.GameEngineErrorCode;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.exception.TerminalGameException;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.hid.HIDEvent;
import info.masterfrog.pixelcat.engine.kernel.KernelAction;
import info.masterfrog.pixelcat.engine.kernel.KernelActionBinder;
import info.masterfrog.pixelcat.engine.kernel.KernelInjection;
import info.masterfrog.pixelcat.engine.kernel.KernelState;
import info.masterfrog.pixelcat.engine.kernel.KernelStateProperty;

import java.util.HashSet;

public class PreProcessingKernelInjection implements KernelInjection {
    private LevelStateManager levelStateManager;
    private EnvironmentManager environmentManager;
    private StartScreenLevelManager startScreenLevelManager;
    private LabyrinthLevelManager labyrinthLevelManager;

    private static final Printer PRINTER = PrinterFactory.getInstance().createPrinter(PreProcessingKernelInjection.class);

    public PreProcessingKernelInjection() throws TransientGameException {
        this.environmentManager = EnvironmentManager.getInstance();
        this.levelStateManager = LevelStateManager.getInstance();
        this.startScreenLevelManager = StartScreenLevelManager.getInstance();
        this.labyrinthLevelManager = LabyrinthLevelManager.getInstance();
    }

    public void run() throws TransientGameException {
        try {
            PRINTER.printTrace("Labyrinth kernel-injected pre-processor started...");

            // handle exit trigger binding
            if (environmentManager.getFlag(EnvironmentManager.FLAG__START_INITIALIZED) && !environmentManager.getFlag(EnvironmentManager.FLAG__EXIT_KEY_BOUND)) {
                KernelActionBinder kernelActionBinder = KernelState.getInstance().getProperty(KernelStateProperty.KERNEL_ACTION_BINDER);
                kernelActionBinder.bind(HIDEvent.ESC, KernelAction.EXIT);
                environmentManager.setFlag(EnvironmentManager.FLAG__EXIT_KEY_BOUND, true);
            }

            // level management
            switch (levelStateManager.getCurrentLevel()) {
                case START_SCREEN:
                    if (!environmentManager.getFlag(EnvironmentManager.FLAG__START_INITIALIZED)) {
                        startScreenLevelManager.init();
                    }
                    startScreenLevelManager.run();
                    break;
                case LABYRINTH:
                    if (!environmentManager.getFlag(EnvironmentManager.FLAG__LABYRINTH_INITIALIZED + levelStateManager.getCurrentLabyrinthIndex())) {
                        labyrinthLevelManager.init();
                    }
                    labyrinthLevelManager.run();
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
