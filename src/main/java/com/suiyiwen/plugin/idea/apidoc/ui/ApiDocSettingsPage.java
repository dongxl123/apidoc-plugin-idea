package com.suiyiwen.plugin.idea.apidoc.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
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
    private ApiDocSettings apiDocSettings = ApiDocSettings.getInstance();

    @NotNull
    @Override
    public String getId() {
        return getDisplayName();
    }

    @Nullable
    @Override
    public Runnable enableSearch(String option) {
        return null;
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return ApiDocConstant.PLUGIN_NAME;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return myGeneralPanel;
    }

    @Override
    public boolean isModified() {
        return apiDocSettings.getDepth() != getDepthValue();
    }

    @Override
    public void apply() throws ConfigurationException {
        apiDocSettings.setDepth(getDepthValue());
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

    @Override
    public void reset() {
        depthTextField.setText(String.valueOf(apiDocSettings.getDepth()));
    }

    @Override
    public void disposeUIResources() {

    }
}
