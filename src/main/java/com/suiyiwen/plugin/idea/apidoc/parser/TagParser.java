package com.suiyiwen.plugin.idea.apidoc.parser;

import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.ApiDocElement;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 20:24
 */
public interface TagParser {

    ApiDocElement parse(String text);
}
