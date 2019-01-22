package com.suiyiwen.plugin.idea.apidoc.parser;

import com.suiyiwen.plugin.idea.apidoc.enums.ApiDocTag;
import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.ApiDocElement;
import com.suiyiwen.plugin.idea.apidoc.utils.ClassUtils;

/**
 * @author dongxuanliang252
 * @date 2018-12-28 12:05
 */
public enum TagProcessorFactory {

    INSTANCE;

    public TagParser getTagParserByName(String name) {
        ApiDocTag tag = ApiDocTag.getTag(name);
        if (tag == null) {
            return null;
        }
        Class<? extends AbstractTagProcessor> cls = tag.getProcessorCls();
        AbstractTagProcessor tagParser = ClassUtils.INSTANCE.newInstance(cls);
        if (tagParser == null) {
            return null;
        }
        tagParser.setTag(tag);
        return tagParser;
    }

    public TagBuilder getTagBuilder(ApiDocElement element) {
        if (element == null) {
            return null;
        }
        ApiDocTag tag = ApiDocTag.getTagByElementCls(element.getClass());
        if (tag == null) {
            return null;
        }
        Class<? extends AbstractTagProcessor> cls = tag.getProcessorCls();
        AbstractTagProcessor tagBuilder = ClassUtils.INSTANCE.newInstance(cls);
        if (tagBuilder == null) {
            return null;
        }
        tagBuilder.setTag(tag);
        return tagBuilder;
    }

}
