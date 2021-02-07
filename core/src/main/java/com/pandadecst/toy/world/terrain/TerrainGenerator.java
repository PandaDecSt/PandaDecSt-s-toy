package com.pandadecst.toy.world.terrain;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class TerrainGenerator {
    int size;
    float[][] vertices;
    private float roughness;
    private Random rand;
    float min = Float.MAX_VALUE;
    float max = Float.MIN_VALUE;
    float water = 0;
    float waterLevel = 0.0f;

    public TerrainGenerator(int s, float roughness, int seed, float waterLevel) {
        changeSize(s);
        this.roughness = roughness;
        rand = new Random(seed);
        this.waterLevel = waterLevel;
    }

    // 设置center
    private float centerHeight(Vector2 v1, Vector2 v2, Vector2 v3, Vector2 v4, float r) {
        float sum = 0.0f;
        float a = 0f;
        Vector2[] vs = {v1, v2, v3, v4};
        for (Vector2 v : vs) {
            try {
                sum += vertices[(int) v.x][(int) v.y];
                a += 1;
            } catch (Exception e) {

            }
        }
        float bar = sum / a + r; //算术平均值+随机位置
        minMaxUpd(bar); //update min and max
        return bar;
    }

    private void minMaxUpd(float bar) { 
        if (bar > max)
            max = bar;
        if (bar < min)
            min = bar;
    }

    public void diamond(int x, int y, int size, float offset) {
        Vector2 v1 = new Vector2(x, y - size);
// up
        Vector2 v2 = new Vector2(x + size, y);
// right
        Vector2 v3 = new Vector2(x, y + size);
// down
        Vector2 v4 = new Vector2(x - size, y);
// left
        vertices[x][y] = centerHeight(v1, v2, v3, v4, offset); // Ustaw wartość środka
    }

    public void square(int x, int y, int size, float offset) {
        Vector2 v1 = new Vector2(x - size, y - size);
// left up
        Vector2 v2 = new Vector2(x + size, y - size);
// right up
        Vector2 v3 = new Vector2(x - size, y + size);
// left down
        Vector2 v4 = new Vector2(x + size, y + size);
// right down
        vertices[x][y] = centerHeight(v1, v2, v3, v4, offset); 
    }

    public void generate() { 
        float scale = roughness * size; //比例尺
//在开始正确算法之前设置顶点
        minMaxUpd(vertices[0][0]
                  = rand.nextFloat() * scale * 2 - scale);
        minMaxUpd(vertices[size][0]
                  = rand.nextFloat() * scale * 2 - scale);
        minMaxUpd(vertices[0][size]
                  = rand.nextFloat() * scale * 2 - scale);
        minMaxUpd(vertices[size][size] = rand.nextFloat() * scale * 2 - scale);
        diamondSquare(size); 
        water = min + ((max - min) * waterLevel); //水平面
    }

    public void diamondSquare(int size) { // Algorytmi Diamond Square
        int x, y, half = size / 2; 
        float scale = roughness * size; 
        if (half < 1) return; 

        for (y = half; y < this.size; y += size) { 
            for (x = half; x < this.size; x += size) {
                square(x, y, half, rand.nextFloat() * scale * 2 - scale); 
            }
        }

        for (y = 0; y <= this.size; y += half) { 
            for (x = (y + half) % size; x <= this.size; x += size) {
                diamond(x, y, half, rand.nextFloat() * scale * 2 - scale); 
            }
        }
        diamondSquare(size / 2); 
    }

    public void changeSize(int w) {
        size = w; 
        resetVertices(); 
    }

    private void resetVertices() { //DiamondSquare 仅支持 2^n 或 2^n+1 个正方形)
        vertices = new float[size + 1][size + 1]; 
    }
}
