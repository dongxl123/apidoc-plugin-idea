package com.suiyiwen.plugin.idea.apidoc.component;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
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

    private boolean saved;

    private int depth;

    private boolean usingSnakeCase;

    private String version;

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

    public boolean isUsingSnakeCase() {
        return usingSnakeCase;
    }

    public void setUsingSnakeCase(boolean usingSnakeCase) {
        this.usingSnakeCase = usingSnakeCase;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public static ApiDocSettings getInstance(@NotNull Project project) {
        ApiDocSettings currentProjectSettings = ServiceManager.getService(project, ApiDocSettings.class);
        if (currentProjectSettings.isSaved()) {
            return currentProjectSettings;
        } else {
            if (!project.isDefault()) {
                //普通项目,则使用通用配置
                ApiDocSettings commonSettings = ServiceManager.getService(ProjectManager.getInstance().getDefaultProject(), ApiDocSettings.class);
                if (commonSettings.isSaved()) {
                    currentProjectSettings.setDepth(commonSettings.getDepth());
                    currentProjectSettings.setUsingSnakeCase(commonSettings.isUsingSnakeCase());
                    return currentProjectSettings;
                }
            }
            //使用默认配置
            currentProjectSettings.setDepth(ApiDocConstant.OBJECT_EXTRACT_MAX_DEPTH);
            currentProjectSettings.setUsingSnakeCase(false);
            return currentProjectSettings;
        }
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
        this.usingSnakeCase = state.isUsingSnakeCase();
        this.saved = state.isSaved();
    }

}
