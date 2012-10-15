package ru.spbau.deadok22.hello_world_tool_window;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * Author: Sergey A. Savenko
 *
 */
public class HelloWorldToolWindowFactory implements ToolWindowFactory {
    private JPanel content;
    private JTextField textField;

    public HelloWorldToolWindowFactory() {
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Messages.showInfoMessage(textField.getText(), "HelloWorldToolWindow plugin notification");
            }
        });
    }

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        Content c = ContentFactory.SERVICE.getInstance().createContent(content, "HelloWorldToolWindow", false);
        toolWindow.getContentManager().addContent(c);
    }

}
