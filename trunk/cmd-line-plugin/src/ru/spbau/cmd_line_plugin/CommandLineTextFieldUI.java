package ru.spbau.cmd_line_plugin;

import com.intellij.ide.DataManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Author: Sergey A. Savenko
 */
public class CommandLineTextFieldUI extends JBTextField {

    private CommandLineModel model;
    private CommandLinePopup popup;
    private JBPopup suggestionsPopup;
    private JList suggestionsList;

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
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                updateSuggestions();
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractCommandLineAction action = model.getCommandLineAction(getText());
                if (null == action && null != suggestionsList) {
                    action = model.getCommandLineAction(suggestionsList.getSelectedValue());
                }
                if (null == action) {
                    JBPopupFactory.getInstance().createMessage("Action not found.").showUnderneathOf(popup.getComponent());
                } else {
                    performConsoleAction(action);
                }
            }
        });
    }

    private void updateSuggestions() {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                Object[] suggestions = model.getMatchingObjects(getText());
                closeSuggestionsPopup();
                if (0 == suggestions.length) {
                    return;
                }
                CommandLineTextFieldUI.this.suggestionsList = new JBList(suggestions);
                suggestionsList.setCellRenderer(model.getListCellRenderer());
                PopupChooserBuilder builder = JBPopupFactory.getInstance().createListPopupBuilder(suggestionsList);
                builder.setMinSize(getSize());
                builder.setRequestFocus(false);
                suggestionsPopup = builder.createPopup();
                suggestionsPopup.showUnderneathOf(popup.getComponent());
            }
        });
    }

    private void performConsoleAction(final AbstractCommandLineAction action) {
        close();
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                action.performAction();
            }
        });
    }

    private void closeSuggestionsPopup() {
        if (null != suggestionsPopup) {
            suggestionsPopup.closeOk(null);
        }
        suggestionsPopup = null;
        suggestionsList = null;
    }

    private void close() {
        closeSuggestionsPopup();
        popup.close();
    }

}
