package ru.spbau.cmd_line_plugin.api.autocomplete.default_providers;

import org.jetbrains.annotations.NotNull;
import ru.spbau.cmd_line_plugin.api.autocomplete.Completion;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Author: Sergey A. Savenko
 */
public class FontFaceCompletionProvider implements CompletionProvider {

    @NotNull
    @Override
    public Completion[] getCompletions(@NotNull String s) {
        Font[] allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        return filterAndGetCompletions(allFonts, s);
    }

    private Completion[] filterAndGetCompletions(Font[] fonts, String filter) {
        ArrayList<Completion> completions = new ArrayList<Completion>();
        filter = filter.trim().toUpperCase();
        for (Font f : fonts) {
            if (f.getFontName().toUpperCase().startsWith(filter)) {
                completions.add(new FontFaceCompletion(f));
            }
        }
        Collections.sort(completions);
        return completions.toArray(new Completion[completions.size()]);
    }

    private class FontFaceCompletion extends Completion {

        public FontFaceCompletion(Font font) {
            super(font);
        }

        @NotNull
        @Override
        public String getText() {
            return ((Font)this.getObject()).getFontName();
        }
    }

}
