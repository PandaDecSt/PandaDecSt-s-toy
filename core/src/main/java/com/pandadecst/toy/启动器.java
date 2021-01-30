package com.pandadecst.toy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.VisCHLoader;
import com.pandadecst.toy.test.*;

public class 启动器 extends Game {

    Skin skin;
    
    @Override
    public void create() {
        skin = VisCHLoader.load(); 
        setScreen(new CollisionWorldTest());
    }
}
