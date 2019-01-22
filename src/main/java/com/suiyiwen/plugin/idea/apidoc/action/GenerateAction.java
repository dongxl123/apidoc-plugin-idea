package com.suiyiwen.plugin.idea.apidoc.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.suiyiwen.plugin.idea.apidoc.manager.ApiDocActionManger;

/**
 * @author dongxuanliang252
 * @date 2018-12-17 11:09
 */
public class GenerateAction extends AnAction {

    /**
     * @param e
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        ApiDocActionManger actionManager = new ApiDocActionManger();
        actionManager.showDialog(e);
    }

}
