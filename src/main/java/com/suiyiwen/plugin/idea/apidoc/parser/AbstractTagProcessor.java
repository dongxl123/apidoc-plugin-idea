package com.suiyiwen.plugin.idea.apidoc.parser;

import com.suiyiwen.plugin.idea.apidoc.enums.ApiDocTag;
import com.suiyiwen.plugin.idea.apidoc.enums.JavaDocElements;
import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.ApiDocElement;
import com.suiyiwen.plugin.idea.apidoc.utils.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author dongxuanliang252
 * @date 2018-12-27 20:24
 */
public abstract class AbstractTagProcessor implements TagParser, TagBuilder {

    private static final Logger log = LoggerFactory.getLogger(AbstractTagProcessor.class);

    private ApiDocTag tag;

    public void setTag(ApiDocTag tag) {
        this.tag = tag;
    }

    @Override
    public ApiDocElement parse(String text) {
        if (tag == null || StringUtils.isEmpty(tag.getRegExp())) {
            return null;
        }
        try {
            Pattern pattern = Pattern.compile(tag.getRegExp());
            Matcher matcher = pattern.matcher(text);
            List<String> textList = new ArrayList<>();
            while (matcher.find()) {
                for (int i = 0; i < matcher.groupCount(); i++) {
                    textList.add(StringUtils.trim(matcher.group(i + 1)));
                }
            }
            return parse(textList);
        } catch (Exception e) {
            log.error("TagParser parseOldDialogModel error, text:{}, regExp:{}", text, tag.getRegExp(), e);
        }
        return null;
    }

    public abstract ApiDocElement parse(List<String> textList);

    @Override
    public String build(ApiDocElement element) {
        StringBuilder sb = new StringBuilder();
        sb.append(JavaDocElements.NEW_LINE.getPresentation()).append(JavaDocElements.LINE_START.getPresentation()).append(JavaDocElements.WHITE_SPACE.getPresentation()).append(JavaDocElements.TAG_START.getPresentation()).append(tag.name()).append(JavaDocElements.WHITE_SPACE.getPresentation()).append(buildValue(element));
        return sb.toString();
    }

    public abstract String buildValue(ApiDocElement element);


    protected <T extends ApiDocElement> T newElementInstance() {
        ApiDocElement element = ClassUtils.INSTANCE.newInstance(tag.getElementCls());
        if (element == null) {
            return null;
        }
        return (T) element;
    }

}
