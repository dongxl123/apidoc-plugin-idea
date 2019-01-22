package com.suiyiwen.plugin.idea.apidoc.utils;

import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiAnnotationMemberValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiType;
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
        PsiType fqType = PsiTypesUtils.INSTANCE.createPsiType(annotation.getQualifiedName());
        if (fqType instanceof PsiClassReferenceType) {
            PsiClass psiClass = ((PsiClassReferenceType) fqType).resolve();
            if (psiClass == null) {
                return false;
            }
            if (psiClass.hasAnnotation(supperFQClassName)) {
                return true;
            }
            for (PsiAnnotation superAnnotation : psiClass.getAnnotations()) {
                if (isAssignableFrom(supperFQClassName, superAnnotation, annotationCaches)) {
                    return true;
                }
            }
        }
        return false;
    }

}
