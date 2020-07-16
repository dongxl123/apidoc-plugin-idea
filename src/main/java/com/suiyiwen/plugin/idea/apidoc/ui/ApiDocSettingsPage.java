package com.suiyiwen.plugin.idea.apidoc.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.suiyiwen.plugin.idea.apidoc.component.ApiDocSettings;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author dongxuanliang252
 * @date 2019-03-05 15:20
 */
public class ApiDocSettingsPage implements SearchableConfigurable, Configurable.NoScroll {
    private JPanel myGeneralPanel;
    private JTextField depthTextField;
    private Project project;

    public ApiDocSettingsPage(@NotNull Project project) {
        this.project = project;
    }

    @NotNull
    @Override
    public String getId() {
        return getDisplayName();
    }


    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return ApiDocConstant.PLUGIN_NAME;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        initSettings();
        return myGeneralPanel;
    }

    @Override
    public boolean isModified() {
        return ApiDocSettings.getInstance(project).getDepth() != getDepthValue();
    }

    @Override
    public void apply() throws ConfigurationException {
        ApiDocSettings.getInstance(project).setDepth(getDepthValue());
    }

    private void initSettings() {
        int depth = ApiDocSettings.getInstance(project).getDepth();
        //当前项目无配置
        if (depth <= 0) {
            if (!project.isDefault()) {
                //非默认项目，使用默认项目配置
                depth = ApiDocSettings.getInstance(ProjectManager.getInstance().getDefaultProject()).getDepth();
            }
            //默认项目或非默认项目获取不到配置，使用默认值
            if (depth <= 0) {
                depth = ApiDocConstant.OBJECT_EXTRACT_MAX_DEPTH;
            }
            ApiDocSettings.getInstance(project).setDepth(depth);
        }
        depthTextField.setText(String.valueOf(depth));
    }

    private int getDepthValue() {
        String text = depthTextField.getText();
        int depth = ApiDocConstant.OBJECT_EXTRACT_MAX_DEPTH;
        if (StringUtils.isNotBlank(text)) {
            try {
                depth = Integer.valueOf(text);
            } catch (Exception e) {
            }
        }
        return depth;
    }

}
