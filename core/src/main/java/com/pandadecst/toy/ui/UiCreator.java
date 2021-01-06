package com.pandadecst.toy.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.pandadecst.toy.tool.Logger;
import com.pandadecst.toy.physics.base.BaseBulletTest;

public class UiCreator {

    public static HelpMenu helpMenu;
    public static FileMenu fileMenu;
    public static WindowMenu windowMenu;
    public static EditMenu editMenu;
    
    static BaseBulletTest bbt;
    
    public static void createVisUi(final Stage stage, InputMultiplexer inputMultiplexer, BaseBulletTest b) {
        bbt = b;
        Logger.log("Ui", "ui初始化ing");
        final Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        MenuBar menuBar;
//        if(stage != null){
//        inputMultiplexer.addProcessor(stage);
//        Gdx.input.setInputProcessor(inputMultiplexer);
//}
        menuBar = new MenuBar();
        menuBar.setMenuListener(new MenuBar.MenuBarListener() {
                @Override
                public void menuOpened(Menu menu) {
                    Logger.log("Ui", "Opened menu: " + menu.getTitle());
                }

                @Override
                public void menuClosed(Menu menu) {
                    Logger.log("Ui", "Closed menu: " + menu.getTitle());
                }
            });
        root.add(menuBar.getTable()).expandX().fillX().row();
        root.add().expand().fill();

        createMenus(stage, menuBar);

        Logger.log("Ui", "ui初始化完毕");

//      stage.addActor(new TestMultiSplitPane());       
    }

    public static void createNewObj(Stage stage, Actor obj) {
        stage.addActor(obj);
    }

    private static void createMenus(final Stage stage, MenuBar menuBar) {

        helpMenu = new HelpMenu(stage);
        editMenu = new EditMenu(stage, bbt);
        fileMenu = new FileMenu(stage);
        windowMenu = new WindowMenu();

        menuBar.addMenu(fileMenu);
        menuBar.addMenu(editMenu);
        menuBar.addMenu(windowMenu);
        menuBar.addMenu(helpMenu);
        Logger.log("Ui", "菜单创建完毕");
    }

    private static MenuItem createDoubleNestedMenu() {
        MenuItem doubleNestedMenuItem = new MenuItem("submenu nested x2");
        doubleNestedMenuItem.setSubMenu(createSubMenu());

        PopupMenu nestedMenu = new PopupMenu();
        nestedMenu.addItem(doubleNestedMenuItem);
        nestedMenu.addItem(new MenuItem("single nested"));

        MenuItem menuItem = new MenuItem("submenu nested");
        menuItem.setSubMenu(nestedMenu);
        return menuItem;
    }

    private static PopupMenu createSubMenu() {
        PopupMenu menu = new PopupMenu();
        menu.addItem(new MenuItem("submenuitem #1"));
        menu.addItem(new MenuItem("submenuitem #2"));
        menu.addSeparator();
        menu.addItem(new MenuItem("submenuitem #3"));
        menu.addItem(new MenuItem("submenuitem #4"));
        return menu;
    }

    private static MenuItem 创建显示设置菜单(final Stage stage) {
        MenuItem item = new MenuItem("显示");

        PopupMenu menu = new PopupMenu();
        menu.addItem(new MenuItem("视野", new ChangeListener() {
                             @Override
                             public void changed(ChangeEvent event, Actor actor) {

                             }
                         }));
        menu.addItem(new MenuItem("submenuitem #2"));
        menu.addSeparator();
        menu.addItem(new MenuItem("submenuitem #3"));
        menu.addItem(new MenuItem("submenuitem #4"));

        item.setSubMenu(menu);
        return item;
	}

}
