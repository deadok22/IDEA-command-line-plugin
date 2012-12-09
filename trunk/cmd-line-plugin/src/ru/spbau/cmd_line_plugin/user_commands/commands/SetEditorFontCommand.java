package ru.spbau.cmd_line_plugin.user_commands.commands;

import com.intellij.openapi.actionSystem.DataContext;
import org.jetbrains.annotations.NotNull;
import ru.spbau.cmd_line_plugin.api.Command;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProvider;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProviderFactory;

/**
 * Author: Sergey A. Savenko
 */
public class SetEditorFontCommand implements Command {

    private static final String NAME = "SetEditorFont";
    private static final String DESCRIPTION = "Sets current editor's font. (does not affect other editors)";

    @Override
    public String getName() {
        return NAME;
    }

    @NotNull
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public void execute(String text, DataContext dataContext, Object[] args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletionProvider getCompletionProvider(String text, Object[] args) {
        return CompletionProviderFactory.getStandardCompletionProvider(CompletionProviderFactory.Providers.FONT_FACE);
    }

}
