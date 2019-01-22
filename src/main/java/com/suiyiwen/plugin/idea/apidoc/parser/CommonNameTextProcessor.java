package com.suiyiwen.plugin.idea.apidoc.parser;

import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.AbstractApiNameText;
import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.ApiDocElement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
public class CommonNameTextProcessor extends AbstractTagProcessor {

    @Override
    public ApiDocElement parse(List<String> textList) {
        AbstractApiNameText element = newElementInstance();
        if (element == null) {
            return null;
        }
        if (CollectionUtils.isNotEmpty(textList)) {
            if (textList.size() > 1) {
                element.setName(textList.get(0));
                element.setText(textList.get(1));
            } else {
                element.setText(textList.get(0));
            }
        }
        return element;
    }


    @Override
    public String buildValue(ApiDocElement element) {
        if (element instanceof AbstractApiNameText) {
            AbstractApiNameText tElement = (AbstractApiNameText) element;
            StringBuilder sb = new StringBuilder();
            if (StringUtils.isNotBlank(tElement.getName())) {
                sb.append(tElement.getName()).append(StringUtils.SPACE);
            }
            sb.append(tElement.getText());
            return StringUtils.stripToEmpty(sb.toString());
        }
        return StringUtils.EMPTY;
    }
}
