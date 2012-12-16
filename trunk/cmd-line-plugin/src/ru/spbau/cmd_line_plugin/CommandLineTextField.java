package ru.spbau.cmd_line_plugin;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nullable;
import ru.spbau.cmd_line_plugin.api.Command;
import ru.spbau.cmd_line_plugin.api.CommandResultHandler;

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
                    if (!currentText.isEmpty()) {
                        currentText = currentText.substring(0, currentText.length()-1);
                    }
                } else {
                    currentText += e.getKeyChar();
                }
                updateSuggestions();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_DOWN) {
                    suggestionsList.setSelectedIndex(suggestionsList.getSelectedIndex()+1);
                } else if (e.getKeyCode()==KeyEvent.VK_UP) {
                    suggestionsList.setSelectedIndex(suggestionsList.getSelectedIndex()-1);
                }
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
                          //TODO replace new CommandResultHandler() with real implementation.
                          command.execute(getText(), contextComponent, new Object[0], new CommandResultHandler() {
                              @Override
                              public void handleResult(Command cmd, boolean isOk, @Nullable String message) {
                                  DataContext dataContext = DataManager.getInstance().getDataContext(contextComponent);
                                  String echoText = "Command "+cmd.getName()+"finished " + (isOk ? "correctly" : "incorrectly");
                                  if (message!=null && !message.isEmpty()) {
                                      echoText += "\nMessage: "+message;
                                  }
                                  JBPopupFactory.getInstance()
                                          .createBalloonBuilder(new JBLabel(echoText))
                                          .setFillColor(isOk ? Color.GREEN : Color.RED)
                                          .setTitle("ECHO")
                                          .createBalloon()
                                          .showInCenterOf(DataKeys.EDITOR.getData(dataContext).getComponent());
                              }
                          });
                      }
                  }
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