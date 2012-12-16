package ru.spbau.cmd_line_plugin.commands.commands;

import com.intellij.ide.ui.search.OptionDescription;
import com.intellij.ide.util.gotoByName.GotoActionModel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import ru.spbau.cmd_line_plugin.api.Command;
import ru.spbau.cmd_line_plugin.api.CommandResultHandler;
import ru.spbau.cmd_line_plugin.api.autocomplete.Completion;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Author: Sergey A. Savenko
 */
public class ActionCommand extends Command {

    private static final String NAME = "action";
    private static final String DESCRIPTION = "Run any action registered in IDEA ('Make project', 'Run', etc.)";
    private static final String HELP = "action <action_name>";

    @NotNull
    @Override
    public String getName() {
        return NAME;
    }

    @NotNull
    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @NotNull
    @Override
    public String getHelpMessage() {
        return HELP;
    }

    @Override
    public void execute(String text, Component contextComponent, @NotNull Object[] args, @NotNull CommandResultHandler resultHandler) {
    }

    @Override
    public CompletionProvider getCompletionProvider(String text, Object[] args, Component contextComponent) {
        String argsString = getArgumentsString(text);
        //TODO write me

        if ((null == args || 0 == args.length) && (null == argsString || argsString.isEmpty())) {
            return null;
        }
        return null;
    }
}

class ActionCompletionProvider implements CompletionProvider {

    private final GotoActionModel gotoActionModel;

    public ActionCompletionProvider(Project p, Component contextComponent) {
        gotoActionModel = new GotoActionModel(p, contextComponent);
    }

    @NotNull
    @Override
    public Completion[] getCompletions(@NotNull String s) {
        Object[] matches = gotoActionModel.getElementsByName(s, false, s);
        ArrayList<Completion> completions = new ArrayList<Completion>();
        for (Object m : matches) {
            Completion c = createCompletion(m);
            if (null != c) {
                completions.add(c);
            }
        }
        return completions.toArray(new Completion[completions.size()]);
    }

    private Completion createCompletion(Object match) {
        if (match instanceof Map.Entry) {
            return new ActionCompletion((AnAction) ((Map.Entry)match).getKey());
        }
        return null;
    }
}

class ActionCompletion extends Completion {

    @NotNull
    @Override
    public String getText() {
        return ((AnAction) getObject()).getTemplatePresentation().getText();
    }

    public ActionCompletion(AnAction action) {
        super(action);
    }

}