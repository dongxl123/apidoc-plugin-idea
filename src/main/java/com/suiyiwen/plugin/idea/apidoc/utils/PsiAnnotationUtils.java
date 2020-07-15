package com.suiyiwen.plugin.idea.apidoc.utils;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.util.containers.ArrayListSet;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * @author dongxuanliang252
 * @date 2019-01-21 18:37
 */
public enum PsiAnnotationUtils {

    INSTANCE;

    public String getAttributeText(PsiAnnotation annotation, String attributeName) {
        PsiAnnotationMemberValue value = annotation.findAttributeValue(attributeName);
        if (value != null) {
            return StringUtils.strip(value.getText(), ApiDocConstant.CHAR_DOUBLE_QUOTA);
        }
        return null;
    }

    public boolean isAssignableFrom(String supperFQClassName, PsiAnnotation annotation) {
        Set<String> annotationCaches = new ArrayListSet<>();
        return isAssignableFrom(supperFQClassName, annotation, annotationCaches);
    }

    private boolean isAssignableFrom(String supperFQClassName, PsiAnnotation annotation, Set<String> annotationCaches) {
        if (annotation.getQualifiedName().equals(supperFQClassName)) {
            return true;
        }
        if (annotationCaches.contains(annotation.getQualifiedName())) {
            return false;
        } else {
            annotationCaches.add(annotation.getQualifiedName());
        }
        PsiType fqType = PsiTypesUtils.INSTANCE.createPsiType(annotation.getQualifiedName(), annotation);
        if (fqType instanceof PsiClassReferenceType) {
            PsiClass psiClass = ((PsiClassReferenceType) fqType).resolve();
            if (psiClass == null) {
                return false;
            }
            if (hasAnnotation(psiClass.getModifierList(), supperFQClassName)) {
                return true;
            }
            for (PsiAnnotation superAnnotation : psiClass.getModifierList().getAnnotations()) {
                if (isAssignableFrom(supperFQClassName, superAnnotation, annotationCaches)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasAnnotation(PsiModifierList psiModifierList, String fqClassName) {
        if (psiModifierList == null || StringUtils.isBlank(fqClassName)) {
            return false;
        }
        PsiAnnotation psiAnnotation = psiModifierList.findAnnotation(fqClassName);
        return psiAnnotation != null;
    }

}
