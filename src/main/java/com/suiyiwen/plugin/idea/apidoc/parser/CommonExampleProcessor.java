package com.suiyiwen.plugin.idea.apidoc.parser;

import com.suiyiwen.plugin.idea.apidoc.enums.JavaDocElements;
import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.AbstractApiExample;
import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.ApiDocElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
public class CommonExampleProcessor extends AbstractTagProcessor {

    @Override
    public ApiDocElement parse(List<String> textList) {
        AbstractApiExample element = newElementInstance();
        if (element == null) {
            return null;
        }
        if (CollectionUtils.isNotEmpty(textList)) {
            element.setTitle(textList.get(0));
            element.setExample(textList.get(1));
        }
        return element;
    }

    @Override
    public String buildValue(ApiDocElement element) {
        if (element instanceof AbstractApiExample) {
            AbstractApiExample tElement = (AbstractApiExample) element;
            StringBuilder sb = new StringBuilder();
            sb.append(StringUtils.trimToEmpty(tElement.getTitle()));
            sb.append(JavaDocElements.NEW_LINE.getPresentation()).append(StringUtils.trimToEmpty(tElement.getExample()));
            return sb.toString();
        }
        return StringUtils.EMPTY;
    }
}
