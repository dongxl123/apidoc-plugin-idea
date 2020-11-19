package com.suiyiwen.plugin.idea.apidoc.utils;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.javadoc.PsiDocComment;

/**
 * @author dongxuanliang252
 * @date 2019-01-04 17:38
 */
public enum PsiDocCommentUtils {

    INSTANCE;

    public PsiDocComment createPsiDocComment(String commentText) {
        Project project = CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext());
        return JavaPsiFacade.getElementFactory(project).createDocCommentFromText(commentText);
    }

}
