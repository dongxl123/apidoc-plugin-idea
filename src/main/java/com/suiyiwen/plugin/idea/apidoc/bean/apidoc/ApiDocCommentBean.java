package com.suiyiwen.plugin.idea.apidoc.bean.apidoc;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-27 19:19
 */
@Getter
@Setter
public class ApiDocCommentBean implements Serializable {

    private Api api;
    private ApiVersion apiVersion;
    private ApiGroup apiGroup;
    private ApiName apiName;
    private ApiDescription apiDescription;
    private ApiParamTagGroup requestParamApiGroup;
    private ApiParamTagGroup requestBodyApiGroup;
    private ApiSuccessTagGroup apiSuccessGroup;

}
