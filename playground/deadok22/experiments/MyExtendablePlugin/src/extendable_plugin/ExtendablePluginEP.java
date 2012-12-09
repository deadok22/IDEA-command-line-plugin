package extendable_plugin;

import com.intellij.openapi.extensions.ExtensionPointName;

/**
 * Author: Sergey A. Savenko
 */
public interface ExtendablePluginEP {

    ExtensionPointName<ExtendablePluginEP> EP_NAME = ExtensionPointName.create("MyExtendablePlugin.WhoAreYouEP");

    public String whoAreYou();

}
