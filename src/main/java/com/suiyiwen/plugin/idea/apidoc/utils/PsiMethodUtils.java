package com.suiyiwen.plugin.idea.apidoc.utils;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiModifierList;

/**
 * @author dongxuanliang252
 * @date 2019-01-04 17:38
 */
public enum PsiMethodUtils {

    INSTANCE;

    public boolean isPublicMethod(PsiMethod element) {
        PsiModifierList psiModifierList = element.getModifierList();
        if (psiModifierList.hasModifierProperty(PsiModifier.PUBLIC)) {
            return true;
        }
        return false;
    }
}
