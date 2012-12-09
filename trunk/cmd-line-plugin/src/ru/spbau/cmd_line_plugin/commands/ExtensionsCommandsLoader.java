package ru.spbau.cmd_line_plugin.commands;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.Extensions;
import ru.spbau.cmd_line_plugin.api.Command;
import ru.spbau.cmd_line_plugin.api.CommandLineCommandsProvider;

import java.util.HashSet;
import java.util.Set;

/**
 * Used to load Commands from other plugins via extensions mechanism.
 *
 * Author: Sergey A. Savenko
 */
public class ExtensionsCommandsLoader {

    private static final ExtensionPointName<CommandLineCommandsProvider> EP_NAME
            = ExtensionPointName.create("ru.spbau.cmd_line_plugin.CommandsProvider");

    public ExtensionsCommandsLoader() {}

    public Set<Command> loadCommands() {
        HashSet<Command> commands = new HashSet<Command>();
        CommandLineCommandsProvider[] providers = Extensions.getExtensions(EP_NAME);
        for (CommandLineCommandsProvider provider : providers) {
            commands.addAll(provider.getAllCommands());
        }
        return commands;
    }

}