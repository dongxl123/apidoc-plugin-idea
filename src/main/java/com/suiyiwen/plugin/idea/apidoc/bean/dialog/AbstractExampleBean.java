package com.suiyiwen.plugin.idea.apidoc.bean.dialog;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 15:20
 */
@Getter
@Setter
public abstract class AbstractExampleBean implements Serializable {
    private String title;
    private List<FieldBean> fieldList;
    private String example;

}
