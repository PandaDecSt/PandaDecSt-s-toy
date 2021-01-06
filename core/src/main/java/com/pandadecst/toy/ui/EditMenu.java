package com.pandadecst.toy.ui;

import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuItem;

public class EditMenu extends Menu {

    private MenuItem copy;
    private MenuItem paste;
    private MenuItem undo;
    private MenuItem redo;
    private MenuItem add;

    public EditMenu() {
        super("编辑");

        copy = new MenuItem("复制");
        paste = new MenuItem("粘贴");
        undo = new MenuItem(" < ");
        redo = new MenuItem(" > ");
        add = new MenuItem("添加");

        addItem(copy);
        addItem(paste);
        addItem(undo);
        addItem(redo);
        addItem(add);

    }

    public MenuItem getRedo() {
        return redo;
    }

    public MenuItem getCopy() {
        return copy;
    }

    public MenuItem getPaste() {
        return paste;
    }

    public MenuItem getUndo() {
        return undo;
    }
    
    public MenuItem getAdd() {
        return add;
    }
}
