package com.pandadecst.toy.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

public class FileMenu extends Menu {

    private MenuItem newProject;
    private MenuItem openProject;
    private MenuItem saveProject;

    public FileMenu(final Stage stage) {
        super("项目");
        
        final FileChooser saveFileChooser = new FileChooser(FileChooser.Mode.SAVE);
        saveFileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        saveFileChooser.setDefaultFileName("current_data.autom");
        saveFileChooser.setListener(new FileChooserAdapter() {
                @Override
                public void selected(Array<FileHandle> file) {

                }
            });

        final FileChooser loadFileChooser = new FileChooser(FileChooser.Mode.OPEN);
        loadFileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        loadFileChooser.setListener(new FileChooserAdapter() {
                @Override
                public void selected(Array<FileHandle> file) {

                }
            });

        newProject = new MenuItem("创建项目");
        openProject = new MenuItem("打开项目",new ChangeListener() {
                @Override
                public void changed (ChangeEvent event, Actor actor) {
                    stage.addActor(loadFileChooser.fadeIn());
                }
            });
        saveProject = new MenuItem("保存项目",new ChangeListener() {
                @Override
                public void changed (ChangeEvent event, Actor actor) {
                    stage.addActor(saveFileChooser.fadeIn());
                }
            });

        addItem(newProject);
        addItem(openProject);
        addItem(saveProject);
    }

    public MenuItem getNewProject() {
        return newProject;
    }

    public MenuItem getOpenProject() {
        return openProject;
    }

    public MenuItem getSaveProject() {
        return saveProject;
    }
    
}
