package com.suiyiwen.plugin.idea.apidoc.utils;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.javadoc.PsiDocComment;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;

/**
 * @author dongxuanliang252
 * @date 2019-01-04 17:38
 */
public enum PsiDocCommentUtils {

    INSTANCE;

    public PsiDocComment createPsiDocComment(String commentText) {
        DataContext dataContext = null;
        try {
            dataContext = DataManager.getInstance().getDataContextFromFocusAsync().blockingGet(ApiDocConstant.DATA_CONTEXT_BLOCKING_TIMEOUT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Project project = CommonDataKeys.PROJECT.getData(dataContext);
        return JavaPsiFacade.getElementFactory(project).createDocCommentFromText(commentText);
    }

}
