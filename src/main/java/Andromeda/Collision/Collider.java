package Andromeda.Collision;

import Andromeda.GameObject.Component;
import Andromeda.GameObject.GameObject;
import Andromeda.Utils.Vector2;

import java.awt.*;
import java.util.HashSet;

/**
 * Classname: Collider
 * Author: Elias Jätte
 * Date: 18/08/12
 *
 * Abstract class of collider object.
 */
public abstract class Collider extends Component{
    public boolean isTrigger = true;

    //Contains colliders which this collider is currently colliding with.
    //This is bad design and doesn't scale well.
    //Should let a manager handle who is colliding with who.
    private HashSet<Collider> collidings;

    private Color defaultColor = new Color(0, 222, 44);
    private Color color = defaultColor;

    public Collider (GameObject gameObject){
        super(gameObject);
        collidings = new HashSet<>(5);
    }

    /**
     * Check whehter a collider is colliding with this collider.
     * This is only from a game engine perspective, "is it colliding this frame?".
     * It doesn't actually do any calculations.
     *
     * @param collider  Collider
     *
     * @return  bool
     */
    public boolean isColliding(Collider collider){
        return collidings.contains(collider);
    }

    public void addColliding(Collider collider){
        collidings.add(collider);
    }

    public void removeColliding(Collider collider){
        collidings.remove(collider);
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
        this.color = defaultColor;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Get center of collider, relative to world.
     * @return Vector2
     */
    public abstract Vector2 getWorldCentroid();

    /**
     * Get center of collider, relative to itself.
     * @return Vector2
     */
    public abstract Vector2 getCentroid();

    /**
     * Get area of collider.
     * @return double
     */
    public abstract double getArea();

    /**
     * Get projection of collider on to a vector.
     *
     * @param p Vector to project onto.
     *
     * @return Projection
     */
    public abstract Projection getProjection(Vector2 p);
}
