package ru.spbau.deadok22.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.FoldRegion;
import com.intellij.openapi.ui.Messages;

/**
 * Author: Sergey A. Savenko
 */
public class ShowSomeStuffAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getData(DataKeys.EDITOR);
        if (null == editor) {
            Messages.showInfoMessage("editor is null", "ShowSomeStuffAction");
        } else {
            //collapses all regions in current editor
            editor.getFoldingModel().runBatchFoldingOperation(new Runnable() {
                @Override
                public void run() {
                    for (FoldRegion fr : editor.getFoldingModel().getAllFoldRegions()) {
                        fr.setExpanded(false);
                    }
                }
            });
        }
    }
}
