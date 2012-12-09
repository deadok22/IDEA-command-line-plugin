package ru.spbau.cmd_line_plugin.api;

import com.intellij.openapi.actionSystem.DataContext;
import org.jetbrains.annotations.NotNull;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProvider;

/**
 * Author: Sergey A. Savenko
 */
public interface Command {

    /**
     * Get name which can be used to search this command.
     * @return - name
     */
    @NotNull
    String getName();

    /**
     * Command description. Should contain basic description of what the command is for..
     * @return - description
     */
    @NotNull
    String getDescription();

    /**
     * Executes command.
     * @param text - full length string which is entered to the command line.
     * @param dataContext - data context
     * @param args - arguments obtained via CompletionProviders
     */
    void execute(String text, DataContext dataContext, @NotNull Object[] args);

    /**
     * Returns a completion provider depending on what command text is entered
     * and arguments selected from CompletionProviders.
     * @param text - full length string which is entered to the command line.
     * @param args - arguments obtained via previous CompletionProviders
     * @return
     */
    CompletionProvider getCompletionProvider(String text, Object[] args);

}
