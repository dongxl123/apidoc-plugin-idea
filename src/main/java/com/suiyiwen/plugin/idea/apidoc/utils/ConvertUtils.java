package com.suiyiwen.plugin.idea.apidoc.utils;

import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.*;
import com.suiyiwen.plugin.idea.apidoc.bean.dialog.AbstractExampleBean;
import com.suiyiwen.plugin.idea.apidoc.bean.dialog.DialogModel;
import com.suiyiwen.plugin.idea.apidoc.bean.dialog.ParamBean;
import com.suiyiwen.plugin.idea.apidoc.bean.dialog.ResultBean;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-01-02 11:47
 */
public enum ConvertUtils {

    INSTANCE;

    public DialogModel convertCommentBean2DialogModel(ApiDocCommentBean commentBean) {
        if (commentBean == null) {
            return null;
        }
        DialogModel dialogModel = new DialogModel();
        if (commentBean.getApi() != null) {
            dialogModel.setRequestMethod(commentBean.getApi().getType());
            dialogModel.setRequestUrl(commentBean.getApi().getField());
            dialogModel.setRequestTitle(commentBean.getApi().getDescription());
        }
        if (commentBean.getApiVersion() != null) {
            dialogModel.setVersion(commentBean.getApiVersion().getText());
        }
        if (commentBean.getApiGroup() != null) {
            dialogModel.setGroupName(commentBean.getApiGroup().getText());
        }
        if (commentBean.getApiName() != null) {
            dialogModel.setName(commentBean.getApiName().getText());
        }
        if (commentBean.getApiDescription() != null) {
            dialogModel.setDescription(commentBean.getApiDescription().getText());
        }
        dialogModel.setRequestParameter(convertTagGroupBean2ExampleBean(commentBean.getRequestParamApiGroup(), ParamBean.class));
        dialogModel.setRequestBody(convertTagGroupBean2ExampleBean(commentBean.getRequestBodyApiGroup(), ParamBean.class));
        dialogModel.setResponseBody(convertTagGroupBean2ExampleBean(commentBean.getApiSuccessGroup(), ResultBean.class));
        return dialogModel;
    }

    private <T extends AbstractExampleBean> T convertTagGroupBean2ExampleBean(AbstractApiFlowTagGroup tagGroupBean, Class<T> cls) {
        if (tagGroupBean == null) {
            return null;
        }
        T exampleBean = ClassUtils.INSTANCE.newInstance(cls);
        if (exampleBean == null) {
            return null;
        }
        if (tagGroupBean.getExample() != null) {
            exampleBean.setExample(tagGroupBean.getExample().getExample());
            exampleBean.setTitle(tagGroupBean.getExample().getTitle());
        }
        List<AbstractApiField> serviceFieldList = tagGroupBean.getFieldList();
        if (CollectionUtils.isNotEmpty(serviceFieldList)) {
            AbstractApiField firstServiceField = serviceFieldList.get(0);
            if (StringUtils.isBlank(exampleBean.getTitle())) {
                exampleBean.setTitle(firstServiceField.getGroup());
            }
            exampleBean.setFieldList(FieldBeanTreeUtils.INSTANCE.toTreeFieldBeanList(serviceFieldList));
        }
        return exampleBean;
    }


    public ApiDocCommentBean convertDialogModel2CommentBean(DialogModel dialogModel) {
        if (dialogModel == null) {
            return null;
        }
        ApiDocCommentBean commentBean = new ApiDocCommentBean();
        if (StringUtils.isNotBlank(dialogModel.getRequestUrl())) {
            Api api = new Api();
            api.setType(dialogModel.getRequestMethod());
            api.setField(dialogModel.getRequestUrl());
            api.setDescription(dialogModel.getRequestTitle());
            commentBean.setApi(api);
        }
        if (StringUtils.isNotBlank(dialogModel.getVersion())) {
            ApiVersion serviceVersion = new ApiVersion();
            serviceVersion.setText(dialogModel.getVersion());
            commentBean.setApiVersion(serviceVersion);
        }
        if (StringUtils.isNotBlank(dialogModel.getGroupName())) {
            ApiGroup serviceGroup = new ApiGroup();
            serviceGroup.setText(dialogModel.getGroupName());
            commentBean.setApiGroup(serviceGroup);
        }
        if (StringUtils.isNotBlank(dialogModel.getName())) {
            ApiName serviceName = new ApiName();
            serviceName.setText(dialogModel.getName());
            commentBean.setApiName(serviceName);
        }
        if (StringUtils.isNotBlank(dialogModel.getDescription())) {
            ApiDescription apiDescription = new ApiDescription();
            apiDescription.setText(dialogModel.getDescription());
            commentBean.setApiDescription(apiDescription);
        }
        commentBean.setRequestParamApiGroup(convertExampleBean2TagGroupBean(dialogModel.getRequestParameter(), ApiParamTagGroup.class, ApiParamField.class, ApiParamExample.class));
        commentBean.setRequestBodyApiGroup(convertExampleBean2TagGroupBean(dialogModel.getRequestBody(), ApiParamTagGroup.class, ApiParamField.class, ApiParamExample.class));
        commentBean.setApiSuccessGroup(convertExampleBean2TagGroupBean(dialogModel.getResponseBody(), ApiSuccessTagGroup.class, ApiSuccessField.class, ApiSuccessExample.class));
        return commentBean;
    }

    private <T extends AbstractApiFlowTagGroup, F extends AbstractApiField, K extends AbstractApiExample> T convertExampleBean2TagGroupBean(AbstractExampleBean exampleBean, Class<T> tagGroupCls, Class<F> serviceFieldCls, Class<K> exampleCls) {
        if (exampleBean == null) {
            return null;
        }
        T tagGroupBean = ClassUtils.INSTANCE.newInstance(tagGroupCls);
        if (tagGroupBean == null) {
            return null;
        }
        if (StringUtils.isNotBlank(exampleBean.getExample())) {
            K example = ClassUtils.INSTANCE.newInstance(exampleCls);
            example.setExample(exampleBean.getExample());
            example.setTitle(StringUtils.defaultIfBlank(exampleBean.getTitle(), ApiDocConstant.TAG_TEXT_DEFAULT_TITLE_RESULT_GROUP));
            tagGroupBean.setExample(example);
        }
        if (exampleBean.getFieldList() != null) {
            tagGroupBean.setFieldList(FieldBeanTreeUtils.INSTANCE.toServiceFieldTagList(StringUtils.defaultIfBlank(exampleBean.getTitle(), ApiDocConstant.TAG_TEXT_DEFAULT_TITLE_RESULT_GROUP), exampleBean.getFieldList(), serviceFieldCls));
        }
        return tagGroupBean;
    }

}
