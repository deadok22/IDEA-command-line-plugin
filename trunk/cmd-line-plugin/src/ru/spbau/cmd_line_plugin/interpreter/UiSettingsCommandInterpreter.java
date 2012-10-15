package ru.spbau.cmd_line_plugin.interpreter;

import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.actionSystem.DataContext;
import ru.spbau.cmd_line_plugin.interfaces.Command;
import ru.spbau.cmd_line_plugin.interfaces.CommandExecutor;
import ru.spbau.cmd_line_plugin.interfaces.CommandInterpreter;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Sergey A. Savenko
 */
public final class UiSettingsCommandInterpreter implements CommandInterpreter {

    private static final Map<String, CommandExecutor> KnownCommands = new HashMap<String, CommandExecutor>();

    static {
        KnownCommands.put(FontSettingsChanger.CMD_NAME, new FontSettingsChanger());
        //put your ui settings changers here.
    }

    @Override
    public boolean canInterpret(Command command) {
        return isValidCommand(command) && KnownCommands.containsKey(command.getTokens()[0]);
    }

    @Override
    public boolean interpretCommand(Command command) {
        return canInterpret(command) && KnownCommands.get(command.getTokens()[0]).executeCommand(command);
    }

    private boolean isValidCommand(Command command) {
         return !(null == command || null == command.getTokens() || 0 == command.getTokens().length);
    }

    private static class FontSettingsChanger implements CommandExecutor {

        private static final String CMD_NAME = "font";

        @Override
        public boolean executeCommand(Command command) {
            throw new UnsupportedOperationException("FontSettingsChanger.executeCommand() is not yet implemented");
        }

    }

}
