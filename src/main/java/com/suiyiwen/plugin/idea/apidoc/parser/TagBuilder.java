package com.suiyiwen.plugin.idea.apidoc.parser;

import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.ApiDocElement;

/**
 * @author dongxuanliang252
 * @date 2018-12-28 19:10
 */
public interface TagBuilder {

    String build(ApiDocElement element);
}
