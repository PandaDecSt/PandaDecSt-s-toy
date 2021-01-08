package com.pandadecst.toy.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;
import java.io.File;

public class ModelLoader {

    static G3dModelLoader g3dbModelLoader = new G3dModelLoader(new UBJsonReader());
    static G3dModelLoader g3djModelLoader = new G3dModelLoader(new JsonReader());
    static ObjLoader objLoader = new ObjLoader();
    static ModelBuilder modelBuilder = new ModelBuilder();
    static Material m = new Material(ColorAttribute.createDiffuse(Color.GREEN));

    public static Model loadModel(File f) {
        if (f == null) {
            return modelBuilder.createBox(5f, 5f, 5f, m,
                                          VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        } else {
            String absolutePath = f.getAbsolutePath();

            if (absolutePath.toLowerCase().endsWith("obj")) {
                return objLoader.loadModel(Gdx.files.absolute(absolutePath));
            } else if (absolutePath.toLowerCase().endsWith("g3dj")) {
                return g3djModelLoader.loadModel(Gdx.files.absolute(absolutePath));
            } else {
                return g3dbModelLoader.loadModel(Gdx.files.absolute(absolutePath));
            }
        }
	}

    public static Model loadModel(FileHandle f) {
        if (!f.exists()) {
            return modelBuilder.createBox(5f, 5f, 5f, m,
                                          VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        } else {
            if (f.name().toLowerCase().endsWith("obj")) {
                return objLoader.loadModel(f);
            } else if (f.name().toLowerCase().endsWith("g3dj")) {
                return g3djModelLoader.loadModel(f);
            } else {
                return g3dbModelLoader.loadModel(f);
            }
        }
    }
    
//    public static Model Json2Model(Model nullm, String str){
//        return (Model)FileOperator.load(nullm, str);
//    }
    
//    public static Model Json2Model(Model nullm, FileHandle f){
//        if (f.exists()) {
//        return Json2Model(nullm, f.readString());
//        }
//        return null;
//    }
    
    

}
