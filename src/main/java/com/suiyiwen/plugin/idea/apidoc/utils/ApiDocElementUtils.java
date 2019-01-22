package com.suiyiwen.plugin.idea.apidoc.utils;

import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.suiyiwen.plugin.idea.apidoc.enums.ApiDocTag;
import com.suiyiwen.plugin.idea.apidoc.enums.JavaDocElements;
import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.*;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import com.suiyiwen.plugin.idea.apidoc.parser.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 12:05
 */
public enum ApiDocElementUtils {

    INSTANCE;

    public ApiDocCommentBean parse(PsiDocComment docComment) {
        Map<String, List<ApiDocElement>> tags = findApiDocTags(docComment);
        if (MapUtils.isEmpty(tags)) {
            return null;
        }
        //参数、返回结果特殊处理
        Map<String, List<ApiDocElement>> paramsTags = new LinkedHashMap<>();
        Map<String, List<ApiDocElement>> resultTags = new LinkedHashMap<>();
        ApiDocCommentBean commentBean = new ApiDocCommentBean();
        for (String tagName : tags.keySet()) {
            List<ApiDocElement> elements = tags.get(tagName);
            if (CollectionUtils.isEmpty(elements)) {
                continue;
            }
            ApiDocElement firstElement = elements.get(0);
            ApiDocTag docTag = ApiDocTag.getTagByElementCls(firstElement.getClass());
            if (docTag == null) {
                continue;
            }
            if (ApiDocTag.api.equals(docTag)) {
                commentBean.setApi((Api) firstElement);
            } else if (ApiDocTag.apiVersion.equals(docTag)) {
                commentBean.setApiVersion((ApiVersion) firstElement);
            } else if (ApiDocTag.apiDescription.equals(docTag)) {
                commentBean.setApiDescription((ApiDescription) firstElement);
            } else if (ApiDocTag.apiGroup.equals(docTag)) {
                commentBean.setApiGroup((ApiGroup) firstElement);
            } else if (ApiDocTag.apiName.equals(docTag)) {
                commentBean.setApiName((ApiName) firstElement);
            } else if (ApiDocTag.apiParam.equals(docTag) || ApiDocTag.apiParamExample.equals(docTag)) {
                putIntoMap(docTag, elements, paramsTags);
            } else if (ApiDocTag.apiSuccess.equals(docTag) || ApiDocTag.apiSuccessExample.equals(docTag)) {
                putIntoMap(docTag, elements, resultTags);
            }
        }
        TagGroupParser tagGroupParser = new ApiFlowTagGroupProcessor();
        if (MapUtils.isNotEmpty(paramsTags)) {
            for (String groupName : paramsTags.keySet()) {
                List<ApiDocElement> elements = paramsTags.get(groupName);
                ApiParamTagGroup paramGroup = (ApiParamTagGroup) tagGroupParser.parse(elements);
                if (paramGroup == null) {
                    continue;
                }
                if (ApiDocConstant.TAG_REQUEST_PARAM_GROUP_TITLE.equals(groupName)) {
                    commentBean.setRequestParamApiGroup(paramGroup);
                } else if (ApiDocConstant.TAG_REQUEST_BODY_GROUP_TITLE.equals(groupName)) {
                    commentBean.setRequestBodyApiGroup(paramGroup);
                }
            }
        }
        if (MapUtils.isNotEmpty(resultTags)) {
            List<ApiSuccessTagGroup> resultGroupList = new ArrayList<>();
            for (String groupName : resultTags.keySet()) {
                List<ApiDocElement> elements = resultTags.get(groupName);
                ApiSuccessTagGroup resultGroup = (ApiSuccessTagGroup) tagGroupParser.parse(elements);
                if (resultGroup != null) {
                    resultGroupList.add(resultGroup);
                }
            }
            commentBean.setApiSuccessGroup(resultGroupList.get(0));
        }
        return commentBean;
    }

