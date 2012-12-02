package ru.spbau.cmd_line_plugin;

/**
 * Abstract command line command.
 * Author: Sergey A. Savenko
 */
public abstract class AbstractCommandLineAction {

    private String commandLineText;

    public AbstractCommandLineAction(String commandLineText) {
        this.commandLineText = commandLineText;
    }

    /**
     * Implementations should run command on this method call.
     * Do not ever run this method on a gui thread.
     */
    public abstract void performAction();

}
