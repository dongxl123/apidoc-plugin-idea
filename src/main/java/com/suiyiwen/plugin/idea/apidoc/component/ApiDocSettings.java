package com.suiyiwen.plugin.idea.apidoc.component;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author dongxuanliang252
 * @date 2019-01-02 15:12
 */
@State(
        name = "ApiDocSettings",
        storages = @Storage(StoragePathMacros.PRODUCT_WORKSPACE_FILE)
)
public class ApiDocSettings implements PersistentStateComponent<ApiDocSettings> {

    private int depth = ApiDocConstant.OBJECT_EXTRACT_MAX_DEPTH;

    private String version = ApiDocConstant.DEFAULT_VERSION;

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static ApiDocSettings getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ApiDocSettings.class);
    }

    @Nullable
    @Override
    public ApiDocSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ApiDocSettings state) {
        this.depth = state.getDepth();
        this.version = state.getVersion();
    }
}
