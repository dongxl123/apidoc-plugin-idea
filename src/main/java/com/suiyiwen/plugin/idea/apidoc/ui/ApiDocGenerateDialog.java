package com.suiyiwen.plugin.idea.apidoc.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBScrollPane;
import com.suiyiwen.plugin.idea.apidoc.bean.dialog.DialogModel;
import com.suiyiwen.plugin.idea.apidoc.component.ApiDocSettings;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import com.suiyiwen.plugin.idea.apidoc.helper.DialogHelper;
import com.suiyiwen.plugin.idea.apidoc.utils.TreeTableUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.JXTreeTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


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
    private DialogModel model;
    private PsiElement psiElement;

    public ApiDocGenerateDialog(boolean canBeParent, @NotNull DialogModel initModel, @NotNull PsiElement psiElement) {
        super(canBeParent);
        this.model = initModel;
        this.psiElement = psiElement;
        init();
        setTitle(ApiDocConstant.TITLE_GENERATE_DIALOG);
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
        generateComment();
        super.doOKAction();
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
        if (model.getRequestParameter() != null) {
            JXTreeTable treeTable = TreeTableUtils.INSTANCE.createTreeTable(model.getRequestParameter());
            requestParameterPanel.setViewportView(treeTable);
        }
        if (model.getRequestBody() != null) {
            JXTreeTable treeTable = TreeTableUtils.INSTANCE.createTreeTable(model.getRequestBody());
            requestBodyPanel.setViewportView(treeTable);
        }
        if (model.getResponseBody() != null) {
            JXTreeTable treeTable = TreeTableUtils.INSTANCE.createTreeTable(model.getResponseBody());
            responseBodyPanel.setViewportView(treeTable);
        }
    }
}
