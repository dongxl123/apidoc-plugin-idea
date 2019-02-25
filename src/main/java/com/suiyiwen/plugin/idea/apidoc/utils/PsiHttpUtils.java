package com.suiyiwen.plugin.idea.apidoc.utils;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import com.suiyiwen.plugin.idea.apidoc.enums.AnnotationClass;
import com.suiyiwen.plugin.idea.apidoc.enums.HttpRequestMethod;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-01-21 15:38
 */
public enum PsiHttpUtils {

    INSTANCE;

    public HttpRequestMethod getHttpRequestMethod(PsiMethod element) {
        if (element == null) {
            return null;
        }
        PsiAnnotation[] annotations = element.getModifierList().getAnnotations();
        //从attribute annotation分析
        if (ArrayUtils.isNotEmpty(annotations)) {
            for (PsiAnnotation annotation : annotations) {
                String qualifiedName = annotation.getQualifiedName();
                if (AnnotationClass.POST_MAPPING.getClassName().equals(qualifiedName)) {
                    return HttpRequestMethod.POST;
                } else if (AnnotationClass.GET_MAPPING.getClassName().equals(qualifiedName)) {
                    return HttpRequestMethod.GET;
                } else if (AnnotationClass.DELETE_MAPPING.getClassName().equals(qualifiedName)) {
                    return HttpRequestMethod.DELETE;
                } else if (AnnotationClass.PUT_MAPPING.getClassName().equals(qualifiedName)) {
                    return HttpRequestMethod.PUT;
                } else if (AnnotationClass.REQUEST_MAPPING.getClassName().equals(qualifiedName)) {
                    String methodText = PsiAnnotationUtils.INSTANCE.getAttributeText(annotation, "method");
                    if (StringUtils.isNotBlank(methodText)) {
                        HttpRequestMethod requestMethod = HttpRequestMethod.getHttpRequestMethod(methodText);
                        if (requestMethod != null) {
                            return requestMethod;
                        }
                    }
                    break;
                }
            }
        }
        PsiParameter[] parameters = element.getParameterList().getParameters();
        if (ArrayUtils.isNotEmpty(parameters)) {
            for (PsiParameter parameter : parameters) {
                PsiAnnotation[] parameterAnnotations = parameter.getModifierList().getAnnotations();
                if (ArrayUtils.isEmpty(parameterAnnotations)) {
                    continue;
                }
                for (PsiAnnotation annotation : parameterAnnotations) {
                    String qualifiedName = annotation.getQualifiedName();
                    if (AnnotationClass.REQUEST_BODY.getClassName().equals(qualifiedName)) {
                        return HttpRequestMethod.POST;
                    }
                }
            }
        }
        return HttpRequestMethod.GET;
    }

    public String getHttpRequestUrl(PsiMethod element) {
        if (element == null) {
            return null;
        }
        PsiClass psiClass = element.getContainingClass();
        if (!isController(psiClass)) {
            return null;
        }
        if (!isRequestMethod(element)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getPath(psiClass.getModifierList().getAnnotations())).append(ApiDocConstant.CHAR_SEPERATOR).append(getPath(element.getModifierList().getAnnotations()));
        return normalize(sb.toString());
    }

    private String getPath(PsiAnnotation[] annotations) {
        for (PsiAnnotation annotation : annotations) {
            if (PsiAnnotationUtils.INSTANCE.isAssignableFrom(AnnotationClass.REQUEST_MAPPING.getClassName(), annotation)) {
                String valueText = PsiAnnotationUtils.INSTANCE.getAttributeText(annotation, "value");
                if (StringUtils.isBlank(valueText)) {
                    valueText = PsiAnnotationUtils.INSTANCE.getAttributeText(annotation, "path");
                }
                if (StringUtils.isNotBlank(valueText)) {
                    return valueText;
                }
            }
        }
        return StringUtils.EMPTY;
    }

    public boolean isController(PsiClass psiClass) {
        PsiAnnotation[] annotations = psiClass.getModifierList().getAnnotations();
        if (ArrayUtils.isEmpty(annotations)) {
            return false;
        }
        for (PsiAnnotation annotation : annotations) {
            if (PsiAnnotationUtils.INSTANCE.isAssignableFrom(AnnotationClass.CONTROLLER.getClassName(), annotation)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRequestMethod(PsiMethod psiMethod) {
        PsiAnnotation[] annotations = psiMethod.getModifierList().getAnnotations();
        if (ArrayUtils.isEmpty(annotations)) {
            return false;
        }
        for (PsiAnnotation annotation : annotations) {
            if (PsiAnnotationUtils.INSTANCE.isAssignableFrom(AnnotationClass.REQUEST_MAPPING.getClassName(), annotation)) {
                return true;
            }
        }
        return false;
    }

    public String normalize(String url) {
        if (StringUtils.isBlank(url)) {
            return ApiDocConstant.CHAR_SEPERATOR;
        }
        String[] urlParts = StringUtils.split(url, ApiDocConstant.CHAR_SEPERATOR);
        if (ArrayUtils.isEmpty(urlParts)) {
            return ApiDocConstant.CHAR_SEPERATOR;
        }
        List<String> newParts = new ArrayList<>();
        for (String urlPart : urlParts) {
            if (StringUtils.isNotBlank(urlPart.trim())) {
                newParts.add(urlPart.trim());
            }
        }
        return ApiDocConstant.CHAR_SEPERATOR + StringUtils.join(newParts, ApiDocConstant.CHAR_SEPERATOR);
    }

}

