package com.suiyiwen.plugin.idea.apidoc.utils;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.util.ClassUtil;
import com.suiyiwen.plugin.idea.apidoc.enums.FieldType;
import com.suiyiwen.plugin.idea.apidoc.enums.JavaDocElements;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-01-04 17:38
 */
public enum PsiTypesUtils {

    INSTANCE;

    private final String JAVA_LANG_CHAR_SEQUENCE = "java.lang.CharSequence";

    private static final List<String> boxedTypes = new ArrayList<>();

    static {
        boxedTypes.add(CommonClassNames.JAVA_LANG_BOOLEAN);
        boxedTypes.add(CommonClassNames.JAVA_LANG_BYTE);
        boxedTypes.add(CommonClassNames.JAVA_LANG_CHARACTER);
        boxedTypes.add(CommonClassNames.JAVA_LANG_SHORT);
        boxedTypes.add(CommonClassNames.JAVA_LANG_INTEGER);
        boxedTypes.add(CommonClassNames.JAVA_LANG_LONG);
        boxedTypes.add(CommonClassNames.JAVA_LANG_DOUBLE);
        boxedTypes.add(CommonClassNames.JAVA_LANG_FLOAT);
    }

    public boolean isBoxedType(PsiType psiType) {
        String canonicalText = psiType.getCanonicalText();
        return boxedTypes.contains(canonicalText);
    }

    public boolean isString(PsiType psiType) {
        String canonicalText = psiType.getCanonicalText();
        return CommonClassNames.JAVA_LANG_STRING.equals(canonicalText);
    }

    public boolean isIterable(PsiType psiType) {
        return isAssignableFrom(CommonClassNames.JAVA_LANG_ITERABLE, psiType) || isAssignableFrom(CommonClassNames.JAVA_UTIL_ITERATOR, psiType);
    }

    public boolean isEnum(PsiType psiType) {
        return isAssignableFrom(CommonClassNames.JAVA_LANG_ENUM, psiType);
    }

    public boolean isMap(PsiType psiType) {
        return isAssignableFrom(CommonClassNames.JAVA_UTIL_MAP, psiType);
    }

    public boolean isAssignableFrom(String fQClassName, PsiType psiType) {
        PsiType fqType = createPsiType(fQClassName);
        return fqType.isAssignableFrom(psiType);
    }

    public PsiType createPsiType(String fQClassName) {
        Project project = CommonDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext());
        return JavaPsiFacade.getElementFactory(project).createTypeByFQClassName(fQClassName);
    }

    public boolean isExtractEndPsiType(PsiType psiType) {
        if (psiType instanceof PsiClassType) {
            if (isBoxedType(psiType) || isString(psiType) || isMap(psiType) || isEnum(psiType) || isNumber(psiType)
                    || isCharacter(psiType) || isCharSequence(psiType) || isBoolean(psiType) || isDate(psiType)) {
                return true;
            }
        } else if (psiType instanceof PsiPrimitiveType) {
            return true;
        }
        return false;
    }

    public String getPresentableText(PsiType psiType) {
        String presentableText = psiType.getPresentableText();
        if (StringUtils.isNotBlank(presentableText)) {
            return StringUtils.remove(presentableText, StringUtils.SPACE);
        }
        return CommonClassNames.JAVA_LANG_OBJECT_SHORT;
    }

    public PsiType createGenericPsiType(PsiType psiType, PsiSubstitutor psiSubstitutor) {
        if (psiType instanceof PsiClassType && MapUtils.isNotEmpty(psiSubstitutor.getSubstitutionMap())) {
            return psiSubstitutor.substitute(psiType);
        }
        return psiType;
    }

    public String generateEnumDescription(PsiType psiType) {
        if (isEnum(psiType)) {
            StringBuilder sb = new StringBuilder();
            PsiClass psiClass = ((PsiClassReferenceType) psiType).resolve();
            for (PsiField psiField : psiClass.getFields()) {
                if (psiField instanceof PsiEnumConstant) {
                    sb.append(psiField.getNameIdentifier().getText().trim());
                    PsiElement next = psiField.getNameIdentifier().getNextSibling();
                    if (next != null) {
                        sb.append(next.getText().trim());
                    }
                    sb.append(JavaDocElements.NEW_LINE.getPresentation());
                }
            }
            return sb.toString();
        }
        return null;
    }

    public boolean isNumber(PsiType psiType) {
        return isAssignableFrom(CommonClassNames.JAVA_LANG_NUMBER, psiType);
    }

    public boolean isCharSequence(PsiType psiType) {
        return isAssignableFrom(JAVA_LANG_CHAR_SEQUENCE, psiType);
    }

    public boolean isCharacter(PsiType psiType) {
        String canonicalText = psiType.getCanonicalText();
        return CommonClassNames.JAVA_LANG_CHARACTER.equals(canonicalText);
    }

    public boolean isBoolean(PsiType psiType) {
        String canonicalText = psiType.getCanonicalText();
        return CommonClassNames.JAVA_LANG_BOOLEAN.equals(canonicalText);
    }

    public boolean isDate(PsiType psiType) {
        return isAssignableFrom(CommonClassNames.JAVA_UTIL_DATE, psiType);
    }

    public FieldType getFieldType(PsiType psiType) {
        if (psiType instanceof PsiPrimitiveType) {
            String boxedTypeName = ((PsiPrimitiveType) psiType).getBoxedTypeName();
            return getFieldType(PsiTypesUtils.INSTANCE.createPsiType(boxedTypeName));
        } else if (psiType instanceof PsiArrayType || isIterable(psiType)) {
            return FieldType.Array;
        } else if (isBoolean(psiType)) {
            return FieldType.Boolean;
        } else if (isNumber(psiType) || isDate(psiType)) {
            return FieldType.Number;
        } else if (isCharSequence(psiType) || isCharacter(psiType) || isEnum(psiType)) {
            return FieldType.String;
        } else if (isMap(psiType)) {
            return FieldType.Object;
        }
        return FieldType.Object;
    }

}
