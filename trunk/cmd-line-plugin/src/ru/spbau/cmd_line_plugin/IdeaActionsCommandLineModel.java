package ru.spbau.cmd_line_plugin;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.LayeredIcon;
import com.intellij.util.containers.HashMap;
import com.intellij.util.ui.EmptyIcon;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 * Author: Sergey A. Savenko
 */
public class IdeaActionsCommandLineModel implements CommandLineModel {

    private static Icon EMPTY_ICON = EmptyIcon.ICON_18;

    private Component commandLineActionComponent;
    private String[] allActionIds;

    public IdeaActionsCommandLineModel(Component commandLineActionComponent) {
        this.commandLineActionComponent = commandLineActionComponent;
        allActionIds = fetchAllActionIds();
    }

    String[] fetchAllActionIds() {
        return ActionManager.getInstance().getActionIds("");
    }

    @NotNull
    @Override
    public Object[] getMatchingObjects(String text) {
        if (StringUtil.isEmpty(text)) {
            return new Object[0];
        }

        Pattern pattern = getPattern(text);
        ArrayList<String> matchedIds = new ArrayList<String>();
        for (String actionId : allActionIds) {
            if (pattern.matcher(actionId).matches()) {
                matchedIds.add(actionId);
            }
        }

        Collections.sort(matchedIds);

        Object[] matchedActions = new Object[matchedIds.size()];
        int i = 0;
        for (String matchedId : matchedIds) {
            matchedActions[i++] = ActionManager.getInstance().getAction(matchedId);
        }
        return matchedActions;
    }

    private Pattern getPattern(String text) {
        return Pattern.compile(".*" + text.trim() + ".*", Pattern.CASE_INSENSITIVE);
    }

    @NotNull
    @Override
    public ListCellRenderer getListCellRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                final JPanel panel = new JPanel(new BorderLayout());
                panel.setOpaque(true);
                final Color bg = isSelected ? UIUtil.getListSelectionBackground() : UIUtil.getListBackground();
                panel.setBackground(bg);
                if (value instanceof AnAction) {
                    final AnAction action = (AnAction) value;
                    final Icon icon = action.getTemplatePresentation().getIcon();
                    JLabel label = createActionLabel(ActionManager.getInstance().getId(action), isSelected, icon);
                    panel.add(label, BorderLayout.WEST);
                } else {
                    //TODO render other suggestions here
                }
                return panel;
            }
        };
    }

    protected JLabel createActionLabel(String anActionName, boolean selected, final Icon icon) {
        final LayeredIcon layeredIcon = new LayeredIcon(2);
        layeredIcon.setIcon(EMPTY_ICON, 0);
        if (icon != null) {
            int hShift = (-icon.getIconWidth() + EMPTY_ICON.getIconWidth()) / 2;
            int vShift = (EMPTY_ICON.getIconHeight() - icon.getIconHeight()) / 2;
            layeredIcon.setIcon(icon, 1, hShift, vShift);
        }

        final JLabel actionLabel = new JLabel(anActionName, layeredIcon, SwingConstants.LEFT);
        actionLabel.setBackground(selected ? UIUtil.getListSelectionBackground() : UIUtil.getListBackground());
        actionLabel.setForeground(selected ? UIUtil.getListSelectionForeground() :UIUtil.getListForeground());
        return actionLabel;
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
