package ru.spbau.cmd_line_plugin.commands.commands;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;
import ru.spbau.cmd_line_plugin.api.Command;
import ru.spbau.cmd_line_plugin.api.CommandResultHandler;
import ru.spbau.cmd_line_plugin.api.autocomplete.Completion;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProvider;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProviderFactory;

import java.awt.*;

/**
 * Author: Sergey A. Savenko
 */
public class ShowLineNumbersCommand extends Command {

    private static final String NAME = "ShowLineNumbers";
    private static final String DESCRIPTION = "Shows/hides line numbers in current editor";
    private static final String HELP = "ShowLineNumbers <true|false>";

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
        Editor currentEditor = DataKeys.EDITOR.getData(DataManager.getInstance().getDataContext(contextComponent));
        if (null == currentEditor) {
            resultHandler.handleResult(this, false, "editor not found");
            return;
        }
        if (0 != args.length) {
            setLineNumbersShown((Boolean)args[0], currentEditor);
            resultHandler.handleResult(this, true, null);
        } else {
            String argsText = getArgumentsString(text);
            CompletionProvider cp = getCompletionProvider(getName(), new Object[0], contextComponent);
            Completion[] completions = cp.getCompletions(argsText);
            if (1 != completions.length) {
                resultHandler.handleResult(this, false, "invalid arguments");
            } else {
                setLineNumbersShown((Boolean)completions[0].getObject(), currentEditor);
                resultHandler.handleResult(this, true, null);
            }
        }
    }

    @Override
    public CompletionProvider getCompletionProvider(String text, Object[] args, Component contextComponent) {
        String argsText = getArgumentsString(text);
        if ((null == args || 0 == args.length) && (null == argsText || argsText.isEmpty())) {
            return CompletionProviderFactory.getStandardCompletionProvider(CompletionProviderFactory.Providers.BOOLEAN);
        }
        return CompletionProviderFactory.getStandardCompletionProvider(null);
    }

    private void setLineNumbersShown(boolean areShown, Editor currentEditor) {
        currentEditor.getSettings().setLineNumbersShown(areShown);
    }

}
