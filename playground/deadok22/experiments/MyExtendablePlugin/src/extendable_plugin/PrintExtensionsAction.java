package extendable_plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.ui.popup.JBPopupFactory;

/**
 * Author: Sergey A. Savenko
 */
public class PrintExtensionsAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        final ExtendablePluginEP[] exts = (ExtendablePluginEP[])Extensions.getExtensions(ExtendablePluginEP.EP_NAME);
        String message = "extensions:\n";
        for (ExtendablePluginEP ext : exts) {
            message += ext.whoAreYou();
            message += "\n";
        }
        JBPopupFactory.getInstance().createMessage(message).showCenteredInCurrentWindow(e.getProject());
    }
}
