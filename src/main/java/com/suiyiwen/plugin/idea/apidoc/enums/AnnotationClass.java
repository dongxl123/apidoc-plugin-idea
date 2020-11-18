package com.suiyiwen.plugin.idea.apidoc.enums;

/**
 * @author dongxuanliang252
 * @date 2019-01-21 16:14
 */
public enum AnnotationClass {

    POST_MAPPING("org.springframework.web.bind.annotation.PostMapping"),
    GET_MAPPING("org.springframework.web.bind.annotation.GetMapping"),
    PUT_MAPPING("org.springframework.web.bind.annotation.PutMapping"),
    DELETE_MAPPING("org.springframework.web.bind.annotation.DeleteMapping"),
    PATCH_MAPPING("org.springframework.web.bind.annotation.PatchMapping"),
    REQUEST_MAPPING("org.springframework.web.bind.annotation.RequestMapping"),
    REQUEST_BODY("org.springframework.web.bind.annotation.RequestBody"),
    REST_CONTROLLER("org.springframework.web.bind.annotation.RestController"),
    CONTROLLER("org.springframework.stereotype.Controller"),
    REQUEST_PARAM("org.springframework.web.bind.annotation.RequestParam"),
    ;

    private String className;

    AnnotationClass(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
