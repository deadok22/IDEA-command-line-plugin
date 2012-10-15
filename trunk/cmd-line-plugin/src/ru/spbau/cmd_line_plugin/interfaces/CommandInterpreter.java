package ru.spbau.cmd_line_plugin.interfaces;

/**
 *
 * Author: Sergey A. Savenko
 */
public interface CommandInterpreter {

    /**
     * Determines whether the command is known to this interpreter.
     * @param command
     * @return true if command is known to this interpreter, else false.
     */
    boolean canInterpret(Command command);

    /**
     * Interpret passed command
     * @param command
     * @return true on success, false on failure
     */
    boolean interpretCommand(Command command);

}
