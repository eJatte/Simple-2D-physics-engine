/*
 * Classname: RenderPanel.java
 * Author: Elias Jätte
 * Date: 2017-12-10
 */

package Andromeda.View;

import Andromeda.Collision.Transform;
import Andromeda.Render.Camera;
import Andromeda.Render.Drawable;
import Andromeda.Render.RenderList;
import Andromeda.Utils.Matrix_2x2;
import Andromeda.Utils.Vector2;
import com.sun.org.apache.regexp.internal.RE;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A class for rendering drawables to a JPanel.
 * @author Elias Jätte, c16eje
 */
public class RenderPanel extends JPanel{
    private ArrayList<Drawable> drawables;
    private Camera camera;
    private Transform screenTransform;
    private Transform screenTransformInverse;

    /**
     * Renders all drawables. Size is based of off the panel size and sent to
     * the drawables.
     *
     * DO NOT CALL ON THIS METHOD YOURSELF USE draw() INSTEAD.
     *
     * @param g         Graphics component
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(camera != null) {
            calculateScreenTransform();
            if (drawables != null) {
                Toolkit.getDefaultToolkit().sync();
                for (Drawable d : drawables) {
                    d.draw(g, getScreenTransform());
                }
            }
        }
    }

    private void calculateScreenTransform(){
        double width = this.getWidth();
        double height = this.getHeight();
        double hwRatio = height/width;
        Transform st = new Transform(new Matrix_2x2(width,0,0,height),new Vector2(0,0), null);
        Transform ct = camera.getTransform(hwRatio);
        screenTransform = ct.multiply(st);
    }

    public Transform getScreenTransform(){
        return screenTransform;
    }

    public void setCamera(Camera camera){
        this.camera = camera;
    }

    /**
     * Draw drawables.
     *
     * @param drawables         Drawables which are to be drawn.
     */
    public void draw(ArrayList<Drawable> drawables){
        this.drawables = drawables;
        repaint();
    }
}
