package com.suiyiwen.plugin.idea.apidoc.constant;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 12:01
 */
public interface ApiDocConstant {

    String DEFAULT_VERSION = "1.0.0";
    String TITLE_GENERATE_DIALOG = "ApiDoc";
    String NOTIFICATION_GROUP_DISPLAY_ID = "ApiDoc";
    String PLUGIN_NAME = "ApiDoc plugin ";
    String NOTIFICATION_TITLE = "ApiDoc Notification";
    String NOTIFICATION_FOCUS_CONTENT = "please focus over java method";
    String NOTIFICATION_NOT_CONTROLLER_CONTENT = "this is not a controller";
    String NOTIFICATION_NOT_REQUEST_METHOD_CONTENT = "this is not a request method";
    String TAG_PREFIX = "api";
    String TAG_TEXT_OPEN_PAREN = "(";
    String TAG_TEXT_CLOSE_PAREN = ")";
    String TAG_TEXT_OPEN_BRACE = "{";
    String TAG_TEXT_CLOSE_BRACE = "}";
    String CHAR_DOT = ".";
    String CHAR_COMMA = ",";
    String CHAR_DOUBLE_QUOTA = "\"";
    String CHAR_SEPERATOR = "/";
    String CHAR_AND = "&";
    String CHAR_EQUAL = "=";
    String TAG_TEXT_DEFAULT_TITLE_RESULT_GROUP = "responseBody";
    int OBJECT_EXTRACT_MAX_DEPTH = 3;
    int OBJECT_EXTRACT_DEPTH_START = 0;
    String TAG_REQUEST_PARAM_GROUP_TITLE = "请求参数";
    String TAG_REQUEST_BODY_GROUP_TITLE = "请求体";
    String TAG_RESPONSE_BODY_GROUP_TITLE = "响应结果";
    String TAG_GROUP_EXAMPLE_TITLE_SUFFIX = "示例";
    String STRING_REQUEST_BODY = "requestBody";
    String STRING_RESPONSE = "response";
    String MULTIPART_FILE_CLASS_NAME = "org.springframework.web.multipart.MultipartFile";
    String HTTP_SERVLET_REQUEST_CLASS_NAME = "javax.servlet.http.HttpServletRequest";
    String HTTP_SERVLET_RESPONSE_CLASS_NAME = "javax.servlet.http.HttpServletResponse";
    int UI_LINE_MIN_SIZE = 10;
    int UI_LINE_PREFER_SIZE = 20;
    int UI_LINE_MAX_SIZE = 25;
    int UI_TITLE_SIZE = 30;
    int UI_MIN_SIZE = 50;
    int UI_MAX_SIZE = 300;
    int DATA_CONTEXT_BLOCKING_TIMEOUT = 5 * 1000;
}
