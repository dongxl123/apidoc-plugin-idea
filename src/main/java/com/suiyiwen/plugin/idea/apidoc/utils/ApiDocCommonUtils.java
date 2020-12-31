package com.suiyiwen.plugin.idea.apidoc.utils;

import com.google.common.base.CaseFormat;
import com.intellij.psi.PsiElement;
import com.suiyiwen.plugin.idea.apidoc.component.ApiDocSettings;
import org.jetbrains.annotations.NotNull;

/**
 * @author dongxuanliang252
 * @date 2020-12-31 11:31
 */
public enum ApiDocCommonUtils {

    INSTANCE;

    public String getSuitableFieldName(String name, @NotNull PsiElement context) {
        boolean usingSnakeCase = ApiDocSettings.getInstance(context.getProject()).isUsingSnakeCase();
        return usingSnakeCase ? CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name) : name;
    }

}
