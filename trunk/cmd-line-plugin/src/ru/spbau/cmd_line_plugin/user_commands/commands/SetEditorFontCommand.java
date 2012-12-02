package ru.spbau.cmd_line_plugin.user_commands.commands;

import ru.spbau.cmd_line_plugin.user_commands.UserCommand;

/**
 * Author: Sergey A. Savenko
 */
public class SetEditorFontCommand implements UserCommand {

    private static final String NAME = "SetEditorFont";

    @Override
    public String getName() {
        return NAME;
    }

}
