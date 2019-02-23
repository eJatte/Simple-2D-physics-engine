package Andromeda.Render;

import Andromeda.Collision.Collision;
import Andromeda.Collision.CollisionClip;
import Andromeda.Collision.Transform;
import Andromeda.Utils.Matrix_2x2;
import Andromeda.Utils.Vector2;

import java.awt.*;
import java.util.ArrayList;

/**
 * Classname: Drawer
 * Author: Elias Jätte
 * Date: 18/08/21
 */
public class Drawer implements Drawable{
    private ArrayList<Collision> collisions;

    public Drawer(ArrayList<Collision> collisions) {
        this.collisions = collisions;
    }

    @Override
    public void draw(Graphics g, Transform transform) {
        for (Collision collision: collisions) {
            if (collision.isCollision()) {
                for (Vector2 point : collision.getContactManifold()) {
                    g.setColor(new Color(255, 222,0));
                    point = point.multiply(transform);
                    g.fillOval((int) point.getX() - 5, (int) point.getY() - 5, 10, 10);
                }
            }
        }
    }

    @Override
    public int getLayer() {
        return 10;
    }
}
