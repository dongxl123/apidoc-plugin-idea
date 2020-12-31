package com.suiyiwen.plugin.idea.apidoc.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
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
    private JCheckBox usingSnakeCaseCheckBox;
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
        ApiDocSettings settings = ApiDocSettings.getInstance(project);
        return settings.getDepth() != getDepthValue() || settings.isUsingSnakeCase() != usingSnakeCaseCheckBox.isSelected();
    }

    @Override
    public void apply() throws ConfigurationException {
        ApiDocSettings settings = ApiDocSettings.getInstance(project);
        settings.setDepth(getDepthValue());
        settings.setUsingSnakeCase(usingSnakeCaseCheckBox.isSelected());
        settings.setSaved(true);
    }

    @Override
    public void reset() {
        initFromSettings();
    }

    private void initFromSettings() {
        ApiDocSettings settings = ApiDocSettings.getInstance(project);
        depthTextField.setText(String.valueOf(settings.getDepth()));
        usingSnakeCaseCheckBox.setSelected(settings.isUsingSnakeCase());
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
