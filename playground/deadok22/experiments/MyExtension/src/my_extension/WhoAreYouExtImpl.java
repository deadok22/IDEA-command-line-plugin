package my_extension;

import extendable_plugin.ExtendablePluginEP;

/**
 * Author: Sergey A. Savenko
 */
public class WhoAreYouExtImpl implements ExtendablePluginEP {
    @Override
    public String whoAreYou() {
        return "I'm a cool plugin extension";
    }
}
