package ru.spbau.cmd_line_plugin.commands.commands;

import com.intellij.openapi.actionSystem.DataContext;
import org.jetbrains.annotations.NotNull;
import ru.spbau.cmd_line_plugin.api.Command;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProvider;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProviderFactory;

/**
 * Author: Sergey A. Savenko
 */
public class SetEditorFontCommand extends Command {

    private static final String NAME = "SetEditorFont";
    private static final String DESCRIPTION = "Sets current editor's font. (does not affect other editors)";
    private static final String HELP = NAME + " <font_name>";

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
    public void execute(String text, DataContext dataContext, Object[] args) {
        //TODO implement
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletionProvider getCompletionProvider(String text, Object[] args) {
        return CompletionProviderFactory.getStandardCompletionProvider(CompletionProviderFactory.Providers.FONT_FACE);
    }

}
