package info.masterfrog.labyrinth.level;

import info.masterfrog.pixelcat.engine.exception.TerminalErrorException;
import info.masterfrog.pixelcat.engine.exception.TransientGameException;

public interface LevelManager {
    void init() throws TransientGameException;

    void run() throws TransientGameException, TerminalErrorException;
}
