package Andromeda.Collision;

import Andromeda.Utils.Vector2;

/**
 * Classname: ContactPoint
 * Author: Elias Jätte
 * Date: 18/08/06
 *
 * Information container regarding a contact point.
 */
public class ContactPoint {
    private Vector2 point;
    private double depth;

    public ContactPoint(Vector2 point, double depth) {
        this.point = point;
        this.depth = depth;
    }

    public Vector2 getPoint() {
        return point;
    }

    public double getDepth() {
        return depth;
    }

    public Vector2 getAdjusted(Vector2 n){
        return point.add(n.multiply(depth));
    }

    public String toString(){
        return "point: "+point.toString()+" depth: "+depth;
    }
}
