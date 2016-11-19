package info.masterfrog.labyrinth.entity;

import info.masterfrog.labyrinth.enumeration.CanvasHandle;
import info.masterfrog.pixelcat.engine.common.printer.Printer;
import info.masterfrog.pixelcat.engine.common.printer.PrinterFactory;
import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;
import info.masterfrog.pixelcat.engine.kernel.CanvasManager;
import info.masterfrog.pixelcat.engine.kernel.KernelState;
import info.masterfrog.pixelcat.engine.kernel.KernelStateProperty;
import info.masterfrog.pixelcat.engine.logic.gameobject.element.aspect.canvas.Canvas;

import java.awt.Rectangle;

public class EnvironmentConfigurator {
    private static EnvironmentConfigurator instance;

    private static final Printer PRINTER = PrinterFactory.getInstance().createPrinter(EnvironmentConfigurator.class);

    public static EnvironmentConfigurator getInstance() {
        if (instance == null) {
            instance = new EnvironmentConfigurator();
        }

        return instance;
    }

    private EnvironmentConfigurator() {
        // do nothing
    }

    public void init() throws TransientGameException {
        initCanvases();
    }

    private void initCanvases() throws TransientGameException{
        // setup
        CanvasManager canvasManager = KernelState.getInstance().getProperty(KernelStateProperty.CANVAS_MANAGER);
        Rectangle screenBounds = KernelState.getInstance().getProperty(KernelStateProperty.SCREEN_BOUNDS);

        // init main canvas
        EnvironmentManager.getInstance().registerCanvas(
            CanvasHandle.MAIN,
            canvasManager.getDefault().getId()
        );

        // init room canvas
        Canvas roomCanvas = Canvas.create(
            new Rectangle(
                screenBounds.width * 5,
                screenBounds.height * 5
            )
        ).setScaleFactor(3.0);
        canvasManager.add(roomCanvas);
        EnvironmentManager.getInstance().registerCanvas(
            CanvasHandle.ROOM,
            roomCanvas.getId()
        );
    }
}
