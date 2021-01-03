package com.pandadecst.toy.tool;

import com.badlogic.gdx.Gdx;

public class Logger {
    
    public static void log(String tag, String str){
        Gdx.app.log(tag, str);
    }
    
}
