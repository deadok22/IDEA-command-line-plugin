package ru.spbau.cmd_line_plugin.interpreter;

import com.intellij.ide.ui.LafManager;
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
            if (3 != command.getTokens().length) {
                return false;
            }
            if ("face".equals(command.getTokens()[1])) {
                UISettings settings = UISettings.getInstance();
                settings.OVERRIDE_NONIDEA_LAF_FONTS = true;
                settings.FONT_FACE = command.getTokens()[2];
                settings.fireUISettingsChanged();
                LafManager.getInstance().updateUI();
                return true;
            }
            if ("size".equals(command.getTokens()[1])) {
                UISettings settings = UISettings.getInstance();
                try {
                    settings.OVERRIDE_NONIDEA_LAF_FONTS = true;
                    settings.FONT_SIZE = Integer.parseInt(command.getTokens()[2]);
                } catch (NumberFormatException nfe) {
                    return false;
                }
                settings.fireUISettingsChanged();
                LafManager.getInstance().updateUI();
                return true;
            }
            throw new UnsupportedOperationException("Unknown command.");
        }

    }

}
