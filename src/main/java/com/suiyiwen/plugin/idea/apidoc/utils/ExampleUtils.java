package com.suiyiwen.plugin.idea.apidoc.utils;

import com.alibaba.fastjson.JSONObject;
import com.github.jsonzou.jmockdata.JMockData;
import com.suiyiwen.plugin.idea.apidoc.bean.dialog.FieldBean;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-01-25 18:26
 */
public enum ExampleUtils {

    INSTANCE;

    public String generateRequestExampleString(List<FieldBean> fieldBeanList) {
        if (CollectionUtils.isEmpty(fieldBeanList)) {
            return null;
        }
        JSONObject o = generateExampleRecursively(fieldBeanList);
        if (o == null) {
            return null;
        }
        List<String> parameterList = new ArrayList<>();
        for (String key : o.keySet()) {
            parameterList.add(String.format("%s=%s", key, JSONObject.toJSONString(o.get(key))));
        }
        return StringUtils.join(parameterList, ApiDocConstant.CHAR_AND);
    }

    public String generateExampleString(List<FieldBean> fieldBeanList) {
        if (CollectionUtils.isEmpty(fieldBeanList)) {
            return null;
        }
        JSONObject o = generateExampleRecursively(fieldBeanList);
        return JSONObject.toJSONString(o);
    }

    private JSONObject generateExampleRecursively(List<FieldBean> fieldBeanList) {
        if (CollectionUtils.isEmpty(fieldBeanList)) {
            return null;
        }
        JSONObject root = new JSONObject();
        for (FieldBean fieldBean : fieldBeanList) {
            List<FieldBean> childFieldBeanList = fieldBean.getChildFieldList();
            if (CollectionUtils.isEmpty(childFieldBeanList)) {
                root.put(fieldBean.getName(), generateDefaultFieldValue(fieldBean));
            } else {
                JSONObject o = generateExampleRecursively(childFieldBeanList);
                root.put(fieldBean.getName(), o);
            }
        }
        return root;
    }

    private Object generateDefaultFieldValue(FieldBean fieldBean) {
        if (fieldBean == null || fieldBean.getPsiType() == null) {
            return null;
        }
        Class cls = ClassUtils.INSTANCE.getClass(fieldBean.getPsiType());
        return JMockData.mock(cls);
    }

}
