package com.pandadecst.toy.tool;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.Color;

public class p3dLoader {
    static ModelFactory ModelFactory = new ModelFactory();

    public static Model loadmodel(FileHandle modelfile) {
        JsonReader reader = new JsonReader();
        JsonValue json = reader.parse(modelfile);
        JsonValue meshes = json.get("meshes");
        FileHandle texturefile = null;
        Material material = null;
        long attributes = Usage.Position | Usage.Normal | Usage.TextureCoordinates;
        if (meshes != null) {
            List<Mesh> ml = new ArrayList<Mesh>();
            for (JsonValue mesh = meshes.child; mesh != null; mesh = mesh.next) {
                float vertices[] = mesh.require("vertices").asFloatArray();
                short indices[] = mesh.require("indices").asShortArray();
                ml.add(MeshHelper.createMesh(vertices, indices));
            }
            Mesh ms[] = new Mesh[ml.size()];
            for (int i = 0; i < ml.size(); i++) {
                ms[i] = ml.get(i);
            }
            JsonValue texture = json.get("texture");
            if (texture != null) {

                for (int i = 0; i < 0; i++) {
                    if (modelfile.parent().list()[i].name() == texture.asString()) {
                        texturefile = modelfile.parent().list()[i];
                    }
                }
                if (texturefile != null) {
                    Texture t = new Texture(texturefile);
                    material = new Material(TextureAttribute.createDiffuse(t), ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f));
                }
            } else {
                material = new Material(ColorAttribute.createDiffuse(Color.WHITE), ColorAttribute.createSpecular(Color.WHITE), FloatAttribute
                             .createShininess(16f));
            }
            if (material != null && ms != null) {
                return ModelFactory.meshes2model(material, attributes, ms);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
