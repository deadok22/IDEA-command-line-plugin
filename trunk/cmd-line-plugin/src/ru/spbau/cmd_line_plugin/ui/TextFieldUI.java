package ru.spbau.cmd_line_plugin.ui;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import ru.spbau.cmd_line_plugin.interfaces.CommandTextHandler;
import ru.spbau.cmd_line_plugin.interfaces.CommandTextProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Author: Sergey A. Savenko
 */
public class TextFieldUI implements CommandTextProvider {
    private JPanel content;
    private JTextField textField;

    public JComponent getComponent() {
        return content;
    }

    @Override
    public void addCommandTextHandler(final CommandTextHandler h) {
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                h.handleCommandText(textField.getText());
            }
        });
    }

}
