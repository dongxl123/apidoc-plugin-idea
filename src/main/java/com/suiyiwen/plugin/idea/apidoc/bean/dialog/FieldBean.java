package com.suiyiwen.plugin.idea.apidoc.bean.dialog;

import com.suiyiwen.plugin.idea.apidoc.ui.tree.CellProvider;
import com.suiyiwen.plugin.idea.apidoc.ui.tree.Selector;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2018-12-18 15:13
 */
@Getter
@Setter
public class FieldBean implements Selector, CellProvider, Serializable {

    private String name;
    private String type;
    private String description;
    private List<FieldBean> childFieldList;
    private transient boolean checked = true;

    @Override
    public String getCellTitle(int index) {
        if (index == 0) {
            return name;
        } else if (index == 1) {
            return type;
        } else if (index == 2) {
            return description;
        }
        return null;
    }

    @Override
    public void setValueAt(int column, String text) {
        if (column == 0) {
            setName(text);
        } else if (column == 1) {
            setType(text);
        } else if (column == 2) {
            setDescription(text);
        }
    }

    @Override
    public void setSelect(boolean select) {
        setChecked(select);
    }

    @Override
    public boolean getSelect() {
        return isChecked();
    }

}
