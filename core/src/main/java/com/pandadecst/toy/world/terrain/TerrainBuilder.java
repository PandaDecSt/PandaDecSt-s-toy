package com.pandadecst.toy.world.terrain;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import java.util.LinkedList;
import java.util.List;
import com.pandadecst.toy.tool.MeshHelper;

public class TerrainBuilder {
    public static final int VERTEX_INFO_SIZE = 6;
    public static final float UNIT = 1.0f;
    Color sand = Color.valueOf("ffff66");  
    Color seaside = Color.OLIVE; 
    Color wood = Color.valueOf("266A2E"); 
    Color hill = Color.DARK_GRAY; 
    Color summit = Color.LIGHT_GRAY; 
    Color desk = Color.valueOf("663311"); 
    Color darkDesk = new Color(Color.BLACK).lerp(desk, 0.5f); 
    ColorManager cm; 
    TerrainGenerator tg;
    Material baseColor = new Material(ColorAttribute.createDiffuse(Color.WHITE));
    int width, height;
    float[][] vertices;
    float water = 0;
    float waterLevel = 0.0f;
    float min, max;

    private void setTg(TerrainGenerator tg) {
        this.tg = tg;
        width = tg.size;
        height = tg.size;
        vertices = tg.vertices;
        water = tg.water;
        waterLevel = tg.waterLevel;
        min = tg.min;
        max = tg.max;
        cm = new ColorManager();
        if (waterLevel > 0) { //有无水影响沙子的生成
            cm.insert(sand, 0.0f);
            cm.insert(seaside, 0.05f);
        } else 
            cm.insert(seaside, 0.00f);
        cm.insert(wood, 0.2f);
        cm.insert(hill, 0.6f);
        cm.insert(summit, 1.0f);
    }

    public void buildMeshChunk(ModelBuilder builder, int p, int q, int id) {
        String name = "Terrain" + (id++);
        MeshPartBuilder meshbuild = builder.part(name, GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.ColorPacked, baseColor);
        for (int i = p; i < 64 + p && i < width; i++) {
            for (int j = q; j < 64 + q && j < height; j++) {
                Vector3 v1 = new Vector3(i * UNIT, vertices[i][j], j * UNIT);
                Vector3 v2 = new Vector3((i + 1) * UNIT, vertices[i + 1][j], j * UNIT);
                Vector3 v3 = new Vector3((i + 1) * UNIT, vertices[i + 1][j + 1], (j + 1) * UNIT);
                Vector3 v4 = new Vector3((i) * UNIT, vertices[i][j + 1], (j + 1) * UNIT);
                Vector3 n = MeshHelper.calculateNormal(v1, v2, v3); 

                MeshPartBuilder.VertexInfo a = new MeshPartBuilder.VertexInfo().setPos(v1).setNor(n).setCol(getColor(v1.y));
                MeshPartBuilder.VertexInfo b = new MeshPartBuilder.VertexInfo().setPos(v2).setNor(n).setCol(getColor(v2.y));
                MeshPartBuilder.VertexInfo c = new MeshPartBuilder.VertexInfo().setPos(v3).setNor(n).setCol(getColor(v3.y));
                MeshPartBuilder.VertexInfo d;
                meshbuild.triangle(b, a, c); 
                n = MeshHelper.calculateNormal(v1, v2, v4);
                a = new MeshPartBuilder.VertexInfo().setPos(v1).setNor(n).setCol(getColor(v1.y));
                c = new MeshPartBuilder.VertexInfo().setPos(v3).setNor(n).setCol(getColor(v3.y));
                d = new MeshPartBuilder.VertexInfo().setPos(v4).setNor(n).setCol(getColor(v4.y));
                meshbuild.triangle(a, d, c);
            }
        }
    }

    public Model build(TerrainGenerator tg) { 
        setTg(tg);
        ModelBuilder builder = new ModelBuilder();
        builder.begin(); 
        int id = 0;
        for (int p = 0; p < width; p += 64) { // 一个mesh的顶点不能超过2^15个
            for (int q = 0; q < height; q += 64) {
                buildMeshChunk(builder, p, q, id++); 
            }
        }
        return builder.end(); 
    }

    private Color getColor(float h) { 
        if (h < water && waterLevel > 0.0f) 
            return sand;
        h -= water; 
        float x = h / (max - water); 
        return cm.get(x); 
    }

    class ColorManager {
        class Col {
            Color c; 
            float p; 

            public Col(Color c, float p) {
                this.c = c;
                this.p = p;
            }
        }

        List<Col> cols = new LinkedList<Col>(); 

        public void insert(Color c, float p) { 
            int i;
            for (i = 0; i < cols.size() && p < cols.get(i).p; i++) ;
            cols.add(i, new Col(c, p));
        }

        public Color get(float p) {
            if (p >= 1.0f)
                return cols.get(0).c;
            int i;
            Col x = null, y = null;
            for (i = 1; i < cols.size() && p < cols.get(i).p; i++) ; 
            x = cols.get(i - 1); 
            y = cols.get(i); 
            p -= x.p; 
            return new Color(new Color(x.c).lerp(y.c, p / (y.p - x.p))); // Wartość interpolowana
        }
    }
}
