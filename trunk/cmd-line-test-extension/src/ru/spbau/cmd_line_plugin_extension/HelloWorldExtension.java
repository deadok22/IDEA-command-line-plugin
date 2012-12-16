package ru.spbau.cmd_line_plugin_extension;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.ui.components.JBLabel;
import org.jetbrains.annotations.NotNull;
import ru.spbau.cmd_line_plugin.api.Command;
import ru.spbau.cmd_line_plugin.api.CommandLineCommandsProvider;
import ru.spbau.cmd_line_plugin.api.CommandResultHandler;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProvider;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProviderFactory;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Sergey A. Savenko
 */
public class HelloWorldExtension implements CommandLineCommandsProvider {

    @Override
    public Set<Command> getAllCommands() {
        Set<Command> cmds = new HashSet<Command>();
        cmds.add(new EchoCommand());
        return cmds;
    }

    private class EchoCommand extends Command {

        private static final String NAME = "echo";
        private static final String DESCRIPTION = "A command that shows the text passed to it in a balloon tip";
        private static final String HELP = "echo <text>";

        @NotNull
        @Override
        public String getName() {
            return NAME;
        }

        @NotNull
        @Override
        public String getDescription() {
            return DESCRIPTION;
        }

        @NotNull
        @Override
        public String getHelpMessage() {
            return HELP;
        }

        @Override
        public void execute(String text, Component contextComponent, @NotNull Object[] args, @NotNull CommandResultHandler resultHandler) {
            DataContext dataContext = DataManager.getInstance().getDataContext(contextComponent);
            if (!text.startsWith(NAME)) {
                resultHandler.handleResult(this, false, "wrong command text");
                return;
            }
            String echoText = text.substring(NAME.length()).trim();
            JBPopupFactory.getInstance()
                    .createBalloonBuilder(new JBLabel(echoText))
                    .setFillColor(Color.ORANGE)
                    .setTitle("ECHO")
                    .createBalloon()
                    .showInCenterOf(DataKeys.EDITOR.getData(dataContext).getComponent());
            resultHandler.handleResult(this, true, null);
        }

        @Override
        public CompletionProvider getCompletionProvider(String text, Object[] args) {
            return CompletionProviderFactory.getStandardCompletionProvider(null);
        }
    }

}
