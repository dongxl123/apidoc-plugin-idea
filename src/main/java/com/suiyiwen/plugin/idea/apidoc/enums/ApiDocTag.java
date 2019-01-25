package com.suiyiwen.plugin.idea.apidoc.enums;

import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.*;
import com.suiyiwen.plugin.idea.apidoc.parser.*;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 20:17
 */
public enum ApiDocTag {

    api(false, CommonFieldProcessor.class, ApiDocTag.FIELD_REGEXP, Api.class),
    apiVersion(false, CommonTextProcessor.class, ApiDocTag.VERSION_REGEXP, ApiVersion.class),
    apiDescription(false, CommonTextProcessor.class, ApiDocTag.TEXT_REGEXP, ApiDescription.class),
    apiGroup(false, CommonTextProcessor.class, ApiDocTag.TEXT_REGEXP, ApiGroup.class),
    apiName(false, CommonTextProcessor.class, ApiDocTag.TEXT_REGEXP, ApiName.class),
    apiParam(true, CommonFieldProcessor.class, ApiDocTag.FIELD_REGEXP, ApiParamField.class),
    apiParamExample(true, CommonExampleProcessor.class, ApiDocTag.EXAMPLE_REGEXP, ApiParamExample.class),
    apiSuccess(true, CommonFieldProcessor.class, ApiDocTag.FIELD_REGEXP, ApiSuccessField.class),
    apiSuccessExample(true, CommonExampleProcessor.class, ApiDocTag.EXAMPLE_REGEXP, ApiSuccessExample.class),
    ;

    private static final String VERSION_REGEXP = "^(\\d+\\.\\d+\\.\\d+)$";
    private static final String TEXT_REGEXP = "^(.*)$";
    private static final String NAME_TEXT_REGEXP = "^([^\\s]*)\\s*(.+)$";
    private static final String FIELD_REGEXP = "^(\\([^\\(|^\\)]+\\))?\\s*(\\{[^\\{|^\\}]+\\})?\\s*([^\\s]+)\\s*(.*)$";
    private static final String EXAMPLE_REGEXP = "^([^\\s]+)\\s+(.+)$";

    private boolean multiple;
    private Class<? extends AbstractTagProcessor> processorCls;
    private String regExp;
    private Class<? extends ApiDocElement> elementCls;

    ApiDocTag(boolean multiple, Class<? extends AbstractTagProcessor> processorCls, String regExp, Class<? extends ApiDocElement> elementCls) {
        this.multiple = multiple;
        this.processorCls = processorCls;
        this.regExp = regExp;
        this.elementCls = elementCls;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public Class<? extends AbstractTagProcessor> getProcessorCls() {
        return processorCls;
    }

    public String getRegExp() {
        return regExp;
    }

    public Class<? extends ApiDocElement> getElementCls() {
        return elementCls;
    }

    public static ApiDocTag getTag(String name) {
        for (ApiDocTag tag : ApiDocTag.values()) {
            if (tag.name().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }

    public static ApiDocTag getTagByProcessorCls(Class<? extends AbstractTagProcessor> processorCls) {
        for (ApiDocTag tag : ApiDocTag.values()) {
            if (tag.getProcessorCls().equals(processorCls)) {
                return tag;
            }
        }
        return null;
    }

    public static ApiDocTag getTagByElementCls(Class<? extends ApiDocElement> elementCls) {
        for (ApiDocTag tag : ApiDocTag.values()) {
            if (tag.getElementCls().equals(elementCls)) {
                return tag;
            }
        }
        return null;
    }

}
