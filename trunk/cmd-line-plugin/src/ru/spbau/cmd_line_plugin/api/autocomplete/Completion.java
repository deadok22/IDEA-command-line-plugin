package ru.spbau.cmd_line_plugin.api.autocomplete;

import org.jetbrains.annotations.NotNull;

/**
 * Represents Completion - a thing you get using autocomplete that has a text representation and the object itself.
 *
 * Author: Sergey A. Savenko
 */
public abstract class Completion implements Comparable<Completion> {

    private Object object;

    public Completion(Object object) {
        this.object = object;
    }

    @NotNull
    public abstract String getText();

    public Object getObject() {
        return object;
    }

    @Override
    public int compareTo(Completion o) {
        return this.getText().compareTo(null == o ? "" : o.getText());
    }
}