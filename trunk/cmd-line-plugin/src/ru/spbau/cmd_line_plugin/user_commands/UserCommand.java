package ru.spbau.cmd_line_plugin.user_commands;

import com.intellij.openapi.actionSystem.DataContext;

/**
 * Author: Sergey A. Savenko
 */
public interface UserCommand {

    String getName();

    void execute(String text, DataContext dataContext);

    //TODO add autocomplete providing functionality.

}
