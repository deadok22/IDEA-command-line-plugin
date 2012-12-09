package extendable_plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.ui.popup.JBPopupFactory;

/**
 * Author: Sergey A. Savenko
 */
public class PrintExtensionsAction extends AnAction {

    static ExtensionPointName<ExtendablePluginEP> EP_NAME = ExtensionPointName.create("MyExtendablePlugin.WhoAreYouEP");

    public void actionPerformed(AnActionEvent e) {
        final ExtendablePluginEP[] exts = (ExtendablePluginEP[])Extensions.getExtensions(EP_NAME);
        String message = "extensions:\n";
        for (ExtendablePluginEP ext : exts) {
            message += ext.whoAreYou();
            message += "\n";
        }
        JBPopupFactory.getInstance().createMessage(message).showCenteredInCurrentWindow(e.getProject());
    }
}
