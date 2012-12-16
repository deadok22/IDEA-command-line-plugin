package ru.spbau.cmd_line_plugin.api.autocomplete;

import ru.spbau.cmd_line_plugin.api.autocomplete.default_providers.BooleanCompletionProvider;
import ru.spbau.cmd_line_plugin.api.autocomplete.default_providers.DefaultCompletionProvider;
import ru.spbau.cmd_line_plugin.api.autocomplete.default_providers.FontFaceCompletionProvider;

/**
 * Constructs frequently used completion providers.
 *
 * Author: Sergey A. Savenko
 */
public class CompletionProviderFactory {

    public enum Providers {
        FONT_FACE,
        BOOLEAN
    }

    public static CompletionProvider getStandardCompletionProvider(Providers prov) {
        //TODO consider storing pre-constructed instances and return 'em
        switch (prov) {
            case FONT_FACE : {
                return new FontFaceCompletionProvider();
            }
            case BOOLEAN : {
                return new BooleanCompletionProvider();
            }
            default : {
                return new DefaultCompletionProvider();
            }
        }
    }

    private CompletionProviderFactory() {}

}
