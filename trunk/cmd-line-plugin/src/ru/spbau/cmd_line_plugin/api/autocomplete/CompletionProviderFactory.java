package ru.spbau.cmd_line_plugin.api.autocomplete;

import ru.spbau.cmd_line_plugin.api.autocomplete.default_providers.DefaultCompletionProvider;
import ru.spbau.cmd_line_plugin.api.autocomplete.default_providers.FontFaceCompletionProvider;

/**
 * Constructs frequently used completion providers.
 *
 * Author: Sergey A. Savenko
 */
public class CompletionProviderFactory {

    public enum Providers {
        FONT_FACE
    }

    public static CompletionProvider getStandardCompletionProvider(Providers prov) {
        switch (prov) {
            case FONT_FACE : {
                return new FontFaceCompletionProvider();
            }
            default : {
                return new DefaultCompletionProvider();
            }
        }
    }

    private CompletionProviderFactory() {}

}
