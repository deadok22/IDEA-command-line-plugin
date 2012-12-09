package ru.spbau.cmd_line_plugin.api;

import org.jetbrains.annotations.Nullable;

/**
 * Implementors of this interface will receive command execution result.
 *
 * Author: Sergey A. Savenko
 */
public interface CommandResultHandler {

    void handleResult(Command cmd, boolean isOk, @Nullable String message);

}
