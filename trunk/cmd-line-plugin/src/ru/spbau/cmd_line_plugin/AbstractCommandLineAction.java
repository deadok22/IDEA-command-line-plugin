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
     * Call this method via ApplicationManager.getApplication().invokeLater() only.
     */
    public abstract void performAction();

}
