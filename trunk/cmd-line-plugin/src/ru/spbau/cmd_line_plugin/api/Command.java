package ru.spbau.cmd_line_plugin.api;

import com.intellij.openapi.actionSystem.DataContext;
import org.jetbrains.annotations.NotNull;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProvider;

/**
 * Represents a command line command.
 *
 * Author: Sergey A. Savenko
 */
public abstract class Command {

    /**
     * Get name which can be used to search this command.
     * @return - name
     */
    @NotNull
    public abstract String getName();

    /**
     * Command description. Should contain basic description of what the command is for.
     * @return - description
     */
    @NotNull
    public abstract String getDescription();

    /**
     * Command help message. Should contain syntax and possible args list.
     * @return - help message
     */
    @NotNull
    public abstract String getHelpMessage();

    /**
     * Executes command.
     * @param text - full length string which is entered to the command line.
     * @param dataContext - data context
     * @param args - arguments obtained via CompletionProviders
     * @param resultHandler - result handler
     */
    public abstract void execute(String text, DataContext dataContext, @NotNull Object[] args, CommandResultHandler resultHandler);

    /**
     * Returns a completion provider depending on what command text is entered
     * and arguments selected from CompletionProviders.
     * @param text - full length string which is entered to the command line.
     * @param args - arguments obtained via previous CompletionProviders
     * @return
     */
    public abstract CompletionProvider getCompletionProvider(String text, Object[] args);

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return null != o && o instanceof Command && equals((Command) o);
    }

    public boolean equals(Command c) {
        return null != c && getName().equals(c);
    }

    @Override
    public String toString() {
        return getName() + "; " + getDescription() + "; " + getHelpMessage();
    }

}
