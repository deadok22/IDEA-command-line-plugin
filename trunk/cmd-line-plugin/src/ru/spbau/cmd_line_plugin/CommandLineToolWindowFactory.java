package ru.spbau.cmd_line_plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import ru.spbau.cmd_line_plugin.interfaces.Command;
import ru.spbau.cmd_line_plugin.interfaces.CommandInterpreter;
import ru.spbau.cmd_line_plugin.interfaces.CommandTextHandler;
import ru.spbau.cmd_line_plugin.interpreter.CompoundCommandInterpreter;
import ru.spbau.cmd_line_plugin.interpreter.UiSettingsCommandInterpreter;
import ru.spbau.cmd_line_plugin.ui.TextFieldUI;

import java.awt.*;

/**
 * Author: Sergey A. Savenko
 */
public class CommandLineToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        TextFieldUI ui = new TextFieldUI();
        final CommandInterpreter interpreter = new CompoundCommandInterpreter(new CommandInterpreter[]{new UiSettingsCommandInterpreter()});
        ui.addCommandTextHandler(new CommandTextHandler() {
            @Override
            public void handleCommandText(String commandText) {
                if (null != commandText) {
                    String[] tokens = commandText.split("\\s");
                    //TODO pass some context to Command constructor
                    interpreter.interpretCommand(new Command(tokens, null));
                }
            }
        });
        Content c = ContentFactory.SERVICE.getInstance().createContent(ui.getComponent(), "CommandLinePluginUI", false);
        toolWindow.getContentManager().addContent(c);
    }

}
