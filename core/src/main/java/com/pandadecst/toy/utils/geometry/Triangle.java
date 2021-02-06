package com.pandadecst.toy.utils.geometry;

import com.badlogic.gdx.math.Vector3;

public class Triangle {
    Vector3 v0, v1, v2;

    public Triangle(Vector3 v0, Vector3 v1, Vector3 v2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
    }

    public void setV0(Vector3 v0) {
        this.v0 = v0;
    }

    public Vector3 getV0() {
        return v0;
    }

    public void setV1(Vector3 v1) {
        this.v1 = v1;
    }

    public Vector3 getV1() {
        return v1;
    }

    public void setV2(Vector3 v2) {
        this.v2 = v2;
    }

    public Vector3 getV2() {
        return v2;
    }

}
