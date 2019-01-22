package com.suiyiwen.plugin.idea.apidoc.parser;

import com.suiyiwen.plugin.idea.apidoc.enums.ApiDocTag;
import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-31 11:48
 */
public class ApiFlowTagGroupProcessor implements TagGroupParser, TagGroupBuilder {


    @Override
    public ApiDocGroupElement parse(List<ApiDocElement> elements) {
        AbstractApiFlowTagGroup group = determineNewInstance(elements);
        if (group == null) {
            return null;
        }
        for (ApiDocElement element : elements) {
            if (element instanceof AbstractApiField) {
                if (group.getFieldList() == null) {
                    group.setFieldList(new ArrayList<>());
                }
                group.getFieldList().add((AbstractApiField) element);
            } else if (element instanceof AbstractApiExample) {
                group.setExample((AbstractApiExample) element);
            }
        }
        return group;
    }

    private AbstractApiFlowTagGroup determineNewInstance(List<ApiDocElement> elements) {
        if (CollectionUtils.isEmpty(elements)) {
            return null;
        }
        ApiDocElement element = elements.get(0);
        ApiDocTag docTag = ApiDocTag.getTagByElementCls(element.getClass());
        if (docTag == null) {
            return null;
        }
        if (ApiDocTag.apiParam.equals(docTag) || ApiDocTag.apiParamExample.equals(docTag)) {
            return new ApiParamTagGroup();
        } else if (ApiDocTag.apiSuccess.equals(docTag) || ApiDocTag.apiSuccessExample.equals(docTag)) {
            return new ApiSuccessTagGroup();
        }
        return null;
    }


    @Override
    public String build(ApiDocGroupElement element) {
        if (element instanceof AbstractApiFlowTagGroup) {
            AbstractApiFlowTagGroup tagGroup = (AbstractApiFlowTagGroup) element;
            StringBuilder sb = new StringBuilder();
            if (CollectionUtils.isNotEmpty(tagGroup.getFieldList())) {
                for (AbstractApiField serviceField : tagGroup.getFieldList()) {
                    TagBuilder tagBuilder = TagProcessorFactory.INSTANCE.getTagBuilder(serviceField);
                    if (tagBuilder != null) {
                        sb.append(tagBuilder.build(serviceField));
                    }
                }
            }
            if (tagGroup.getExample() != null) {
                TagBuilder tagBuilder = TagProcessorFactory.INSTANCE.getTagBuilder(tagGroup.getExample());
                if (tagBuilder != null) {
                    sb.append(tagBuilder.build(tagGroup.getExample()));
                }
            }
            return sb.toString();
        }
        return StringUtils.EMPTY;
    }


}
