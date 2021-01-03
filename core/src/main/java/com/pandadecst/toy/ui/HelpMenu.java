package com.pandadecst.toy.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuItem;

public class HelpMenu extends Menu {

    public HelpMenu(final Stage stage) {
        super("帮助");
        this.addItem(new MenuItem("关于", new ChangeListener() {
                                 @Override
                                 public void changed (ChangeEvent event, Actor actor) {
                                     Dialogs.showOKDialog(stage, "关于", "编辑器版本: " + 0);
                                 }
                             }));
    }    
       
}
