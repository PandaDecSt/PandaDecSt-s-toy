package com.pandadecst.toy.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.math.Vector3;
import com.pandadecst.toy.tool.MeshHelper;
import com.pandadecst.toy.utils.geometry.Triangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by PandaDecSt on 21.02.04.
 */
public class MeshHelper {

    static boolean debug = true;

    public static float[] getVertices(Mesh m) {
        if (m != null) {
            float v[] = FloatBuffer2Array(m.getVerticesBuffer());
            if (debug) {
                log("vertices.length = " + v.length);
                for (int i = 0; i < v.length; i += 3) {
                    log(v[i] + "," + v[i + 1] + "," + v[i + 2]);
                }
            }
            return v;
        } else {
            return null;
        }
    }

    public static float[] getVertices(List<Vector3> t) {//Maybe have some problem.
        if (t != null) {
            float vertices[] = new float[t.size() * 3];
            int a = 0;
            for (int i = 0; i < t.size(); i++) {
                vertices[a++] = t.get(i).x;
                vertices[a++] = t.get(i).y;
                vertices[a++] = t.get(i).z;
            }
            return vertices;
        } else {
            return null;
        }
    }
    
    public static short[] getIndices(Mesh m) {
        if (m != null) {
            short s[] = ShortBuffer2Array(m.getIndicesBuffer());
            if (debug) {
                log("indices.length = " + s.length);
                log("numTriangles = " + s.length / 3);
                for (int i = 0; i < s.length; i += 3) {
                    log(s[i] + "," + s[i + 1] + "," + s[i + 2]);
                }
            }
            return s;
        } else {
            return null;
        }
    }

    public static float[] cutVertices(float[] v, Vector3 cutpoint) {//Maybe have some problem.
        if (v != null) {
            List<Vector3> t = getPositions(v);
            return cutVertices(t, cutpoint);
        } else {
            return null;
        }
    }

    public static float[] cutVertices(List<Vector3> t, Vector3 cutpoint) {//Maybe have some problem.
        if (t != null) {
            List<Vector3> cutT = new ArrayList<Vector3>();
            for (int i = 0; i < t.size(); i++) {
                if (t.get(i).z > cutpoint.z)
                    cutT.add(t.get(i));
            }
            float vertices[] = getVertices(cutT);
            return vertices;
        } else {
            return null;
        }
    }

    public static List<Vector3> getPositions(Mesh m) {//Maybe have some problem.
        if (m != null) {
            float v[] = getVertices(m);
            return getPositions(v);
        } else {
            return null;
        }
    }

    public static List<Vector3> getPositions(float[] v) {//Maybe have some problem.
        if (v != null) {
            List<Vector3> ts = new ArrayList<Vector3>();
            for (int i = 0; i < v.length; i += 3) {
                ts.add(new Vector3(v[i], v[i + 1], v[i + 2]));
                if (debug) {
                    log("" + v[i] + v[i + 1] + v[i + 2]);
                }
            }
            if (debug) {
                log("numPositions = " + ts.size());
            }
            return ts;
        } else {
            return null;
        }
    }

    public static List<Triangle> getTriangles(Mesh m) {
        short indices[] = getIndices(m);
        List<Vector3> pos = tidyPositions(getPositions(m));
        List<Triangle> triangles = new ArrayList<Triangle>();
        for (int i = 0; i < indices.length; i += 3) {
            triangles.add(new Triangle(pos.get(indices[i]), pos.get(indices[i + 1]), pos.get(indices[i + 2])));
        }
        if (debug) {
            log("triangleSize = " + triangles.size());
            for (int i = 0; i < triangles.size(); i++) {
                log(triangles.get(i).toString());
            }
        }
        return triangles;
    }

    public static VertexAttributes getVertexAttributes(Mesh m) {
        if (m != null) {
            return m.getVertexAttributes() ;
        } else {
            return null;
        }
    }

    //清理重复顶点
    public static List<Vector3> tidyPositions(List<Vector3> positions) {
        List<Vector3> a = new ArrayList<Vector3>();
        for (int i = 0; i < positions.size(); i++) {
            if (!a.contains(positions.get(i))) {
                a.add(positions.get(i));
            }
        }
        if (debug) log("不重复顶点个数 = " + a.size());
        return a;
    }

    /*
     **1.删除顶点集重复顶点
     **2.获取被删除顶点索引
     **3.通过这两个数组计算准确的indices
     */
    public static short[] checkIndices(List<Vector3> p) {
        if (p != null) {
            List<Vector> sl = new ArrayList<Vector>();
            for (int i = 0; i < 0; i++) {
                for (int j = 0; j < 0; j++) {
                    if (p.get(i) == p.get(j)) {
                        sl.add(new Vector(i));
                        sl.add(new Vector(j));
                    }
                }
            }
            short s[] = new short[sl.size()];
            for (int i = 0; i < sl.size(); i++) {
                s[i] = sl.get(i).get(0);
            }
            return s;
        } else {
            return null;
        }
    }

