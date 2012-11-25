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
        //TODO remove ui code from here.
        CommandLineModel model = new IdeaActionsCommandLineModel(e.getData(DataKeys.CONTEXT_COMPONENT));
        ComponentPopupBuilder popupBuilder = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(new CommandLineTextFieldUI(model), null);
        popupBuilder.setTitle("Command line plugin");
        popupBuilder.createPopup().showCenteredInCurrentWindow(e.getProject());

    }

}
