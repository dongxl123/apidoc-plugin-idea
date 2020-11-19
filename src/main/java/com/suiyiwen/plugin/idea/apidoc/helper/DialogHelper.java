package com.suiyiwen.plugin.idea.apidoc.helper;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.ApiDocCommentBean;
import com.suiyiwen.plugin.idea.apidoc.bean.dialog.*;
import com.suiyiwen.plugin.idea.apidoc.component.ApiDocSettings;
import com.suiyiwen.plugin.idea.apidoc.component.operation.JavaDocWriter;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import com.suiyiwen.plugin.idea.apidoc.enums.HttpRequestMethod;
import com.suiyiwen.plugin.idea.apidoc.ui.ApiDocGenerateDialog;
import com.suiyiwen.plugin.idea.apidoc.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author dongxuanliang252
 * @date 2018-12-18 12:05
 */
public enum DialogHelper {

    INSTANCE;

    private JavaDocWriter writer = ServiceManager.getService(JavaDocWriter.class);

    public void showGenerateDialog(DialogModel model, PsiElement psiElement) {
        ApiDocGenerateDialog dialog = new ApiDocGenerateDialog(false, model, psiElement);
        dialog.show();
    }

    public DialogModel parseOldDialogModel(PsiDocComment docComment) {
        ApiDocCommentBean commentBean = ApiDocElementUtils.INSTANCE.parse(docComment);
        return ConvertUtils.INSTANCE.convertCommentBean2DialogModel(commentBean);
    }

    public void writeJavaDoc(DialogModel model, PsiElement psiElement) {
        processDialogModel(model);
        PsiDocComment javaDoc = PsiDocCommentUtils.INSTANCE.createPsiDocComment(buildCommentText(model));
        writer.write(javaDoc, psiElement);
    }

    private void processDialogModel(DialogModel model) {
        if (model == null) {
            return;
        }
        ParamBean requestParameter = model.getRequestParameter();
        if (requestParameter != null) {
            filterDialogModelFieldBeanRecursively(requestParameter.getFieldList());
            if (StringUtils.isBlank(requestParameter.getExample()) || BooleanUtils.isTrue(model.getReGenerateExampleFlag())) {
                requestParameter.setExample(ExampleUtils.INSTANCE.generateRequestParameterExampleString(requestParameter.getFieldList()));
            }
        }
        ParamBean requestBody = model.getRequestBody();
        if (requestBody != null) {
            filterDialogModelFieldBeanRecursively(requestBody.getFieldList());
            if (StringUtils.isBlank(requestBody.getExample()) || BooleanUtils.isTrue(model.getReGenerateExampleFlag())) {
                requestBody.setExample(ExampleUtils.INSTANCE.generateRequestBodyExampleString(requestBody.getFieldList()));
            }
        }
        ResultBean responseBody = model.getResponseBody();
        if (responseBody != null) {
            filterDialogModelFieldBeanRecursively(responseBody.getFieldList());
            if (StringUtils.isBlank(responseBody.getExample()) || BooleanUtils.isTrue(model.getReGenerateExampleFlag())) {
                responseBody.setExample(ExampleUtils.INSTANCE.generateResponseBodyExampleString(responseBody.getFieldList()));
            }
        }
    }

    private void filterDialogModelFieldBeanRecursively(List<FieldBean> fieldBeanList) {
        if (CollectionUtils.isEmpty(fieldBeanList)) {
            return;
        }
        Iterator<FieldBean> itr = fieldBeanList.iterator();
        while (itr.hasNext()) {
            FieldBean fieldBean = itr.next();
            if (!fieldBean.isChecked()) {
                itr.remove();
            }
            filterDialogModelFieldBeanRecursively(fieldBean.getChildFieldList());
        }
    }

    private String buildCommentText(DialogModel model) {
        ApiDocCommentBean commentBean = ConvertUtils.INSTANCE.convertDialogModel2CommentBean(model);
        return ApiDocElementUtils.INSTANCE.build(commentBean);
    }

