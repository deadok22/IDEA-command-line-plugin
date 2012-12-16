package ru.spbau.cmd_line_plugin.api.autocomplete.default_providers;

import org.jetbrains.annotations.NotNull;
import ru.spbau.cmd_line_plugin.api.autocomplete.Completion;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProvider;

/**
 * Author: Sergey A. Savenko
 */
public class DefaultCompletionProvider implements CompletionProvider {
    @NotNull
    @Override
    public Completion[] getCompletions(@NotNull String s) {
        return new Completion[0];
    }
}