package ru.spbau.cmd_line_plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import ru.spbau.cmd_line_plugin.api.Command;

import java.awt.*;
import java.util.Set;


/**
 * Author: Pavel Chadnov
 */

public class CommandLineUI implements CommandLinePopup {

    private JBPopup popup;
    private Project project;

    public CommandLineUI(Set<Command> commands, Project project, Component contextComponent) {
        CommandLineTextField textField = new CommandLineTextField(commands, this, contextComponent);
        ComponentPopupBuilder popupBuilder = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(textField, textField.getPreferredFocusComponent());
        popupBuilder.setTitle("Enter command:");
        popupBuilder.setFocusable(true);
        popupBuilder.setRequestFocus(true);
        popup = popupBuilder.createPopup();
        this.project = project;
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