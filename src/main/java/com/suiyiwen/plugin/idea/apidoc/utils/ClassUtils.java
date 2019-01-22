package com.suiyiwen.plugin.idea.apidoc.utils;

/**
 * @author dongxuanliang252
 * @date 2019-01-04 14:18
 */
public enum ClassUtils {

    INSTANCE;

    public <T> T newInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
