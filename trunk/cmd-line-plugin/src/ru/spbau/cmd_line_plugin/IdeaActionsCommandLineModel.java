package ru.spbau.cmd_line_plugin;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Author: Sergey A. Savenko
 */
public class IdeaActionsCommandLineModel implements CommandLineModel {

    private Component commandLineActionComponent;

    public IdeaActionsCommandLineModel(Component commandLineActionComponent) {
        this.commandLineActionComponent = commandLineActionComponent;
    }

    @NotNull
    @Override
    public Object[] getMatchingObjects(String text) {
        //TODO search for actions
        return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    @Override
    public ListCellRenderer getCellRenderer(Object object) {
        //TODO add action rendering
        return new DefaultListCellRenderer();
    }

    @Nullable
    @Override
    public AbstractCommandLineAction getCommandLineAction(String text) {
        AnAction action = ActionManager.getInstance().getAction(text);
        if (null == action) {
            return null;
        }
        DataContext context = DataManager.getInstance().getDataContext(commandLineActionComponent);
        return new IdeaActionCommandLineAction(text, action, context);
    }

    class IdeaActionCommandLineAction extends AbstractCommandLineAction {

        private AnAction action;
        private DataContext context;

        public IdeaActionCommandLineAction(String commandLineText, AnAction action, DataContext context) {
            super(commandLineText);
            this.action = action;
            this.context = context;
        }

        @Override
        public void performAction() {
            AnActionEvent e =
                    new AnActionEvent(null, context, ActionPlaces.UNKNOWN, new Presentation(), ActionManager.getInstance(), 0);
            action.actionPerformed(e);
        }
    }

}
