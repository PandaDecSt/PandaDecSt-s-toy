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

public class UiCreator {

//    public void resize(int width, int height) {
//        getViewport().update(width, height, true);
//    }

    public static void createVisUi(final Stage stage, InputMultiplexer inputMultiplexer) {
        Logger.log("xyUi", "ui初始化ing");
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
                    Logger.log("xyUi", "Opened menu: " + menu.getTitle());
                }

                @Override
                public void menuClosed(Menu menu) {
                    Logger.log("xyUi", "Closed menu: " + menu.getTitle());
                }
            });
        root.add(menuBar.getTable()).expandX().fillX().row();
        root.add().expand().fill();

        createMenus(stage, menuBar);
        
        Logger.log("xyUi", "ui初始化完毕");

//      stage.addActor(new TestMultiSplitPane());       
    }
    
    public static void createNewObj(Stage stage, Actor obj){
        stage.addActor(obj);
    }

    private static void createMenus(final Stage stage, MenuBar menuBar) {

        HelpMenu helpMenu = new HelpMenu(stage);
        EditMenu editMenu = new EditMenu();
        FileMenu fileMenu = new FileMenu(stage);
        WindowMenu windowMenu = new WindowMenu();

        menuBar.addMenu(fileMenu);
        menuBar.addMenu(editMenu);
        menuBar.addMenu(windowMenu);
        menuBar.addMenu(helpMenu);
        Logger.log("xyUi", "菜单创建完毕");
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
