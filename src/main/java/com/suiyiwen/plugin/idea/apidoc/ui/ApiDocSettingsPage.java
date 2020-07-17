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
        initFromSettings();
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

    @Override
    public void reset() {
        initFromSettings();
    }

    private void initFromSettings() {
        int depth = ApiDocSettings.getActualDepth(project);
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
