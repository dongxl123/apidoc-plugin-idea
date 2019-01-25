package com.suiyiwen.plugin.idea.apidoc.utils;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiFormatUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.suiyiwen.plugin.idea.apidoc.bean.dialog.FieldBean;
import com.suiyiwen.plugin.idea.apidoc.bean.dialog.ParamBean;
import com.suiyiwen.plugin.idea.apidoc.bean.dialog.ResultBean;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import com.suiyiwen.plugin.idea.apidoc.enums.AnnotationClass;
import com.suiyiwen.plugin.idea.apidoc.enums.HttpRequestMethod;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-01-02 13:47
 */
public enum NewDialogModelParseUtils {

    INSTANCE;

    public String parseRequestMethod(PsiMethod element) {
        HttpRequestMethod requestMethod = PsiHttpUtils.INSTANCE.getHttpRequestMethod(element);
        if (requestMethod == null) {
            HttpRequestMethod.POST.name();
        }
        return requestMethod.name();
    }

    public String parseRequestUrl(PsiMethod element) {
        return PsiHttpUtils.INSTANCE.getHttpRequestUrl(element);
    }

    public String parseRequestTitle(PsiMethod element) {
        if (element == null) {
            return null;
        }
        return PsiFormatUtil.formatMethod(element, PsiSubstitutor.EMPTY,
                PsiFormatUtil.SHOW_NAME, PsiFormatUtil.SHOW_NAME);
    }

    public String parseApiGroup(PsiMethod element) {
        if (element == null) {
            return null;
        }
        return PsiFormatUtil.formatClass(element.getContainingClass(), PsiFormatUtil.SHOW_NAME);
    }

    public String parseApiName(PsiMethod element) {
        if (element == null) {
            return null;
        }
        return PsiFormatUtil.formatMethod(element, PsiSubstitutor.EMPTY,
                PsiFormatUtil.SHOW_NAME, PsiFormatUtil.SHOW_NAME);
    }

    public ParamBean parseRequestParameter(PsiMethod element) {
        if (element == null) {
            return null;
        }
        List<PsiParameter> requestParameterTypeList = new ArrayList<>();
        for (PsiParameter psiParameter : element.getParameterList().getParameters()) {
            if (isRequestParameter(psiParameter)) {
                requestParameterTypeList.add(psiParameter);
            }
        }
        return parseParamBean(ApiDocConstant.TAG_REQUEST_PARAM_GROUP_TITLE, requestParameterTypeList);
    }

    private boolean isRequestParameter(PsiParameter psiParameter) {
        if (psiParameter == null) {
            return false;
        }
        if (psiParameter.hasAnnotation(AnnotationClass.REQUEST_PARAM.getClassName())) {
            return true;
        }
        if (ArrayUtils.isEmpty(psiParameter.getAnnotations())) {
            if (PsiTypesUtils.INSTANCE.isAssignableFrom(ApiDocConstant.HTTP_SERVLET_REQUEST_CLASS_NAME, psiParameter.getType())) {
                return false;
            }
            if (PsiTypesUtils.INSTANCE.isAssignableFrom(ApiDocConstant.HTTP_SERVLET_RESPONSE_CLASS_NAME, psiParameter.getType())) {
                return false;
            }
            if (PsiTypesUtils.INSTANCE.isAssignableFrom(ApiDocConstant.MULTIPART_FILE_CLASS_NAME, psiParameter.getType())) {
                return false;
            }
            return true;
        }
        return false;
    }

    public ParamBean parseRequestBody(PsiMethod element) {
        if (element == null) {
            return null;
        }
        List<PsiParameter> requestBodyTypeList = new ArrayList<>();

        for (PsiParameter psiParameter : element.getParameterList().getParameters()) {
            if (psiParameter.hasAnnotation(AnnotationClass.REQUEST_BODY.getClassName())) {
                requestBodyTypeList.add(psiParameter);
            }
        }
        return parseParamBean(ApiDocConstant.TAG_REQUEST_BODY_GROUP_TITLE, requestBodyTypeList);
    }

    public ResultBean parseResponseBody(PsiMethod element) {
        if (element == null) {
            return null;
        }
        ResultBean resultBean = parseResultBean(ApiDocConstant.TAG_RESPONSE_BODY_GROUP_TITLE, element.getReturnType());
        return resultBean;
    }

    private ResultBean parseResultBean(String title, PsiType psiType) {
        ResultBean exampleBean = new ResultBean();
        if (psiType == null) {
            return null;
        }
        List<FieldBean> innerChildFieldList = parseRefFieldBeanList(psiType);
        if (CollectionUtils.isEmpty(innerChildFieldList)) {
            innerChildFieldList = new ArrayList<>();
            FieldBean fieldBean = new FieldBean();
            fieldBean.setName(ApiDocConstant.STRING_RESULT);
            fieldBean.setType(PsiTypesUtils.INSTANCE.getFieldType(psiType).name());
            fieldBean.setPsiType(psiType);
            if (PsiTypesUtils.INSTANCE.isEnum(psiType)) {
                fieldBean.setDescription(PsiTypesUtils.INSTANCE.generateEnumDescription(psiType));
            }
            innerChildFieldList.add(fieldBean);
        }
        exampleBean.setTitle(title);
        exampleBean.setFieldList(innerChildFieldList);
        return exampleBean;
    }