    public DialogModel createNewDialogModel(PsiMethod element) {
        if (element == null) {
            return null;
        }
        DialogModel dialogModel = new DialogModel();
        dialogModel.setGroupName(NewDialogModelParseUtils.INSTANCE.parseApiGroup(element));
        dialogModel.setName(NewDialogModelParseUtils.INSTANCE.parseApiName(element));
        ApiDocSettings settings = ApiDocSettings.getInstance(element.getProject());
        if (settings != null && StringUtils.isNotBlank(settings.getVersion())) {
            dialogModel.setVersion(settings.getVersion());
        } else {
            dialogModel.setVersion(ApiDocConstant.DEFAULT_VERSION);
        }
        dialogModel.setRequestMethod(NewDialogModelParseUtils.INSTANCE.parseRequestMethod(element));
        dialogModel.setRequestUrl(NewDialogModelParseUtils.INSTANCE.parseRequestUrl(element));
        dialogModel.setRequestTitle(NewDialogModelParseUtils.INSTANCE.parseRequestTitle(element));
        dialogModel.setRequestParameter(NewDialogModelParseUtils.INSTANCE.parseRequestParameter(element));
        dialogModel.setRequestBody(NewDialogModelParseUtils.INSTANCE.parseRequestBody(element));
        dialogModel.setResponseBody(NewDialogModelParseUtils.INSTANCE.parseResponseBody(element));
        return dialogModel;
    }

    public DialogModel mergeDialogModel(DialogModel newModel, DialogModel oldModel) {
        if (newModel == null && oldModel == null) {
            return null;
        }
        if (newModel == null) {
            return oldModel;
        }
        if (oldModel == null) {
            if(StringUtils.isBlank(newModel.getRequestMethod())){
                newModel.setRequestMethod(HttpRequestMethod.GET.name());
            }
            return newModel;
        }
        DialogModel mergeModel = newModel;
        if (StringUtils.isNotBlank(oldModel.getGroupName())) {
            mergeModel.setGroupName(oldModel.getGroupName());
        }
        if (StringUtils.isNotBlank(oldModel.getName())) {
            mergeModel.setName(oldModel.getName());
        }
        if (StringUtils.isNotBlank(oldModel.getDescription())) {
            mergeModel.setDescription(oldModel.getDescription());
        }
        if (StringUtils.isNotBlank(oldModel.getVersion())) {
            mergeModel.setVersion(oldModel.getVersion());
        }
        if (StringUtils.isNotBlank(oldModel.getRequestTitle())) {
            mergeModel.setRequestTitle(oldModel.getRequestTitle());
        }
        if (StringUtils.isBlank(mergeModel.getRequestUrl())) {
            mergeModel.setRequestUrl(oldModel.getRequestUrl());
        }
        if (StringUtils.isBlank(mergeModel.getRequestMethod())) {
            if (StringUtils.isNotBlank(oldModel.getRequestMethod())) {
                mergeModel.setRequestMethod(oldModel.getRequestMethod());
            } else {
                mergeModel.setRequestMethod(HttpRequestMethod.GET.name());
            }
        }
        mergeModel.setRequestParameter(mergeExample(newModel.getRequestParameter(), oldModel.getRequestParameter()));
        mergeModel.setRequestBody(mergeExample(newModel.getRequestBody(), oldModel.getRequestBody()));
        mergeModel.setResponseBody(mergeExample(newModel.getResponseBody(), oldModel.getResponseBody()));
        return mergeModel;
    }

    private <T extends AbstractExampleBean> List<T> mergeExampleList(List<T> newBeanList, List<T> oldBeanList) {
        if (CollectionUtils.isEmpty(newBeanList)) {
            return null;
        }
        if (CollectionUtils.isEmpty(oldBeanList)) {
            return newBeanList;
        }
        Map<String, AbstractExampleBean> oldBeanMap = oldBeanList.stream().collect(Collectors.toMap(o -> o.getTitle(), o -> o));
        for (AbstractExampleBean newBean : newBeanList) {
            String title = newBean.getTitle();
            if (oldBeanMap.containsKey(title)) {
                AbstractExampleBean oldBean = oldBeanMap.get(title);
                mergeExample(newBean, oldBean);
            }
        }
        return newBeanList;
    }

    private <T extends AbstractExampleBean> T mergeExample(T newBean, T oldBean) {
        if (newBean == null) {
            return null;
        }
        if (oldBean == null) {
            return newBean;
        }
        newBean.setFieldList(FieldBeanTreeUtils.INSTANCE.merge(newBean.getFieldList(), oldBean.getFieldList()));
        if (StringUtils.isBlank(newBean.getExample())) {
            newBean.setExample(oldBean.getExample());
        }
        return newBean;
    }

}
