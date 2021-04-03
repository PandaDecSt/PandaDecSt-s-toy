package com.pandadecst.toy.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.pandadecst.toy.tool.G3DConverter;
import java.io.IOException;

public class FileMenu extends Menu {

    private MenuItem newProject;
    private MenuItem openProject;
    private MenuItem saveProject;
    private MenuItem converte;
    FileChoosers f = new FileChoosers();

    public FileMenu(final Stage stage) {
        super("项目");

        newProject = new MenuItem("创建项目");
        openProject = new MenuItem("打开项目", new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    stage.addActor(f.loadFileChooser.fadeIn());
                }
            });
        saveProject = new MenuItem("保存项目", new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    stage.addActor(f.saveFileChooser.fadeIn());
                }
            });
        converte = new MenuItem("模型格式转换", new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    f.loadFileChooser.setListener(new FileChooserAdapter() {
                            @Override
                            public void selected(Array<FileHandle> file) {
                                try {
                                    G3DConverter.g3db2g3dj(file.get(0), false);
                                } catch (IOException e) {}
                            }
                        });
              
                    stage.addActor(f.loadFileChooser.fadeIn());
                }
            });

        addItem(newProject);
        addItem(openProject);
        addItem(saveProject);
        addItem(converte);
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
