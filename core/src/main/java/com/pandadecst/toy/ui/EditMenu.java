package com.pandadecst.toy.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.pandadecst.toy.tool.ModelLoader;
import com.badlogic.gdx.graphics.g3d.Model;
import com.pandadecst.toy.physics.base.BaseBulletTest;
import com.pandadecst.toy.tool.BulletConstructor;

public class EditMenu extends Menu {

    private MenuItem copy;
    private MenuItem paste;
    private MenuItem undo;
    private MenuItem redo;
    private MenuItem add;
    public FileChoosers f = new FileChoosers();
    ModelLoader modelLoader = new ModelLoader();
    Model model;

    public EditMenu(final Stage stage, final BaseBulletTest bbt) {
        super("编辑");
       
        copy = new MenuItem("复制");
        paste = new MenuItem("粘贴");
        undo = new MenuItem(" < ");
        redo = new MenuItem(" > ");
        add = new MenuItem("添加");
        
        f.loadFileChooser.setListener(new FileChooserAdapter() {
                @Override
                public void selected(Array<FileHandle> file) {
                    model = modelLoader.loadModel(file.get(0));
                    bbt.world.addConstructor("new", new BulletConstructor(model, 1f));
                }
            });
        
        add.addListener(new ChangeListener() {
                @Override
                public void changed (ChangeEvent event, Actor actor) {
                    stage.addActor(f.loadFileChooser.fadeIn());
                }
            });

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
    
    public Model getModel() {
        return model;
    }
}
