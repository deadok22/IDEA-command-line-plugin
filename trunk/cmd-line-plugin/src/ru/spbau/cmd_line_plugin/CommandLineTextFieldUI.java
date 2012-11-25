package ru.spbau.cmd_line_plugin;

import com.intellij.ui.components.JBTextField;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Author: Sergey A. Savenko
 */
public class CommandLineTextFieldUI extends JBTextField {

    private CommandLineModel model;

    public CommandLineTextFieldUI(CommandLineModel model) {
        super();
        this.model = model;

        initActions();
        initUI();
    }

    private void initUI() {
        setMinimumSize(new Dimension(200, getFontMetrics(getFont()).getHeight() + 4));
    }

    private void initActions() {
        //TODO add key listener and update ui
        addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractCommandLineAction action = model.getCommandLineAction(getText());
                if (null == action) {
                    //TODO notify user that his input is not a valid command
                } else {
                    //TODO close the popup
                    action.performAction();
                }
            }
        });
    }

}
