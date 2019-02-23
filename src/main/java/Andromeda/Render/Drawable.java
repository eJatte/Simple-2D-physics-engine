/*
 * Classname: Drawable.java
 * Author: Elias Jätte
 * Date: 2017-12-10
 */

package Andromeda.Render;

import Andromeda.Collision.Transform;

import java.awt.*;

/**
 * Interface for objects that are drawable. Drawable to a Graphics component.
 * @author Elias Jätte, c16eje
 */
public interface Drawable  {
    /**
     * Draws to a graphics component.
     *
     * @param g                     The graphics component to draw to.
     *
     * @param transform             Multiply every vector and matrix or transform that is in world space with this
     *                              transform.
     */
    void draw(Graphics g, Transform transform);

    /**
     * The layer of the drawable. 0 is the furthest back, 1 is gonna be placed on top of drawables with 0 etc.
     *
     * @return      The layer number >= 0
     */
    int getLayer();
}
