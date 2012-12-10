package ru.spbau.cmd_line_plugin;

import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;
import ru.spbau.cmd_line_plugin.api.Command;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Set;
/**
 * Author: Pavel Chadnov
 */
public class CommandLineTextField extends JBTextField {

    private CommandLinePopup popup;
    private JBPopup suggestionsPopup;
    private JBList suggestionsList;
    private Set<Command> commands;
    private String currentText;
    private Component contextComponent;

    public CommandLineTextField(Set<Command> commands, CommandLinePopup popup, Component contextComponent) {
        super();
        this.popup = popup;
        this.commands = commands;
        this.contextComponent = contextComponent;
        initActions();
        initUI();
        currentText = "";
    }

    private ArrayList<Command> getSuggestedCommands(String s) {
        ArrayList<Command> ret = new ArrayList<Command>();
        for (Command command : this.commands) {
            if (command.getName().toLowerCase().contains(s.toLowerCase())) {
                ret.add(command);
            }
        }
        return ret;
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
                if (e.getKeyChar()==KeyEvent.VK_BACK_SPACE){
                    currentText = currentText.substring(0, currentText.length()-1);
                } else {
                    currentText += e.getKeyChar();
                }
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
                  for(Command command : commands) {
                      if (getText().startsWith(command.getName())) {
                          System.out.println("EXEC");
                          command.execute(getText(), contextComponent, new Object[0], null);
                      }
                  }
//                CommandLineAction action = model.getCommandLineAction(getText());
//                if (null == action && null != suggestionsList) {
//                    action = model.getCommandLineAction(suggestionsList.getSelectedValue());
//                }
//                if (null == action) {
//                    JBPopupFactory.getInstance().createMessage("Action not found.").showUnderneathOf(popup.getComponent());
//                } else {
//                    performConsoleAction(action);
//                }
            }
        });
    }

    private void updateSuggestions() {
        closeSuggestionsPopup();
        ArrayList<Command> matchingCommands = getSuggestedCommands(currentText);
        suggestionsList = new JBList(matchingCommands);
        if (suggestionsList.isEmpty()) {
            return;
        }
        PopupChooserBuilder builder = JBPopupFactory.getInstance().createListPopupBuilder(suggestionsList);
        builder.setMinSize(getSize());
        builder.setRequestFocus(false);
        suggestionsPopup = builder.createPopup();
        suggestionsPopup.showUnderneathOf(popup.getComponent());
    }

//    private void performConsoleAction(final AbstractCommandLineAction action) {
//        close();
//        ApplicationManager.getApplication().invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                action.performAction();
//            }
//        });
//    }

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