    public static short[] checkIndices(float[] v) {
        if (v != null) {
            List<Vector3> p = getPositions(v);
            return checkIndices(p);
        } else {
            return null;
        }
    }

    public static Mesh createMesh(float[] vertices, VertexAttributes attributes) {
        return createMesh(vertices, checkIndices(vertices), attributes);
    }

    public static Mesh createMesh(float[] vertices) {
        return createMesh(vertices, new short[0]);
    }

    public static Mesh createMesh(float[] vertices, short[] indices, VertexAttributes attributes) {
        Mesh mesh = new Mesh(true, vertices.length, indices.length, attributes);
        mesh.setVertices(vertices);
        mesh.setIndices(indices);
        mesh.setAutoBind(false);
        return mesh;
    }

    public static Mesh createMesh(float[] vertices, short[] indices, VertexAttribute... attributes) {
        Mesh mesh = new Mesh(true, vertices.length, indices.length, attributes);
        mesh.setVertices(vertices);
        mesh.setIndices(indices);
        mesh.setAutoBind(false);
        return mesh;
    }

    public static Mesh createMesh(float[] vertices, short[] indices) {
        return createMesh(vertices, indices, new VertexAttribute(Usage.Position, indices.length, "position"));
    }

    public static Mesh createMesh(List<Vector3> Positions, short[] indices, VertexAttributes attributes) {//Maybe have some problem.
        float v[] = getVertices(Positions);
        Mesh mesh = new Mesh(true, v.length, indices.length, attributes);
        mesh.setVertices(v);
        mesh.setIndices(indices);
        mesh.setAutoBind(false);
        return mesh;
    }

    public static Mesh createMesh(List<Triangle> triangles, VertexAttribute... attributes) {
        List<Vector3> pos = new ArrayList<Vector3>();
        for (int i = 0; i < triangles.size(); i++) {
            pos.add(triangles.get(i).getV0());
            pos.add(triangles.get(i).getV1());
            pos.add(triangles.get(i).getV2());
        }
        float vertices[] = getVertices(pos);
        short indices[] = new short[pos.size()];
        for (int i = 0; i < pos.size(); i++) {
            indices[i] = (short)i;
        }
        return createMesh(vertices, indices, attributes);
    }

    public static Mesh createMesh(List<Triangle> triangles, VertexAttributes attributes) {
        List<Vector3> pos = new ArrayList<Vector3>();
        for (int i = 0; i < triangles.size(); i++) {
            pos.add(triangles.get(i).getV0());
            pos.add(triangles.get(i).getV1());
            pos.add(triangles.get(i).getV2());
        }
        float vertices[] = getVertices(pos);
        short indices[] = new short[pos.size()];
        for (int i = 0; i < pos.size(); i++) {
            indices[i] = (short)i;
        }
        return createMesh(vertices, indices, attributes);
    }

    public static Mesh buildCube(float SIZE) {

        float[] cubeVerts = { 
            -SIZE, -SIZE, SIZE,//0
            -SIZE, SIZE, SIZE,//1
            SIZE, SIZE, SIZE,//2
            SIZE, -SIZE, SIZE,//3

            SIZE, -SIZE, -SIZE,//4
            -SIZE, -SIZE, -SIZE,//5
            -SIZE, SIZE, -SIZE,//6
            SIZE, SIZE, -SIZE, };//7

        short[] indices = {
            0,1,2,
            2,3,0,
            4,5,6,
            6,7,4};//0, 1, 2, 3, 7, 1, 5, 4, 7, 6, 2, 4, 0, 1 };

        Mesh mesh = new Mesh(true, cubeVerts.length, indices.length, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));
        mesh.setVertices(cubeVerts);
        mesh.setIndices(indices);
        return mesh;
    }

    private static float[] FloatBuffer2Array(FloatBuffer b) {
        if (b != null) {
            int size = b.limit();
            float e[] = new float[size];
            for (int i = 0; i < size; i++) {
                e[i] = b.get(i);
            }
            return e;
        } else {
            return null;
        }
    }

    private static short[] ShortBuffer2Array(ShortBuffer b) {
        if (b != null) {
            int size = b.limit();
            short e[] = new short[size];
            for (int i = 0; i < size; i++) {
                e[i] = b.get(i);
            }
            return e;
        } else {
            return null;
        }
    }

    public static void log(String str) {
        Gdx.app.log("MeshHelper", str);
    }

}
