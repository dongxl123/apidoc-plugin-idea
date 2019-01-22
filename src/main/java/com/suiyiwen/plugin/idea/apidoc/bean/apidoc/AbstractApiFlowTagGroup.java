package com.suiyiwen.plugin.idea.apidoc.bean.apidoc;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:24
 */
@Getter
@Setter
public abstract class AbstractApiFlowTagGroup implements ApiDocGroupElement {

    private List<AbstractApiField> fieldList;
    private AbstractApiExample example;

}
