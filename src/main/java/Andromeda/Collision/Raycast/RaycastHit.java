package Andromeda.Collision.Raycast;

import Andromeda.Collision.Collider;
import Andromeda.Physics.Rigidbody;
import Andromeda.Utils.Vector2;

/**
 * Classname: RaycastHit
 * Author: Elias Jätte c16eje Umeå Universitet
 * Date: 18/08/28
 *
 * Contains information regarding raycast collision.
 */
public class RaycastHit{
    private Collider collider;
    private Rigidbody rigidbody;
    private Vector2 point;
    private Vector2 normal;
    private double distance;

    public RaycastHit(Collider collider, Rigidbody rigidbody, Vector2 point, Vector2 normal, double distance) {
        this.collider = collider;
        this.rigidbody = rigidbody;
        this.point = point;
        this.normal = normal;
        this.distance = distance;
    }

    /**
     * Collider which with the ray interacted.
     *
     * @return Collider
     */
    public Collider getCollider() {
        return collider;
    }

    /**
     * Rigidbody which with the ray interacted
     * @return
     */
    public Rigidbody getRigidbody() {
        return rigidbody;
    }

    /**
     * Point of collision
     *
     * @return Vector2
     */
    public Vector2 getPoint() {
        return point;
    }

    public Vector2 getNormal() {
        return normal;
    }

    public double getDistance() {
        return distance;
    }

    public String toString(){
        return "point: "+point+" normal: "+normal+" distance: "+distance;
    }
}
