package ru.spbau.cmd_line_plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;

/**
 * Author: Sergey A. Savenko
 */
public class CommandLineAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        CommandLineModel model = new IdeaActionsCommandLineModel(e.getData(DataKeys.CONTEXT_COMPONENT));
        CommandLineUI ui = new CommandLineUI(model, e.getProject());
        ui.show();
    }

}
