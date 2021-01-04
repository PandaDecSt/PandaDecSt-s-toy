package com.pandadecst.toy.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pandadecst.toy.tool.Logger;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ObjectController extends VisWindow {

    public boolean 
    frontPressed = false,
    backPressed = false,
    leftPressed = false,
    rightPressed = false;

    public ObjectController() {
        super("物理对象控制器");

        TableUtils.setSpacingDefaults(this);
        columnDefaults(0).left();

        addVisWidgets();        

        setSize(150, 150);
        setResizable(true);
		setPosition(20, 20);
    }

    private void addVisWidgets() {
        Image
            front = new Image(new Texture("img/front.png")),
            back = new Image(new Texture("img/back.png")),
            left = new Image(new Texture("img/left.png")),
            right = new Image(new Texture("img/right.png"));

        front.addListener(new ClickListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    frontPressed = true;
                    Logger.log("input", "td");
                    return false;
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    frontPressed = false;
                    Logger.log("input", "tu");
                }
            });

        back.addListener(new ClickListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    backPressed = true;
                    return false;
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    backPressed = false;
                }
            });

        left.addListener(new ClickListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    leftPressed = true;
                    rightPressed = false;
                    return false;
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    leftPressed = false;
                }
            });

        right.addListener(new ClickListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    rightPressed = true;
                    leftPressed = false;
                    return false;
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    rightPressed = false;
                }
            });

        addImage(front);
        addImage(back);
        addImage(left);
        addImage(right);
    }

    private void addImage(Image i) {
        add(i).size(32).pad(3);
    }


}
