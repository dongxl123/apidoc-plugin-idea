package com.suiyiwen.plugin.idea.apidoc.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.components.JBScrollPane;
import com.suiyiwen.plugin.idea.apidoc.bean.dialog.DialogModel;
import com.suiyiwen.plugin.idea.apidoc.component.ApiDocSettings;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import com.suiyiwen.plugin.idea.apidoc.helper.DialogHelper;
import com.suiyiwen.plugin.idea.apidoc.utils.FieldBeanTreeUtils;
import com.suiyiwen.plugin.idea.apidoc.utils.TreeTableUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.JXTreeTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;


public class ApiDocGenerateDialog extends DialogWrapper {

    private JPanel contentPanel;
    private JFormattedTextField groupNameTextField;
    private JFormattedTextField nameTextField;
    private JTextArea description;
    private JFormattedTextField versionTextField;
    private JTextField requestTitleTextFiled;
    private JTextField requestUrlTextField;
    private JBScrollPane requestParameterPanel;
    private JBScrollPane requestBodyPanel;
    private JBScrollPane responseBodyPanel;
    private JComboBox requestMethodComboBox;
    private JBRadioButton yesRadioButton;
    private JBRadioButton noRadioButton;
    private DialogModel model;
    private PsiElement psiElement;

    public ApiDocGenerateDialog(boolean canBeParent, @NotNull DialogModel initModel, @NotNull PsiElement psiElement) {
        super(canBeParent);
        this.model = initModel;
        this.psiElement = psiElement;
        init();
        setModal(false);
        setTitle(ApiDocConstant.TITLE_GENERATE_DIALOG);
        setSize(800, 800);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        refreshWithInitModel();
        return contentPanel;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return super.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    protected String getDimensionServiceKey() {
        return "apiDocId";
    }

    @Override
    public void doOKAction() {
        saveSettings();
        generateComment();
        super.doOKAction();
    }

    private void saveSettings() {
        ApiDocSettings settings = ApiDocSettings.getInstance(psiElement.getProject());
        if (settings == null) {
            return;
        }
        if (StringUtils.isNotBlank(versionTextField.getText())) {
            settings.setVersion(versionTextField.getText());
        }
    }

    private void generateComment() {
        DialogHelper.INSTANCE.writeJavaDoc(getCurrentModel(), psiElement);
    }

    /**
     * 初始化数据
     */
    private void refreshWithInitModel() {
        renderModel(this.model);
    }

    private DialogModel getCurrentModel() {
        if (StringUtils.isNotBlank(groupNameTextField.getText())) {
            model.setGroupName(groupNameTextField.getText());
        }
        if (StringUtils.isNotBlank(nameTextField.getText())) {
            model.setName(nameTextField.getText());
        }
        if (StringUtils.isNotBlank(description.getText())) {
            model.setDescription(description.getText());
        }
        if (StringUtils.isNotBlank(versionTextField.getText())) {
            model.setVersion(versionTextField.getText());
        }
        if (StringUtils.isNotBlank(requestTitleTextFiled.getText())) {
            model.setRequestTitle(requestTitleTextFiled.getText());
        }
        if (StringUtils.isNotBlank(requestUrlTextField.getText())) {
            model.setRequestUrl(requestUrlTextField.getText());
        }
        if (requestMethodComboBox.getSelectedItem() != null) {
            model.setRequestMethod(requestMethodComboBox.getSelectedItem().toString());
        }
        if (BooleanUtils.isTrue(yesRadioButton.isSelected())) {
            model.setReGenerateExampleFlag(true);
        } else {
            model.setReGenerateExampleFlag(false);
        }
        return model;
    }

    private void renderModel(DialogModel model) {
        if (model == null) {
            return;
        }
        if (StringUtils.isNotBlank(model.getGroupName())) {
            groupNameTextField.setText(model.getGroupName());
        }
        if (StringUtils.isNotBlank(model.getName())) {
            nameTextField.setText(model.getName());
        }
        if (StringUtils.isNotBlank(model.getDescription())) {
            description.setText(model.getDescription());
        }
        if (StringUtils.isNotBlank(model.getVersion())) {
            versionTextField.setText(model.getVersion());
        }
        if (StringUtils.isNotBlank(model.getRequestTitle())) {
            requestTitleTextFiled.setText(model.getRequestTitle());
        }
        if (StringUtils.isNotBlank(model.getRequestUrl())) {
            requestUrlTextField.setText(model.getRequestUrl());
        }
        if (StringUtils.isNotBlank(model.getRequestMethod())) {
            requestMethodComboBox.setSelectedItem(model.getRequestMethod());
        }
        int requestParameterLines = 0;
        if (model.getRequestParameter() != null) {
            JXTreeTable treeTable = TreeTableUtils.INSTANCE.createTreeTable(model.getRequestParameter());
            requestParameterPanel.setViewportView(treeTable);
            requestParameterLines = FieldBeanTreeUtils.INSTANCE.getLines(model.getRequestParameter().getFieldList());
        }
        requestParameterPanel.setMinimumSize(new Dimension(-1, Math.min(Math.max(requestParameterLines * ApiDocConstant.UI_LINE_MIN_SIZE, ApiDocConstant.UI_MIN_SIZE), ApiDocConstant.UI_MAX_SIZE)));
        requestParameterPanel.setMaximumSize(new Dimension(-1, Math.min(requestParameterLines * ApiDocConstant.UI_LINE_MAX_SIZE, ApiDocConstant.UI_MAX_SIZE)));
        requestParameterPanel.setPreferredSize(new Dimension(-1, Math.min(requestParameterLines * ApiDocConstant.UI_LINE_PREFER_SIZE, ApiDocConstant.UI_MAX_SIZE)));
        int requestBodyLines = 0;
        if (model.getRequestBody() != null) {
            JXTreeTable treeTable = TreeTableUtils.INSTANCE.createTreeTable(model.getRequestBody());
            requestBodyPanel.setViewportView(treeTable);
            requestBodyLines = FieldBeanTreeUtils.INSTANCE.getLines(model.getRequestBody().getFieldList());
        }
        requestBodyPanel.setMinimumSize(new Dimension(-1, Math.min(Math.max(requestBodyLines * ApiDocConstant.UI_LINE_MIN_SIZE, ApiDocConstant.UI_MIN_SIZE), ApiDocConstant.UI_MAX_SIZE)));
        requestBodyPanel.setMaximumSize(new Dimension(-1, Math.min(requestBodyLines * ApiDocConstant.UI_LINE_MAX_SIZE, ApiDocConstant.UI_MAX_SIZE)));
        requestBodyPanel.setPreferredSize(new Dimension(-1, Math.min(requestBodyLines * ApiDocConstant.UI_LINE_PREFER_SIZE, ApiDocConstant.UI_MAX_SIZE)));
        int responseBodyLines = 0;
        if (model.getResponseBody() != null) {
            JXTreeTable treeTable = TreeTableUtils.INSTANCE.createTreeTable(model.getResponseBody());
            responseBodyPanel.setViewportView(treeTable);
            responseBodyLines = FieldBeanTreeUtils.INSTANCE.getLines(model.getResponseBody().getFieldList());
        }
        responseBodyPanel.setMinimumSize(new Dimension(-1, Math.min(Math.max(responseBodyLines * ApiDocConstant.UI_LINE_MIN_SIZE, ApiDocConstant.UI_MIN_SIZE), ApiDocConstant.UI_MAX_SIZE)));
        responseBodyPanel.setMaximumSize(new Dimension(-1, Math.min(responseBodyLines * ApiDocConstant.UI_LINE_MAX_SIZE, ApiDocConstant.UI_MAX_SIZE)));
        responseBodyPanel.setPreferredSize(new Dimension(-1, Math.min(responseBodyLines * ApiDocConstant.UI_LINE_PREFER_SIZE, ApiDocConstant.UI_MAX_SIZE)));
    }
}
