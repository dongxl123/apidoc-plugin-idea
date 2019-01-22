package com.suiyiwen.plugin.idea.apidoc.parser;

import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.AbstractApiText;
import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.ApiDocElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
public class CommonTextProcessor extends AbstractTagProcessor {

    @Override
    public ApiDocElement parse(List<String> textList) {
        AbstractApiText element = newElementInstance();
        if (element == null) {
            return null;
        }
        if (CollectionUtils.isNotEmpty(textList)) {
            element.setText(textList.get(0));
        }
        return element;
    }


    @Override
    public String buildValue(ApiDocElement element) {
        if (element instanceof AbstractApiText) {
            AbstractApiText tElement = (AbstractApiText) element;
            return StringUtils.stripToEmpty(tElement.getText());
        }
        return StringUtils.EMPTY;
    }
}
