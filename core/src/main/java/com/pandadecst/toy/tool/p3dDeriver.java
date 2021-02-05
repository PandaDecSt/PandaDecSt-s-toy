package com.pandadecst.toy.tool;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.JsonWriter;
import java.io.Writer;
import java.io.IOException;

public class p3dDeriver {
    
    public static void deriveModel(Model m, String path) throws IOException{
        FileHandle newFile = new FileHandle(path);
        Writer w = newFile.writer(false);
        JsonWriter writer = new JsonWriter(w);
        writer.array("meshes");
        //wait done
    }
    
}
