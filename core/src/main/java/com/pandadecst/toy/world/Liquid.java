package com.pandadecst.toy.world;

import java.util.List;
import com.badlogic.gdx.math.Vector3;

public class Liquid {
    
    // 三角面
    public class Triangle
    {
        public int X;
        public int Y;
        public int Z;

        public Triangle(int x, int y, int z)
        {
            X = x;
            Y = y;
            Z = z;
        }

        public void setZ(int z) {
            Z = z;
        }

        public int getZ() {
            return Z;
        }

        public void setY(int y) {
            Y = y;
        }

        public int getY() {
            return Y;
        }

        public void setX(int x) {
            X = x;
        }

        public int getX() {
            return X;
        }
    }
}
