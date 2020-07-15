package com.suiyiwen.plugin.idea.apidoc.utils;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import com.suiyiwen.plugin.idea.apidoc.enums.FieldType;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

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

    public boolean isIterable(PsiType psiType, @NotNull PsiElement context) {
        return isAssignableFrom(CommonClassNames.JAVA_LANG_ITERABLE, psiType, context) || isAssignableFrom(CommonClassNames.JAVA_UTIL_ITERATOR, psiType, context);
    }

    public boolean isEnum(PsiType psiType, @NotNull PsiElement context) {
        return isAssignableFrom(CommonClassNames.JAVA_LANG_ENUM, psiType, context);
    }

    public boolean isMap(PsiType psiType, @NotNull PsiElement context) {
        return isAssignableFrom(CommonClassNames.JAVA_UTIL_MAP, psiType, context);
    }

    public boolean isAssignableFrom(String fQClassName, PsiType psiType, @NotNull PsiElement context) {
        PsiType fqType = createPsiType(fQClassName, context);
        return fqType.isAssignableFrom(psiType);
    }

    public PsiType createPsiType(String fQClassName, @NotNull PsiElement context) {
        return JavaPsiFacade.getElementFactory(context.getProject()).createTypeByFQClassName(fQClassName);
    }

    public boolean isExtractEndPsiType(PsiType psiType, @NotNull PsiElement context) {
        if (psiType instanceof PsiClassType) {
            if (isBoxedType(psiType) || isString(psiType) || isMap(psiType, context) || isEnum(psiType, context) || isNumber(psiType, context)
                    || isCharacter(psiType) || isCharSequence(psiType, context) || isBoolean(psiType) || isDate(psiType, context)) {
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

    public String generateEnumDescription(PsiType psiType, @NotNull PsiElement context) {
        if (isEnum(psiType, context)) {
            StringBuilder sb = new StringBuilder();
            PsiClass psiClass = ((PsiClassReferenceType) psiType).resolve();
            for (PsiField psiField : psiClass.getFields()) {
                if (psiField instanceof PsiEnumConstant) {
                    sb.append(psiField.getNameIdentifier().getText().trim());
                    PsiElement next = psiField.getNameIdentifier().getNextSibling();
                    if (next != null) {
                        sb.append(next.getText().trim());
                    }
                    sb.append(ApiDocConstant.CHAR_COMMA);
                }
            }
            return StringUtils.stripEnd(sb.toString(), ApiDocConstant.CHAR_COMMA);
        }
        return null;
    }

    public boolean isNumber(PsiType psiType, @NotNull PsiElement context) {
        return isAssignableFrom(CommonClassNames.JAVA_LANG_NUMBER, psiType, context);
    }

    public boolean isCharSequence(PsiType psiType, @NotNull PsiElement context) {
        return isAssignableFrom(JAVA_LANG_CHAR_SEQUENCE, psiType, context);
    }

    public boolean isCharacter(PsiType psiType) {
        String canonicalText = psiType.getCanonicalText();
        return CommonClassNames.JAVA_LANG_CHARACTER.equals(canonicalText);
    }

    public boolean isBoolean(PsiType psiType) {
        String canonicalText = psiType.getCanonicalText();
        return CommonClassNames.JAVA_LANG_BOOLEAN.equals(canonicalText);
    }

    public boolean isDate(PsiType psiType, @NotNull PsiElement context) {
        return isAssignableFrom(CommonClassNames.JAVA_UTIL_DATE, psiType, context);
    }

    public FieldType getFieldType(PsiType psiType, @NotNull PsiElement context) {
        if (psiType instanceof PsiPrimitiveType) {
            String boxedTypeName = ((PsiPrimitiveType) psiType).getBoxedTypeName();
            return getFieldType(createPsiType(boxedTypeName, context), context);
        } else if (psiType instanceof PsiArrayType || isIterable(psiType, context)) {
            return FieldType.Array;
        } else if (isBoolean(psiType)) {
            return FieldType.Boolean;
        } else if (isNumber(psiType, context) || isDate(psiType, context)) {
            return FieldType.Number;
        } else if (isCharSequence(psiType, context) || isCharacter(psiType) || isEnum(psiType, context)) {
            return FieldType.String;
        } else if (isMap(psiType, context)) {
            return FieldType.Object;
        }
        return FieldType.Object;
    }

}
