package com.suiyiwen.plugin.idea.apidoc.manager;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.javadoc.PsiDocComment;
import com.suiyiwen.plugin.idea.apidoc.bean.dialog.DialogModel;
import com.suiyiwen.plugin.idea.apidoc.constant.ApiDocConstant;
import com.suiyiwen.plugin.idea.apidoc.helper.DialogHelper;
import com.suiyiwen.plugin.idea.apidoc.utils.PsiHttpUtils;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 12:05
 */
public class ApiDocActionManger {

    public void showDialog(AnActionEvent e) {
        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        Project project = e.getProject();
        if (psiElement instanceof PsiMethod) {
            PsiMethod psiMethod = (PsiMethod) psiElement;
            if (!PsiHttpUtils.INSTANCE.isController(psiMethod.getContainingClass())) {
                Messages.showWarningDialog(ApiDocConstant.NOTIFICATION_NOT_CONTROLLER_CONTENT, ApiDocConstant.NOTIFICATION_TITLE);
                return;
            }
            if (!PsiHttpUtils.INSTANCE.isRequestMethod(psiMethod)) {
                Messages.showWarningDialog(ApiDocConstant.NOTIFICATION_NOT_REQUEST_METHOD_CONTENT, ApiDocConstant.NOTIFICATION_TITLE);
                return;
            }
            PsiDocComment oldDocComment = null;
            PsiElement firstElement = psiMethod.getFirstChild();
            DialogModel oldDialogModel = null;
            if (firstElement instanceof PsiDocComment) {
                oldDocComment = (PsiDocComment) firstElement;
                oldDialogModel = DialogHelper.INSTANCE.parseOldDialogModel(oldDocComment);
            }
            DialogModel newDialogModel = DialogHelper.INSTANCE.createNewDialogModel(psiMethod);
            DialogModel mergeDialogModel = DialogHelper.INSTANCE.mergeDialogModel(newDialogModel, oldDialogModel);
            DialogHelper.INSTANCE.showGenerateDialog(mergeDialogModel, psiElement);
        } else {
            Messages.showWarningDialog(ApiDocConstant.NOTIFICATION_FOCUS_CONTENT, ApiDocConstant.NOTIFICATION_TITLE);
        }
    }
}
