package com.pandadecst.toy.tool;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.Vector3;
import java.util.List;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.GL20;
import java.util.Vector;

/**
 * Created by PandaDecSt on 21.02.04.
 */
public class MeshHelper {

    public static float[] getVertices(Mesh m) {
        if (m != null) {
            int size = m.getVerticesBuffer().limit();
            float verties[] = new float[size];
            for (int i = 0; i < size; i++) {

                verties[i] = m.getVerticesBuffer().get(i);

            }
            return verties;
        } else {
            return null;
        }
    }

    public static float[] getVertices(List<Vector3> t) {//Maybe have some problem.
        if (t != null) {
            float verties[] = new float[t.size() * 3];
            int a = 0;
            for (int i = 0; i < t.size(); i++) {
                verties[a++] = t.get(i).x;
                verties[a++] = t.get(i).y;
                verties[a++] = t.get(i).z;
            }
            return verties;
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
            float verties[] = getVertices(cutT);
            return verties;
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
            for (int i = 0; i < v.length - 3; i += 3) {
                ts.add(new Vector3(v[i], v[i + 1], v[i + 2]));
            }
            return ts;
        } else {
            return null;
        }
    }

    public static short[] getIndices(Mesh m) {
        if (m != null) {
            int size = m.getIndicesBuffer().limit();
            short indices[] = new short[size];
            for (int i = 0; i < size; i++) {

                indices[i] = m.getIndicesBuffer().get(i);

            }
            return indices;
        } else {
            return null;
        }
    }

    public static VertexAttributes getVertexAttributes(Mesh m) {
        if (m != null) {
            return m.getVertexAttributes() ;
        } else {
            return null;
        }
    }

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

    public static Mesh createMesh(float[] verties, VertexAttributes attributes) {
        return createMesh(verties, checkIndices(verties), attributes);
    }

    public static Mesh createMesh(float[] verties, short[] indices, VertexAttributes attributes) {
        Mesh mesh = new Mesh(true, verties.length, indices.length, attributes);
        mesh.setVertices(verties);
        mesh.setIndices(indices);
        mesh.setAutoBind(false);
        return mesh;
    }
    
    public static Mesh createMesh(float[] verties, short[] indices, VertexAttribute... attributes) {
        Mesh mesh = new Mesh(true, verties.length, indices.length, attributes);
        mesh.setVertices(verties);
        mesh.setIndices(indices);
        mesh.setAutoBind(false);
        return mesh;
    }
    
    public static Mesh createMesh(float[] verties, short[] indices) {
        return createMesh(verties, indices, VertexAttribute.Position(), VertexAttribute.Binormal(), VertexAttribute.Normal(), VertexAttribute.Tangent());
    }

    public static Mesh createMesh(List<Vector3> Positions, short[] indices, VertexAttributes attributes) {//Maybe have some problem.
        float v[] = getVertices(Positions);
        Mesh mesh = new Mesh(true, v.length, indices.length, attributes);
        mesh.setVertices(v);
        mesh.setIndices(indices);
        mesh.setAutoBind(false);
        return mesh;
    }

    public static Mesh buildCube(float SIZE) {
        Mesh mesh = new Mesh(true, 8, 14, new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"));

        float[] cubeVerts = { -SIZE, -SIZE, SIZE, SIZE, -SIZE, SIZE, -SIZE,
            SIZE, SIZE, SIZE, SIZE, SIZE, -SIZE, -SIZE, -SIZE, SIZE, -SIZE,
            -SIZE, -SIZE, SIZE, -SIZE, SIZE, SIZE, -SIZE, };

        short[] indices = { 0, 1, 2, 3, 7, 1, 5, 4, 7, 6, 2, 4, 0, 1 };

        mesh.setVertices(cubeVerts);
        mesh.setIndices(indices);
        return mesh;
    }

}
