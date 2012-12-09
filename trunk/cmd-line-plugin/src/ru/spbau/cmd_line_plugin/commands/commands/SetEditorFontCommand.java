package ru.spbau.cmd_line_plugin.commands.commands;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorFontType;
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
    public void execute(String text, DataContext dataContext, @NotNull Object[] args, CommandResultHandler resultHandler) {
        Font font = getFont(text, args);
        if (null == font) {
            resultHandler.handleResult(this, false, "No font specified");
            return;
        }
        Editor editor = DataKeys.EDITOR_EVEN_IF_INACTIVE.getData(dataContext);
        if (null == editor) {
            resultHandler.handleResult(this, false, "No editor opened");
            return;
        }
        editor.getColorsScheme().setFont(EditorFontType.PLAIN, font);
        resultHandler.handleResult(this, true, null);
    }

    @Override
    public CompletionProvider getCompletionProvider(String text, Object[] args) {
        return CompletionProviderFactory.getStandardCompletionProvider(CompletionProviderFactory.Providers.FONT_FACE);
    }

    private Font getFont(String cmdText, Object[] args) {
        if (null != args && 1 == args.length && args[0] instanceof Font) {
            return (Font)args[0];
        }
        if (null != cmdText && cmdText.startsWith(NAME)) {
            String fontName = cmdText.substring(NAME.length()).trim();
            Completion[] completions = getCompletionProvider(null, null).getCompletions(fontName);
            if (0 == completions.length) {
                return null;
            }
            return (Font)completions[0].getObject();
        }
        return null;
    }

}
