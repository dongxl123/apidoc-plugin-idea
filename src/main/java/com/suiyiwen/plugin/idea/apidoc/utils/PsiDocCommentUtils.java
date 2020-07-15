package com.suiyiwen.plugin.idea.apidoc.utils;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

/**
 * @author dongxuanliang252
 * @date 2019-01-04 17:38
 */
public enum PsiDocCommentUtils {

    INSTANCE;

    public PsiDocComment createPsiDocComment(String commentText, @NotNull PsiElement context) {
        return JavaPsiFacade.getElementFactory(context.getProject()).createDocCommentFromText(commentText);
    }

}
