package ru.spbau.cmd_line_plugin.api;

import java.util.Set;

/**
 * Extension point interface.
 *
 * Author: Sergey A. Savenko
 */
public interface CommandLineCommandsProvider {

    /**
     * Implementers should return a set of all supported commands for their plugin.
     *
     * @return
     */
    public Set<Command> getAllCommands();

}
