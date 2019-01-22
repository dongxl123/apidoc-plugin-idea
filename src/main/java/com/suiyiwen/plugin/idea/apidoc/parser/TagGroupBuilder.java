package com.suiyiwen.plugin.idea.apidoc.parser;

import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.ApiDocGroupElement;

/**
 * @author dongxuanliang252
 * @date 2018-12-31 11:44
 */
public interface TagGroupBuilder {

    String build(ApiDocGroupElement element);

}
