package com.suiyiwen.plugin.idea.apidoc.component.operation.impl;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;
import com.intellij.openapi.vfs.ReadonlyStatusHandler.OperationStatus;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.util.PsiEditorUtil;
import com.suiyiwen.plugin.idea.apidoc.component.operation.JavaDocWriter;
import com.suiyiwen.plugin.idea.apidoc.exception.FileNotValidException;
import com.suiyiwen.plugin.idea.apidoc.exception.NotFoundElementException;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * The type Java doc writer impl.
 *
 * @author Sergey Timofiychuk
 */
public class JavaDocWriterImpl implements JavaDocWriter {

    private static final Logger LOGGER = Logger.getInstance(JavaDocWriterImpl.class);

    @Override
    public void write(@NotNull PsiDocComment javaDoc, @NotNull PsiElement beforeElement) {
        try {
            checkFilesAccess(beforeElement);
        } catch (FileNotValidException e) {
            LOGGER.error(e.getMessage());
            Messages.showErrorDialog("apiDoc plugin is not available, cause: " + e.getMessage(), "apiDoc plugin");
            return;
        }
        WriteCommandAction.writeCommandAction(beforeElement.getProject(), beforeElement.getContainingFile()).withName(WRITE_JAVADOC_COMMAND_NAME)
                .withGroupId(WRITE_JAVADOC_COMMAND_GROUP).run(() -> {
            if (javaDoc == null) {
                return;
            }
            if (beforeElement.getFirstChild() instanceof PsiDocComment) {
                replaceJavaDoc(beforeElement, javaDoc);
            } else {
                addJavaDoc(beforeElement, javaDoc);
            }
            ensureWhitespaceAfterJavaDoc(beforeElement);
            reformatJavaDoc(beforeElement);
        });
    }

    private void ensureWhitespaceAfterJavaDoc(PsiElement element) {
        // this method is required to create well formatted javadocs in enums
        PsiElement firstChild = element.getFirstChild();
        if (!PsiDocComment.class.isAssignableFrom(firstChild.getClass())) {
            return;
        }
        PsiElement nextElement = firstChild.getNextSibling();
        if (PsiWhiteSpace.class.isAssignableFrom(nextElement.getClass())) {
            return;
        }
        pushPostponedChanges(element);
        element.getNode().addChild(new PsiWhiteSpaceImpl("\n"), nextElement.getNode());
    }


    private static void deleteJavaDoc(PsiElement theElement) {
        pushPostponedChanges(theElement);
        theElement.getFirstChild().delete();
    }

    private static void addJavaDoc(PsiElement theElement, PsiDocComment theJavaDoc) {
        pushPostponedChanges(theElement);
        theElement.getNode().addChild(theJavaDoc.getNode(), theElement.getFirstChild().getNode());
    }

    private static void replaceJavaDoc(PsiElement theElement, PsiDocComment theJavaDoc) {
        deleteJavaDoc(theElement);
        addJavaDoc(theElement, theJavaDoc);
    }

    private static void reformatJavaDoc(PsiElement theElement) {
        CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(theElement.getProject());
        pushPostponedChanges(theElement);
        try {
            int javadocTextOffset = findJavaDocTextOffset(theElement);
            int javaCodeTextOffset = findJavaCodeTextOffset(theElement);
            codeStyleManager.reformatText(theElement.getContainingFile(), javadocTextOffset, javaCodeTextOffset + 1);
        } catch (NotFoundElementException e) {
            LOGGER.info("Could not reformat javadoc since cannot find required elements", e);
        }
    }

    private static int findJavaDocTextOffset(PsiElement theElement) {
        PsiElement javadocElement = theElement.getFirstChild();
        if (!(javadocElement instanceof PsiDocComment)) {
            throw new NotFoundElementException("Cannot find element of type PsiDocComment");
        }
        return javadocElement.getTextOffset();
    }

    private static int findJavaCodeTextOffset(PsiElement theElement) {
        if (theElement.getChildren().length < 2) {
            throw new NotFoundElementException("Can not find offset of java code");
        }
        return theElement.getChildren()[1].getTextOffset();
    }

    private static void pushPostponedChanges(PsiElement element) {
        Editor editor = PsiEditorUtil.Service.getInstance().findEditorByPsiElement(element);
        if (editor != null) {
            PsiDocumentManager.getInstance(element.getProject())
                    .doPostponedOperationsAndUnblockDocument(editor.getDocument());
        }
    }

    private void checkFilesAccess(@NotNull PsiElement beforeElement) {
        PsiFile containingFile = beforeElement.getContainingFile();
        if (containingFile == null || !containingFile.isValid()) {
            throw new FileNotValidException("File cannot be used to generate javadocs");
        }
        OperationStatus status = ReadonlyStatusHandler.getInstance(beforeElement.getProject()).
                ensureFilesWritable(Collections.singletonList(containingFile.getVirtualFile()));
        if (status.hasReadonlyFiles()) {
            throw new FileNotValidException(status.getReadonlyFilesMessage());
        }
    }
}
