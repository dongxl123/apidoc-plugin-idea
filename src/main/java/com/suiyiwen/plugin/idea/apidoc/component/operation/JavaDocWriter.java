package com.suiyiwen.plugin.idea.apidoc.component.operation;

import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocComment;
import org.jetbrains.annotations.NotNull;

/**
 * The interface Java doc writer.
 *
 * @author Sergey Timofiychuk
 */
public interface JavaDocWriter {

    /**
     * The constant WRITE_JAVADOC_COMMAND_NAME.
     */
    String WRITE_JAVADOC_COMMAND_NAME = "JavaDocWriter";
    /**
     * The constant WRITE_JAVADOC_COMMAND_GROUP.
     */
    String WRITE_JAVADOC_COMMAND_GROUP = "com.suiyiwen.plugin.idea.apidoc.component.operation";

    /**
     * Write java doc.
     *
     * @param javaDoc       the Java doc
     * @param beforeElement the element to place javadoc before it
     */
    void write(@NotNull PsiDocComment javaDoc, @NotNull PsiElement beforeElement);

}
