package ru.spbau.cmd_line_plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Author: Sergey A. Savenko
 */
public interface CommandLineModel {

    @NotNull
    public Object[] getMatchingObjects(String text);

    @NotNull
    public ListCellRenderer getListCellRenderer();

    /**
     * Returns a command that corresponds to users input.
     * @param text - command line input
     * @return command line action or null if user's input is not a valid command.
     */
    @Nullable
    public AbstractCommandLineAction getCommandLineAction(String text);

    /**
     * Returns a command that corresponds to selected object (from suggestion list, for example).
     * @param suggestedItem
     * @return
     */
    public AbstractCommandLineAction getCommandLineAction(Object suggestedItem);

}
