package ru.spbau.cmd_line_plugin.api.autocomplete;

import org.jetbrains.annotations.NotNull;

/**
 * Author: Sergey A. Savenko
 */
public interface CompletionProvider {

    /**
     * Returns completions that have their text starting with passed string.
     * @param
     * @return
     */
    @NotNull
    Completion[] getCompletions(@NotNull String s);

}