    private ParamBean parseParamBean(String title, List<PsiParameter> elementList) {
        ParamBean exampleBean = new ParamBean();
        if (CollectionUtils.isEmpty(elementList)) {
            return null;
        }
        exampleBean.setTitle(title);
        List<FieldBean> allFieldBeanList = new ArrayList<>();
        for (PsiParameter psiParameter : elementList) {
            List<FieldBean> innerChildFieldList = parseRefFieldBeanList(psiParameter.getType());
            if (CollectionUtils.isNotEmpty(innerChildFieldList)) {
                allFieldBeanList.addAll(innerChildFieldList);
            } else {
                PsiType psiType = psiParameter.getType();
                FieldBean fieldBean = new FieldBean();
                fieldBean.setName(psiParameter.getName());
                fieldBean.setType(PsiTypesUtils.INSTANCE.getFieldType(psiType).name());
                fieldBean.setPsiType(psiType);
                if (PsiTypesUtils.INSTANCE.isEnum(psiType)) {
                    fieldBean.setDescription(PsiTypesUtils.INSTANCE.generateEnumDescription(psiType));
                }
                allFieldBeanList.add(fieldBean);
            }
        }
        exampleBean.setFieldList(allFieldBeanList);
        return exampleBean;
    }

    private List<FieldBean> parseRefFieldBeanList(PsiType psiType) {
        return parseRefFieldBeanList(psiType, ApiDocConstant.OBJECT_RESOLVE_DEPTH_START);
    }

    private List<FieldBean> parseRefFieldBeanList(PsiType psiType, int depth) {
        boolean isFirstDepth = ApiDocConstant.OBJECT_RESOLVE_DEPTH_START == depth;
        if (isFirstDepth) {
            depth++;
        }
        List<FieldBean> innerChildFieldList = new ArrayList<>();
        //boxedType, String, enum, map, primitiveType
        if (PsiTypesUtils.INSTANCE.isExtractEndPsiType(psiType)) {
            //不处理
        } else if (PsiTypesUtils.INSTANCE.isIterable(psiType)) {
            PsiType[] genericPsiTypes = ((PsiClassType) psiType).getParameters();
            if (ArrayUtils.isNotEmpty(genericPsiTypes)) {
                innerChildFieldList = parseRefFieldBeanList(genericPsiTypes[0], depth);
            }
        } else if (psiType instanceof PsiClassType) {
            PsiClass psiClass = PsiTypesUtil.getPsiClass(psiType);
            PsiSubstitutor psiSubstitutor = ((PsiClassType) psiType).resolveGenerics().getSubstitutor();
            for (PsiField psiField : psiClass.getAllFields()) {
                if (PsiFieldUtils.INSTANCE.isVariable(psiField)) {
                    innerChildFieldList.add(parseFieldBean(psiField, psiSubstitutor, depth));
                }
            }
        } else if (psiType instanceof PsiArrayType) {
            PsiArrayType arrayType = (PsiArrayType) psiType;
            PsiType componentType = arrayType.getComponentType();
            innerChildFieldList = parseRefFieldBeanList(componentType, depth);
        }
        if (CollectionUtils.isNotEmpty(innerChildFieldList)) {
            return innerChildFieldList;
        }
        return null;
    }

    private FieldBean parseFieldBean(PsiField psiField, PsiSubstitutor psiSubstitutor, int depth) {
        FieldBean fieldBean = new FieldBean();
        fieldBean.setName(psiField.getName());
        PsiType psiType = PsiTypesUtils.INSTANCE.createGenericPsiType(psiField.getType(), psiSubstitutor);
        fieldBean.setType(PsiTypesUtils.INSTANCE.getFieldType(psiType).name());
        fieldBean.setPsiType(psiType);
        fieldBean.setDescription(PsiFieldUtils.INSTANCE.getFieldDescription(psiField));
        if (StringUtils.isBlank(fieldBean.getDescription()) && PsiTypesUtils.INSTANCE.isEnum(psiType)) {
            fieldBean.setDescription(PsiTypesUtils.INSTANCE.generateEnumDescription(psiType));
        }
        if (depth >= ApiDocConstant.OBJECT_RESOLVE_MAX_DEPTH) {
            return fieldBean;
        }
        List<FieldBean> childFieldList = parseRefFieldBeanList(psiType, depth + 1);
        if (CollectionUtils.isNotEmpty(childFieldList)) {
            fieldBean.setChildFieldList(childFieldList);
        }
        return fieldBean;
    }

}
