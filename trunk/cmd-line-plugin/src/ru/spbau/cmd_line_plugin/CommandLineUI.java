package ru.spbau.cmd_line_plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;

import java.awt.*;

/**
 * Author: Sergey A. Savenko
 */
public class CommandLineUI implements CommandLinePopup {

    private JBPopup popup;
    private Project project;

    public CommandLineUI(CommandLineModel model, Project p) {
        CommandLineTextFieldUI ui = new CommandLineTextFieldUI(model, this);
        ComponentPopupBuilder popupBuilder = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(ui, ui.getPreferredFocusComponent());
        popupBuilder.setTitle("Command line plugin");
        popupBuilder.setFocusable(true);
        popupBuilder.setRequestFocus(true);
        popup = popupBuilder.createPopup();
        project = p;
    }

    public void show() {
        popup.showCenteredInCurrentWindow(project);
    }

    @Override
    public void close() {
        popup.closeOk(null);
    }

    @Override
    public Component getComponent() {
        return popup.getContent();
    }

}