    public String build(ApiDocCommentBean commentBean) {
        if (commentBean == null) {
            return StringUtils.EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(JavaDocElements.STARTING.getPresentation()).append(JavaDocElements.LINE_START.getPresentation());
        if (commentBean.getApi() != null) {
            sb.append(buildApiDocElementComment(commentBean.getApi()));
        }
        if (commentBean.getApiVersion() != null) {
            sb.append(buildApiDocElementComment(commentBean.getApiVersion()));
        }
        if (commentBean.getApiGroup() != null) {
            sb.append(buildApiDocElementComment(commentBean.getApiGroup()));
        }
        if (commentBean.getApiName() != null) {
            sb.append(buildApiDocElementComment(commentBean.getApiName()));
        }
        if (commentBean.getApiDescription() != null) {
            sb.append(buildApiDocElementComment(commentBean.getApiDescription()));
        }
        TagGroupBuilder tagGroupBuilder = new ApiFlowTagGroupProcessor();
        ApiParamTagGroup requestParamApiGroup = commentBean.getRequestParamApiGroup();
        if (requestParamApiGroup != null) {
            sb.append(tagGroupBuilder.build(requestParamApiGroup));
        }
        ApiParamTagGroup requestBodyApiGroup = commentBean.getRequestBodyApiGroup();
        if (requestBodyApiGroup != null) {
            sb.append(tagGroupBuilder.build(requestBodyApiGroup));
        }
        ApiSuccessTagGroup serviceResultGroup = commentBean.getApiSuccessGroup();
        if (serviceResultGroup != null) {
            sb.append(tagGroupBuilder.build(serviceResultGroup));
        }
        sb.append(JavaDocElements.NEW_LINE.getPresentation()).append(JavaDocElements.LINE_START.getPresentation()).append(JavaDocElements.ENDING.getPresentation());
        return sb.toString();
    }

    private void putIntoMap(ApiDocTag docTag, List<ApiDocElement> elements, Map<String, List<ApiDocElement>> map) {
        if (docTag == null || CollectionUtils.isEmpty(elements)) {
            return;
        }
        if (map == null) {
            map = new LinkedHashMap<>();
        }
        for (ApiDocElement element : elements) {
            String groupName = null;
            if (ApiDocTag.apiParam.equals(docTag) || ApiDocTag.apiSuccess.equals(docTag)) {
                AbstractApiField tElement = (AbstractApiField) element;
                groupName = tElement.getGroup();
            } else if (ApiDocTag.apiParamExample.equals(docTag) || ApiDocTag.apiSuccessExample.equals(docTag)) {
                AbstractApiExample tElement = (AbstractApiExample) element;
                groupName = tElement.getTitle();
            }
            if (StringUtils.isNotBlank(groupName)) {
                if (groupName.startsWith(ApiDocConstant.TAG_REQUEST_PARAM_GROUP_TITLE)) {
                    groupName = ApiDocConstant.TAG_REQUEST_PARAM_GROUP_TITLE;
                } else if (groupName.startsWith(ApiDocConstant.TAG_REQUEST_BODY_GROUP_TITLE)) {
                    groupName = ApiDocConstant.TAG_REQUEST_BODY_GROUP_TITLE;
                } else if (groupName.startsWith(ApiDocConstant.TAG_RESPONSE_BODY_GROUP_TITLE)) {
                    groupName = ApiDocConstant.TAG_RESPONSE_BODY_GROUP_TITLE;
                }
                if (!map.containsKey(groupName)) {
                    map.put(groupName, new ArrayList<>());
                }
                map.get(groupName).add(element);
            }
        }
    }

    private Map<String, List<ApiDocElement>> findApiDocTags(PsiDocComment docComment) {
        if (docComment == null) {
            return null;
        }
        //LinkedHashMap保证存储顺序
        Map<String, List<ApiDocElement>> tags = new LinkedHashMap<>();
        PsiDocTag[] docTags = docComment.getTags();
        for (PsiDocTag docTag : docTags) {
            String name = docTag.getName();
            if (!name.startsWith(ApiDocConstant.TAG_PREFIX)) {
                continue;
            }
            ApiDocTag apiDocTag = ApiDocTag.getTag(name);
            if (apiDocTag == null) {
                continue;
            }
            if (apiDocTag.isMultiple() || !tags.containsKey(name)) {
                TagParser parser = TagProcessorFactory.INSTANCE.getTagParserByName(name);
                if (parser != null) {
                    String tagText = docTag.getText();
                    tagText = StringUtils.removeStart(tagText, String.format("@%s", name));
                    tagText = StringUtils.remove(tagText, "*");
                    tagText = tagText.trim();
                    if (!tags.containsKey(name)) {
                        tags.put(name, new ArrayList<>());
                    }
                    tags.get(name).add(parser.parse(tagText));
                }
            }
        }
        return tags;
    }

    private String buildApiDocElementComment(ApiDocElement element) {
        TagBuilder tagBuilder = TagProcessorFactory.INSTANCE.getTagBuilder(element);
        if (tagBuilder == null) {
            return StringUtils.EMPTY;
        }
        return tagBuilder.build(element);
    }

}
