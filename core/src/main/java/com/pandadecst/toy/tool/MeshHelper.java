package com.pandadecst.toy.tool;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.Vector3;

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
    
    public static float[] getVertices(Vector3[] t) {//Maybe have some problem.
        if (t != null) {
            float verties[] = new float[t.length*3];
            int a = -1;
            for (int i = 0; i < t.length; i++) {
                verties[a++] = t[i].x;
                verties[a++] = t[i].y;
                verties[a++] = t[i].z;
            }
            return verties;
        } else {
            return null;
        }
    }
    
    public static Vector3[] getTriangles(Mesh m) {//Maybe have some problem.
        if (m != null) {
            float v[] = getVertices(m);
            return getTriangles(v);
        } else {
            return null;
        }
    }
    
    public static Vector3[] getTriangles(float[] v) {//Maybe have some problem.
        if (v != null) {
            Vector3 t[] = new Vector3[v.length];
            int a = -1;
            for (int i = 0; i < v.length; i+=3) {
                t[a++] = new Vector3(v[i],v[i+1],v[i+2]);
            }
            return t;
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
    
    public static Mesh createMesh(float[] verties, short[] indices, VertexAttributes attributes) {
        Mesh mesh = new Mesh(true, verties.length, indices.length, attributes);
        mesh.setVertices(verties);
        mesh.setIndices(indices);
        mesh.setAutoBind(false);
        return mesh;
    }
    
    public static Mesh createMesh(Vector3[] triangles, short[] indices, VertexAttributes attributes) {//Maybe have some problem.
        float v[] = getVertices(triangles);
        Mesh mesh = new Mesh(true, v.length, indices.length, attributes);
        mesh.setVertices(v);
        mesh.setIndices(indices);
        mesh.setAutoBind(false);
        return mesh;
    }

}
