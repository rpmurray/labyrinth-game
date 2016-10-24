package info.masterfrog.labyrinth.entity;

import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.kernel.KernelState;

public class EnvironmentConfigurator {
    private KernelState kernelState;
    private EnvironmentManager environmentManager;

    private static Printer PRINTER = PrinterFactory.getInstance().createPrinter(EnvironmentConfigurator.class);

    public EnvironmentConfigurator(KernelState kernelState, EnvironmentManager environmentManager) {
        this.kernelState = kernelState;
        this.environmentManager = environmentManager;
    }

    public void init() throws TerminalErrorException {
        // do nothing
    }
}
