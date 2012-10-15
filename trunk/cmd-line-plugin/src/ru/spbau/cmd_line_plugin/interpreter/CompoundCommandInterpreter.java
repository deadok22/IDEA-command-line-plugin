package ru.spbau.cmd_line_plugin.interpreter;

import ru.spbau.cmd_line_plugin.interfaces.Command;
import ru.spbau.cmd_line_plugin.interfaces.CommandInterpreter;

/**
 *
 * Compound command interpreter uses other command interpreters to interprete commands.
 *
 * Author: Sergey A. Savenko
 */
public class CompoundCommandInterpreter implements CommandInterpreter {

    private final CommandInterpreter[] interpreters;

    public CompoundCommandInterpreter(CommandInterpreter[] interpreters) {
        if (null == interpreters) {
            this.interpreters = new CommandInterpreter[0];
            return;
        }
        this.interpreters = new CommandInterpreter[interpreters.length];
        System.arraycopy(interpreters, 0, this.interpreters, 0, interpreters.length);
    }

    private CommandInterpreter findInterpreter(Command command) {
        for (CommandInterpreter ci : interpreters) {
            if (ci.canInterpret(command)) {
                return ci;
            }
        }
        return null;
    }

    @Override
    public boolean canInterpret(Command command) {
        return null != findInterpreter(command);
    }

    /**
     * Interprets passed command with first command interpreter whose canInterpret() returns true.
     * @param command
     * @return
     */
    @Override
    public boolean interpretCommand(Command command) {
        CommandInterpreter interpreter = findInterpreter(command);
        return null != interpreter && interpreter.interpretCommand(command);
    }
}
