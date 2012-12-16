package ru.spbau.cmd_line_plugin.api.autocomplete.default_providers;

import org.jetbrains.annotations.NotNull;
import ru.spbau.cmd_line_plugin.api.autocomplete.Completion;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProvider;

/**
 * Author: Sergey A. Savenko
 */
public class BooleanCompletionProvider implements CompletionProvider {
    @NotNull
    @Override
    public Completion[] getCompletions(@NotNull String s) {
        if (null == s || s.isEmpty()) {
            return new Completion[] {BooleanCompletion.TRUE_COMPLETION, BooleanCompletion.FALSE_COMPLETION};
        }
        String lowerCased = s.toLowerCase();
        if (Boolean.TRUE.toString().startsWith(lowerCased)) {
            return new Completion[] {BooleanCompletion.TRUE_COMPLETION};
        } else if (Boolean.FALSE.toString().startsWith(lowerCased)) {
            return new Completion[] {BooleanCompletion.FALSE_COMPLETION};
        }
        return new BooleanCompletion[0];
    }
}

class BooleanCompletion extends Completion {

    public static BooleanCompletion TRUE_COMPLETION = new BooleanCompletion(Boolean.TRUE);
    public static BooleanCompletion FALSE_COMPLETION = new BooleanCompletion(Boolean.FALSE);

    public BooleanCompletion(Boolean value) {
        super(value);
    }

    @NotNull
    @Override
    public String getText() {
        return getObject().toString();
    }
}
