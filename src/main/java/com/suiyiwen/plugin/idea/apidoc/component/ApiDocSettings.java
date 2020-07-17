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

    private int depth;

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

    public static ApiDocSettings getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ApiDocSettings.class);
    }

    public static int getActualDepth(@NotNull Project project) {
        int depth = ApiDocSettings.getInstance(project).getDepth();
        if (depth > 0) {
            return depth;
        }
        //当前项目无配置
        if (!project.isDefault()) {
            //非默认项目，使用默认项目配置
            depth = ApiDocSettings.getInstance(ProjectManager.getInstance().getDefaultProject()).getDepth();
        }
        //默认项目或非默认项目获取不到配置，使用默认值
        if (depth <= 0) {
            depth = ApiDocConstant.OBJECT_EXTRACT_MAX_DEPTH;
        }
        return depth;
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
