package com.suiyiwen.plugin.idea.apidoc.utils;

import com.intellij.psi.*;
import com.intellij.psi.javadoc.PsiDocComment;
import com.suiyiwen.plugin.idea.apidoc.enums.JavaDocElements;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author dongxuanliang252
 * @date 2019-01-04 17:38
 */
public enum PsiFieldUtils {

    INSTANCE;

    public boolean isVariable(PsiField psiField) {
        PsiModifierList psiModifierList = psiField.getModifierList();
        if (psiField.isDeprecated() || psiModifierList.hasModifierProperty(PsiModifier.PUBLIC) || psiModifierList.hasModifierProperty(PsiModifier.STATIC) || psiModifierList.hasModifierProperty(PsiModifier.FINAL) || psiModifierList.hasModifierProperty(PsiModifier.TRANSIENT)) {
            return false;
        }
        return true;
    }

    public String getFieldDescription(PsiField psiField) {
        PsiDocComment psiDocComment = psiField.getDocComment();
        if (psiDocComment == null) {
            return null;
        }
        PsiElement[] descriptions = psiDocComment.getDescriptionElements();
        if (ArrayUtils.isEmpty(descriptions)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (PsiElement description : descriptions) {
            sb.append(description.getText());
        }
        return StringUtils.remove(sb.toString().trim(), JavaDocElements.NEW_LINE.getPresentation());
    }

}
