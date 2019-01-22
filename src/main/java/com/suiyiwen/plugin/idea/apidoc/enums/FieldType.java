package com.suiyiwen.plugin.idea.apidoc.enums;

/**
 * @author dongxuanliang252
 * @date 2018-12-28 17:48
 */
public enum FieldType {

    Number(1),
    String(2),
    Boolean(3),
    Array(4),
    Object(5),
    ;

    private Integer code;

    FieldType(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static FieldType getFieldType(String name) {
        for (FieldType fieldType : FieldType.values()) {
            if (fieldType.name().equalsIgnoreCase(name)) {
                return fieldType;
            }
        }
        return null;
    }

    public static FieldType getFieldType(Integer code) {
        for (FieldType fieldType : FieldType.values()) {
            if (fieldType.getCode().equals(code)) {
                return fieldType;
            }
        }
        return null;
    }

}
