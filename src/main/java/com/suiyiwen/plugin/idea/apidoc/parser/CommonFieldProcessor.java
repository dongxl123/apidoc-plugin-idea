package com.suiyiwen.plugin.idea.apidoc.parser;

import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.AbstractApiField;
import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.ApiDocElement;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:05
 */
public class CommonFieldProcessor extends AbstractTagProcessor {

    @Override
    public ApiDocElement parse(List<String> textList) {
        AbstractApiField element = newElementInstance();
        if (element == null) {
            return null;
        }
        if (CollectionUtils.isNotEmpty(textList)) {
            boolean descFlag = false;
            for (String text : textList) {
                if (StringUtils.isBlank(text)) {
                    continue;
                }
                if (StringUtils.startsWith(text, ApiDocConstant.TAG_TEXT_OPEN_PAREN) && StringUtils.endsWith(text, ApiDocConstant.TAG_TEXT_CLOSE_PAREN)) {
                    element.setGroup(StringUtils.strip(text, String.format("%s%s", ApiDocConstant.TAG_TEXT_OPEN_PAREN, ApiDocConstant.TAG_TEXT_CLOSE_PAREN)));
                } else if (StringUtils.startsWith(text, ApiDocConstant.TAG_TEXT_OPEN_BRACE) && StringUtils.endsWith(text, ApiDocConstant.TAG_TEXT_CLOSE_BRACE)) {
                    String typeName = StringUtils.strip(text, String.format("%s%s", ApiDocConstant.TAG_TEXT_OPEN_BRACE, ApiDocConstant.TAG_TEXT_CLOSE_BRACE));
                    element.setType(typeName);
                } else if (!descFlag) {
                    element.setField(text);
                    descFlag = true;
                } else if (StringUtils.isBlank(element.getDescription())) {
                    element.setDescription(text);
                }
            }
        }
        return element;
    }

    @Override
    public String buildValue(ApiDocElement element) {
        if (element instanceof AbstractApiField) {
            AbstractApiField tElement = (AbstractApiField) element;
            StringBuilder sb = new StringBuilder();
            if (StringUtils.isNotBlank(tElement.getGroup())) {
                sb.append(ApiDocConstant.TAG_TEXT_OPEN_PAREN).append(tElement.getGroup()).append(ApiDocConstant.TAG_TEXT_CLOSE_PAREN).append(StringUtils.SPACE);
            }
            if (tElement.getType() != null) {
                sb.append(ApiDocConstant.TAG_TEXT_OPEN_BRACE).append(tElement.getType()).append(ApiDocConstant.TAG_TEXT_CLOSE_BRACE).append(StringUtils.SPACE);
            }
            sb.append(StringUtils.stripToEmpty(tElement.getField())).append(StringUtils.SPACE);
            sb.append(StringUtils.stripToEmpty(tElement.getDescription()));
            return sb.toString();
        }
        return StringUtils.EMPTY;
    }

}
