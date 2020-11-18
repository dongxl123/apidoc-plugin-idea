package com.suiyiwen.plugin.idea.apidoc.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author dongxuanliang252
 * @date 2019-01-21 15:36
 */
public enum HttpRequestMethod {

    POST("RequestMethod.POST"),
    GET("RequestMethod.GET"),
    DELETE("RequestMethod.DELETE"),
    PUT("RequestMethod.PUT"),
    PATCH("RequestMethod.PATCH"),
    ;
    private String requestMethodText;

    HttpRequestMethod(String requestMethodText) {
        this.requestMethodText = requestMethodText;
    }

    public String getRequestMethodText() {
        return requestMethodText;
    }

    public static HttpRequestMethod getHttpRequestMethod(String requestMethodText) {
        for (HttpRequestMethod requestMethod : HttpRequestMethod.values()) {
            if (StringUtils.equalsIgnoreCase(requestMethod.getRequestMethodText(), requestMethodText)) {
                return requestMethod;
            }
        }
        return null;
    }

}
