package ru.spbau.cmd_line_plugin;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.Nullable;
import ru.spbau.cmd_line_plugin.api.Command;
import ru.spbau.cmd_line_plugin.api.CommandResultHandler;
import ru.spbau.cmd_line_plugin.commands.CommandsLoader;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.Set;

/**
 * Author: Sergey A. Savenko
 */
public class CommandLineAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Set<Command> commands = ApplicationManager.getApplication().getComponent(CommandsLoader.class).getAllCommands();
        JBList cmdList = new JBList(commands.toArray());
        cmdList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JBPopupFactory.getInstance()
                .createListPopupBuilder(cmdList)
                .setItemChoosenCallback(new ActionChoosenCallback(cmdList, e.getData(DataKeys.CONTEXT_COMPONENT)))
                .createPopup()
                .showCenteredInCurrentWindow(e.getProject());
    }

    private class ActionChoosenCallback implements Runnable {

        private JBList cmdList;
        private Component contextComponent;

        public ActionChoosenCallback(JBList cmdList, Component contextComponent) {
            this.cmdList = cmdList;
            this.contextComponent = contextComponent;
        }

        @Override
        public void run() {
            Command cmd = (Command)cmdList.getModel().getElementAt(cmdList.getSelectedIndex());
            cmd.execute(cmd.getName() + " CMD_ARG", contextComponent, new Object[0], new CommandResultHandler() {
                @Override
                public void handleResult(Command cmd, boolean isOk, @Nullable String message) {
                    JBPopupFactory.getInstance()
                            .createMessage(cmd.toString() + " completed with status: " + (isOk ? "ok" : "fault") + " message: " + message)
                            .showInFocusCenter();
                }
            });
        }
    }

}
