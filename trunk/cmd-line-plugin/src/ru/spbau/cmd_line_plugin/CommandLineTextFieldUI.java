package ru.spbau.cmd_line_plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBTextField;

import javax.swing.*;
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
    private CommandLinePopup popup;

    public CommandLineTextFieldUI(CommandLineModel model, CommandLinePopup popup) {
        super();
        this.model = model;
        this.popup = popup;

        initActions();
        initUI();
    }

    public JComponent getPreferredFocusComponent() {
        return this;
    }

    private void initUI() {
        setMinimumSize(new Dimension(200, getFontMetrics(getFont()).getHeight() + 4));
    }

    private void initActions() {
        //TODO add key listener and update ui
        addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                final AbstractCommandLineAction action = model.getCommandLineAction(getText());
                if (null == action) {
                    JBPopupFactory.getInstance().createMessage("Action not found.").showUnderneathOf(popup.getComponent());

                } else {
                    popup.close();
                    ApplicationManager.getApplication().invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            action.performAction();
                        }
                    });
                }
            }
        });
    }

}
