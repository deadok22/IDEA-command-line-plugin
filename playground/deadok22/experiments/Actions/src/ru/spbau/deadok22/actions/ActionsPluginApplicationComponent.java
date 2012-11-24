package ru.spbau.deadok22.actions;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Sergey A. Savenko
 */
public class ActionsPluginApplicationComponent implements ApplicationComponent {
    public ActionsPluginApplicationComponent() {
    }

    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName() {
        return "ActionsPluginApplicationComponent";
    }
}
