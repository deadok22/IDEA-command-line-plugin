package ru.spbau.cmd_line_plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBList;
import ru.spbau.cmd_line_plugin.api.Command;
import ru.spbau.cmd_line_plugin.commands.CommandsLoader;

import java.util.Set;

/**
 * Author: Sergey A. Savenko
 */
public class CommandLineAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Set<Command> commands = ApplicationManager.getApplication().getComponent(CommandsLoader.class).getAllCommands();
        JBPopupFactory.getInstance()
                .createListPopupBuilder(new JBList(commands.toArray()))
                .createPopup()
                .showCenteredInCurrentWindow(e.getProject());
    }

}
