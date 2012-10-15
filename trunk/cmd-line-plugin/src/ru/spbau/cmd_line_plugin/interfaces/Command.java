package ru.spbau.cmd_line_plugin.interfaces;

import com.intellij.openapi.project.Project;

/**
 *
 * A command represented by set of tokens and context.
 *
 * Author: Sergey A. Savenko
 */
public class Command {

    private String[] tokens;
    private Project project;

    public Command(String[] tokens, Project project) {
        this.tokens = tokens;
        this.project = project;
    }

    public String[] getTokens() {
        return tokens;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

}
