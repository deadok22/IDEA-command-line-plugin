package ru.spbau.cmd_line_plugin.commands;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;
import ru.spbau.cmd_line_plugin.api.Command;

import java.util.HashSet;
import java.util.Set;


/**
 * Author: Sergey A. Savenko
 */
public class CommandsLoader implements ApplicationComponent {

    private Set<Command> commands = new HashSet<Command>();

    public CommandsLoader() {
    }

    public Set<Command> getAllCommands() {
        return commands;
    }

    public void initComponent() {
        ReflectionCommandsLoader reflectionLoader = new ReflectionCommandsLoader();
        ExtensionsCommandsLoader extensionsLoader = new ExtensionsCommandsLoader();

        commands.addAll(reflectionLoader.loadCommands());
        commands.addAll(extensionsLoader.loadCommands());
    }

    public void disposeComponent() {}

    @NotNull
    public String getComponentName() {
        return "CommandsLoader";
    }

}
