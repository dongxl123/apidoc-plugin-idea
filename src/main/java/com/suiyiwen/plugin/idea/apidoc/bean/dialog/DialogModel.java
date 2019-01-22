package com.suiyiwen.plugin.idea.apidoc.bean.dialog;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


/**
 * @author dongxuanliang252
 * @date 2018-12-18 13:40
 */
@Getter
@Setter
public class DialogModel implements Serializable {

    private String groupName;
    private String name;
    private String description;
    private String version;
    private String requestTitle;
    private String requestUrl;
    private String requestMethod;
    private ParamBean requestParameter;
    private ParamBean requestBody;
    private ResultBean responseBody;

}
