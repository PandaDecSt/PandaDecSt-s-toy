package com.kotcrab.vis.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kotcrab.vis.ui.util.font.LazyBitmapFont;

public class VisCHLoader {
    
    private static FreeTypeFontGenerator defaultFontGenerator;
    private static FreeTypeFontGenerator smallFontGenerator;

    private static LazyBitmapFont defaultFont;
    private static LazyBitmapFont smallFont;
    
    public static Skin load() {
        
            //generate default font
        defaultFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("msyhbd.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
            param.incremental = true;
            param.size = 16;
            defaultFont = new LazyBitmapFont(defaultFontGenerator, param);
            //generate small font
            smallFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("msyhbd.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter param2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
            param2.incremental = true;
            param2.size = 18;
            smallFont = new LazyBitmapFont(smallFontGenerator, param);
            //compose skin
            Skin skin = new Skin();
            skin.addRegions(new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
            skin.add("default-font", defaultFont, BitmapFont.class);
            skin.add("small-font", smallFont, BitmapFont.class);
            skin.load(Gdx.files.internal("uiskin.json"));
            VisUI.load(skin);       
            return skin;
    }
    
    public static void dispose () {      
                    defaultFontGenerator.dispose();
                    smallFontGenerator.dispose();
                    defaultFont.dispose();
                    smallFont.dispose();    
	}
    
}
