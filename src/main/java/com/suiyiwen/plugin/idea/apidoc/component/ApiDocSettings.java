package com.suiyiwen.plugin.idea.apidoc.component;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author dongxuanliang252
 * @date 2019-01-02 15:12
 */
@State(
        name = "ApiDocSettings",
        storages = @Storage(value = "apidoc.xml")
)
public class ApiDocSettings implements PersistentStateComponent<ApiDocSettings> {

    private int depth = ApiDocConstant.OBJECT_EXTRACT_MAX_DEPTH;

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public static ApiDocSettings getInstance() {
        return ServiceManager.getService(ApiDocSettings.class);
    }

    @Nullable
    @Override
    public ApiDocSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull ApiDocSettings state) {
        this.depth = state.getDepth();
    }
}
