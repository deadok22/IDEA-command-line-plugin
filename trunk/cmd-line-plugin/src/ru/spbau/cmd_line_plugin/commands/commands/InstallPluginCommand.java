package ru.spbau.cmd_line_plugin.commands.commands;

import com.intellij.ide.plugins.*;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.updateSettings.impl.PluginDownloader;
import com.intellij.openapi.updateSettings.impl.UpdateChecker;
import com.intellij.openapi.updateSettings.impl.UpdateSettings;
import com.intellij.openapi.util.JDOMExternalizableStringList;
import org.jetbrains.annotations.NotNull;
import ru.spbau.cmd_line_plugin.api.Command;
import ru.spbau.cmd_line_plugin.api.CommandResultHandler;
import ru.spbau.cmd_line_plugin.api.autocomplete.Completion;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProvider;
import ru.spbau.cmd_line_plugin.api.autocomplete.CompletionProviderFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Author: Sergey A. Savenko
 */
public class InstallPluginCommand extends Command {

    private static final String NAME = "InstallPlugin";
    private static final String DESCRIPTION = "Installs plugin with specified name";
    private static final String HELP = "InstallPlugin <plugin_name>";

    private CompletionProvider completionProvider = null;

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
        if (1 == args.length) {
            installPlugin((IdeaPluginDescriptor) args[0], resultHandler);
            return;
        }
        String argsText = getArgumentsString(text);
        if (null == argsText || argsText.isEmpty()) {
            resultHandler.handleResult(this, false, "no plugin specified");
            return;
        }
        CompletionProvider cp = getCompletionProvider(null, null, contextComponent);
        Completion[] comps = completionProvider.getCompletions(argsText);
        if (1 != comps.length) {
            resultHandler.handleResult(this, false, "unknown plugin specified");
        } else {
            installPlugin((IdeaPluginDescriptor) comps[0].getObject(), resultHandler);
            return;
        }
    }

    @Override
    public CompletionProvider getCompletionProvider(String text, Object[] args, Component contextComponent) {
        String argsText = getArgumentsString(text);
        if ((null == args || 0 == args.length) && (null == argsText || argsText.isEmpty())) {
            if (null == completionProvider) {
                try {
                    completionProvider = new PluginNameCompletionProvider();
                } catch (Exception e) {
                    // unable to download plugins list
                    return CompletionProviderFactory.getStandardCompletionProvider(null);
                }
            }
            return completionProvider;
        }
        return CompletionProviderFactory.getStandardCompletionProvider(null);
    }

    private void installPlugin(IdeaPluginDescriptor pd, CommandResultHandler resultHandler) {
        PluginNode pn = createPluginNode(pd);
        if (null == pn) { //TODO remove || null == pn.getRepositoryName()
            resultHandler.handleResult(this, false, "plugin not found");
            return;
        }
        installPlugin(pn, resultHandler);
    }

    private void installPlugin(final PluginNode pluginNode, final CommandResultHandler resultHandler) {
        final ArrayList<PluginNode> plugins = new ArrayList<PluginNode>();
        plugins.add(pluginNode);
        ProgressManager.getInstance().run(new Task.Backgroundable(null, "InstallPluginCommand", true) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                if (!PluginInstaller.prepareToInstall(plugins, Arrays.asList(PluginManager.getPlugins()))) {
                    resultHandler.handleResult(InstallPluginCommand.this, false,
                            pluginNode.getPluginId().getIdString() +" was not successfully installed. Watch notifications for details");
                } else {
                    resultHandler.handleResult(InstallPluginCommand.this, true, null);
                }
            }
        });
    }

    private static PluginNode createPluginNode(IdeaPluginDescriptor descr) {
        PluginNode pluginNode = null;
        if (descr instanceof PluginNode) {
            pluginNode = (PluginNode)descr;
        }
        else if (descr instanceof IdeaPluginDescriptorImpl) {
            final PluginId pluginId = descr.getPluginId();
            pluginNode = new PluginNode(pluginId);
            pluginNode.setName(descr.getName());
            pluginNode.setDepends(Arrays.asList(descr.getDependentPluginIds()), descr.getOptionalDependentPluginIds());
            pluginNode.setSize("-1");
            pluginNode.setRepositoryName(getPluginHostUrl(descr.getPluginId().getIdString()));
        }
        return pluginNode;
    }

    private static String getPluginHostUrl(String pluginId) {
        final JDOMExternalizableStringList pluginHosts = UpdateSettings.getInstance().myPluginHosts;
        for (String host : pluginHosts) {
            try {
                final ArrayList<PluginDownloader> downloaded = new ArrayList<PluginDownloader>();
                UpdateChecker.checkPluginsHost(host, downloaded, false, null);
                for (PluginDownloader downloader : downloaded) {
                    if (pluginId.equals(downloader.getPluginId())) {
                        return host;
                    }
                }
            }
            catch (Exception ignored) {
            }
        }
        // no downloader found for this plugin id
        return null;
    }

}

class PluginNameCompletionProvider implements CompletionProvider {

    private Completion[] allCompletions;

    public PluginNameCompletionProvider() throws Exception {
        ArrayList<IdeaPluginDescriptor> descriptors =  RepositoryHelper.process(null);
        allCompletions = new Completion[descriptors.size()];
        for (int i = 0; i != allCompletions.length; ++i) {
            allCompletions[i] = new IdeaPluginDescriptorCompletion(descriptors.get(i));
        }
    }

    @NotNull
    @Override
    public Completion[] getCompletions(@NotNull String s) {
        ArrayList<Completion> completions = new ArrayList<Completion>();
        String filter = s.trim().toUpperCase();
        for (Completion possibleCompletion : allCompletions) {
            if (possibleCompletion.getText().toUpperCase().startsWith(filter)) {
                completions.add(possibleCompletion);
            }
        }
        return completions.toArray(new Completion[completions.size()]);
    }
}

class IdeaPluginDescriptorCompletion extends Completion {

    public IdeaPluginDescriptorCompletion(IdeaPluginDescriptor desc) {
        super(desc);
    }

    @NotNull
    @Override
    public String getText() {
        return ((IdeaPluginDescriptor)getObject()).getPluginId().getIdString();
    }
}