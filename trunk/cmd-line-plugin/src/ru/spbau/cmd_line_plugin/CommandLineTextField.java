package ru.spbau.cmd_line_plugin;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nullable;
import ru.spbau.cmd_line_plugin.api.Command;
import ru.spbau.cmd_line_plugin.api.autocomplete.*;
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
    private String currentCompleteText;
    private String currentParam;
    private Component contextComponent;
    private Command currentCommand;

    private enum State {
        COMMAND, ARGS
    }

    private State state;
    private CompletionProvider completionProvider;
    private Object[] completions;
    private JBList suggestedCompletions;


    public CommandLineTextField(Set<Command> commands, CommandLinePopup popup, Component contextComponent) {
        super();
        this.popup = popup;
        this.commands = commands;
        this.contextComponent = contextComponent;
        initActions();
        initUI();
        currentText = "";
        currentCommand = null;
        state = State.COMMAND;
        completionProvider = null;
        completions = new Object[0];
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

    private void exec() {
        currentCommand.execute(getText(), contextComponent, new Object[0], new CommandResultHandler() {
            @Override
            public void handleResult(Command cmd, boolean isOk, @Nullable String message) {
                DataContext dataContext = DataManager.getInstance().getDataContext(contextComponent);
                String echoText = "Command " + cmd.getName() + " finished " + (isOk ? "correctly" : "incorrectly");
                if (message != null && !message.isEmpty()) {
                    echoText += "\nMessage: " + message;
                }
//                JBPopupFactory.getInstance()
//                        .createBalloonBuilder(new JBLabel(echoText))
//                        .setFillColor(isOk ? Color.GREEN : Color.RED)
//                        .setTitle("ECHO")
//                        .createBalloon()
//                        .showInCenterOf(suggestionsPopup.getContent());
                closeSuggestionsPopup();
            }
        });
    }

    private void initActions() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (state.equals(State.COMMAND)) {
                    if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                        if (!currentText.isEmpty()) {
                            currentText = currentText.substring(0, currentText.length() - 1);
                            currentCommand = null;
                        }
                    } else if (e.getKeyChar() != KeyEvent.VK_ENTER) {
                        currentText += e.getKeyChar();
                        currentCommand = null;
                    }
                    updateSuggestions();
                } else {
                    if (e.getKeyChar() == ' ') {
                        completionProvider = currentCommand.getCompletionProvider(getText(), completions, contextComponent);
                        currentParam = "";
                    } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                        if (currentParam.length() > 0) {
                            currentParam = currentParam.substring(0, currentParam.length() - 1);
                            updateSuggestions();
                        }
                    } else {
                        currentParam += e.getKeyChar();
                        updateSuggestions();
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (state.equals(State.COMMAND)) {
                    if (e.getKeyCode() == KeyEvent.VK_DOWN && suggestionsList!=null) {
                        suggestionsList.setSelectedIndex(suggestionsList.getSelectedIndex() + 1);
                    } else if (e.getKeyCode() == KeyEvent.VK_UP && suggestionsList!=null) {
                        suggestionsList.setSelectedIndex(suggestionsList.getSelectedIndex() - 1);
                    }
                    if (suggestionsList!=null) {
                    Command selectedCommand = (Command) suggestionsList.getSelectedValue();
                    if (selectedCommand != null) {
                            JBPopupFactory.getInstance()
                                    .createBalloonBuilder(new JBLabel(selectedCommand.getDescription()))
                                    .createBalloon()
                                    .showInCenterOf(suggestionsPopup.getContent());
                    }

                    }
                } else {
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        suggestionsList.setSelectedIndex(suggestionsList.getSelectedIndex() + 1);
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        suggestionsList.setSelectedIndex(suggestionsList.getSelectedIndex() - 1);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (state.equals(State.COMMAND)) {
                    boolean found = false;
                    for (Command command : commands) {
                        if (getText().toLowerCase().startsWith(command.getName().toLowerCase())) {
                            found = true;
                        }
                    }
                    if (!found) {
                        if (suggestionsList.getSelectedValue() == null)
                            return;
                        Command c = (Command) suggestionsList.getSelectedValue();
                        setText(c.getName() + " ");
                        currentText = getText();
                        currentCommand = c;
                        state = State.ARGS;
                        currentCompleteText = currentText;
                        completionProvider = c.getCompletionProvider(getText(), new Object[0], contextComponent);
                        currentParam = "";
                        JBPopupFactory.getInstance()
                                .createBalloonBuilder(new JBLabel(currentCommand.getHelpMessage()))
                                .setTitle(currentCommand.getName())
                                .createBalloon()
                                .showInCenterOf(suggestionsPopup.getContent());
                    }

                } else {
                    if (null == suggestionsList || suggestionsList.getSelectedValue() == null) {
                        exec();
                        closeSuggestionsPopup();
                        return;
                    }
                    currentCompleteText = currentCompleteText+((Completion)suggestionsList.getSelectedValue()).getText()+" ";
                    setText(currentCompleteText);
                    Object[] tmp = new Object[completions.length + 1];
                    for (int i = 0; i < completions.length; ++i)
                        tmp[i] = completions[i];
                    tmp[completions.length] = suggestionsList.getSelectedValue();
                    completions = tmp;
                    completionProvider = currentCommand.getCompletionProvider(getText(), completions, contextComponent);
                    exec();
                    closeSuggestionsPopup();
                }
            }
        });
    }


    ArrayList<Completion> getSuggestedParams(String s) {
        ArrayList<Completion> ret = new ArrayList<Completion>();
        if (completionProvider!=null)
            for (Completion t : completionProvider.getCompletions(s))
                ret.add(t);
        return ret;
    }

    private void updateSuggestions() {
        closeSuggestionsPopup();
        ArrayList matchingCommands = null;
        if (state == State.COMMAND)
            matchingCommands = getSuggestedCommands(currentText);
        else
            matchingCommands = getSuggestedParams(currentParam);
        if (matchingCommands.isEmpty()) {
            return;
        }
        suggestionsList = new JBList(matchingCommands);
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