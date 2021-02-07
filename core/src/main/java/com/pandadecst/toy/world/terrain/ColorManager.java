package com.pandadecst.toy.world.terrain;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mariusz on 14.12.15.
 */
public class ColorManager {
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
