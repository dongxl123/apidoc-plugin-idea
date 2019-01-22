package com.suiyiwen.plugin.idea.apidoc.parser;

import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.ApiDocElement;
import com.suiyiwen.plugin.idea.apidoc.bean.apidoc.ApiDocGroupElement;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-31 11:44
 */
public interface TagGroupParser {

    ApiDocGroupElement parse(List<ApiDocElement> elements);
}
