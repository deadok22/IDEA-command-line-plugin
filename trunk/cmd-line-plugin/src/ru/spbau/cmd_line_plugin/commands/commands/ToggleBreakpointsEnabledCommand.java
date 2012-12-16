package ru.spbau.cmd_line_plugin.commands.commands;

import com.intellij.debugger.DebuggerManagerEx;
import com.intellij.debugger.ui.breakpoints.Breakpoint;
import com.intellij.debugger.ui.breakpoints.BreakpointManager;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import ru.spbau.cmd_line_plugin.api.Command;
import ru.spbau.cmd_line_plugin.api.CommandResultHandler;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProvider;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProviderFactory;

import java.awt.*;

/**
 * Author: Sergey A. Savenko
 */
public class ToggleBreakpointsEnabledCommand extends Command {

    private static final String NAME = "ToggleBreakpointsEnabled";
    private static final String DESCRIPTION = "Toggle all breakpoints enabled/disabled";
    private static final String HELP = "ToggleBreakpointsEnabled <no_args>";

    @NotNull
    @Override
    public String getName() {
        return NAME;
    }

    @NotNull
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @NotNull
    @Override
    public String getHelpMessage() {
        return HELP;
    }

    @Override
    public void execute(String text, Component contextComponent, @NotNull Object[] args, @NotNull CommandResultHandler resultHandler) {
        Project project = DataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(contextComponent));
        if (null == project) {
            resultHandler.handleResult(this, false, "Project not found");
        }
        BreakpointManager breakpointManager = DebuggerManagerEx.getInstanceEx(project).getBreakpointManager();
        for (Breakpoint bp : breakpointManager.getBreakpoints()) {
            breakpointManager.setBreakpointEnabled(bp, !bp.ENABLED);
        }
        resultHandler.handleResult(this, true, null);
    }

    @Override
    public CompletionProvider getCompletionProvider(String text, Object[] args) {
        return CompletionProviderFactory.getStandardCompletionProvider(null);
    }
}
