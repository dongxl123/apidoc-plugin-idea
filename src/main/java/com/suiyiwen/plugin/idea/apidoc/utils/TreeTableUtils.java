package com.suiyiwen.plugin.idea.apidoc.utils;

import com.suiyiwen.plugin.idea.apidoc.bean.dialog.AbstractExampleBean;
import com.suiyiwen.plugin.idea.apidoc.bean.dialog.FieldBean;
import com.suiyiwen.plugin.idea.apidoc.ui.tree.CheckTreeTableManager;
import com.suiyiwen.plugin.idea.apidoc.ui.tree.FiledTreeTableModel;
import com.suiyiwen.plugin.idea.apidoc.ui.tree.Selector;
import org.apache.commons.collections.CollectionUtils;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


/**
 * @author dongxuanliang252
 * @date 2019-01-14 10:25
 */
public enum TreeTableUtils {

    INSTANCE;

    public JXTreeTable createTreeTable(AbstractExampleBean exampleBean) {
        TreeTableModel treeModel = createTreeTableModel(exampleBean);
        JXTreeTable treeTable = new JXTreeTable(treeModel);
        CheckTreeTableManager manager = new CheckTreeTableManager(treeTable);
        List<TreeTableNode> selectedNodeList = getSelectedNodeList((TreeTableNode) treeModel.getRoot());
        if (CollectionUtils.isNotEmpty(selectedNodeList)) {
            manager.getSelectionModel().addPathsByNodes(selectedNodeList);
        }
        treeTable.expandAll();
        treeTable.setCellSelectionEnabled(false);
        treeTable.setShowGrid(true, true);
        treeTable.setRowHeight(25);
        DefaultListSelectionModel defaultListSelectionModel = new DefaultListSelectionModel();
        defaultListSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        treeTable.setSelectionModel(defaultListSelectionModel);
        return treeTable;
    }

    private TreeTableModel createTreeTableModel(AbstractExampleBean exampleBean) {
        if (exampleBean == null || CollectionUtils.isEmpty(exampleBean.getFieldList())) {
            return null;
        }
        FieldBean rootFieldBean = new FieldBean();
        rootFieldBean.setName("root");
        rootFieldBean.setChildFieldList(exampleBean.getFieldList());
        TreeTableNode root = createdTreeNode(rootFieldBean);
        TreeTableModel treeTableModel = new FiledTreeTableModel(root);
        return treeTableModel;
    }

    private MutableTreeTableNode createdTreeNode(FieldBean fieldBean) {
        if (fieldBean == null) {
            return null;
        }
        DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode(fieldBean);
        if (CollectionUtils.isEmpty(fieldBean.getChildFieldList())) {
            return root;
        }
        for (FieldBean childFieldBean : fieldBean.getChildFieldList()) {
            root.add(createdTreeNode(childFieldBean));
        }
        return root;
    }

    private List<TreeTableNode> getSelectedNodeList(TreeTableNode root) {
        List<TreeTableNode> allNodeList = getRecursiveLeafNodeList(root);
        if (CollectionUtils.isEmpty(allNodeList)) {
            return null;
        }
        List<TreeTableNode> selectedNodeList = new ArrayList<>();
        for (TreeTableNode node : allNodeList) {
            Selector selector = (Selector) node.getUserObject();
            if (selector.getSelect()) {
                selectedNodeList.add(node);
            }
        }
        return selectedNodeList;
    }

    private List<TreeTableNode> getRecursiveLeafNodeList(TreeTableNode root) {
        if (root == null || root.getChildCount() == 0) {
            return null;
        }
        Enumeration itr = root.children();
        List<TreeTableNode> childNodeList = new ArrayList<>();
        while (itr.hasMoreElements()) {
            TreeTableNode childNode = (TreeTableNode) itr.nextElement();
            if (childNode.isLeaf()) {
                childNodeList.add(childNode);
            } else {
                List<TreeTableNode> nextChildNodeList = getRecursiveLeafNodeList(childNode);
                if (CollectionUtils.isNotEmpty(nextChildNodeList)) {
                    childNodeList.addAll(nextChildNodeList);
                }
            }
        }
        return childNodeList;
    }


}
