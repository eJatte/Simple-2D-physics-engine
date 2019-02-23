package Andromeda.Collision.Raycast;

import Andromeda.Collision.Collider;
import Andromeda.Manager.GameManager;
import Andromeda.Utils.Vector2;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Classname: Raycast
 * Author: Elias Jätte c16eje Umeå Universitet
 * Date: 18/08/28
 *
 * Class for doing ray cast collisions.
 */
public class Raycast {
    private Vector2 point;
    private Vector2 direction;
    private double distance;
    private ArrayList<RaycastHit> hits;

    public Raycast(Vector2 point, Vector2 direction, double distance, ArrayList<RaycastHit> hits) {
        this.point = point;
        this.direction = direction;
        this.distance = distance;
        this.hits = hits;
    }

    public RaycastHit getClosestHit(){
        if(hits.size() == 0)
            return null;
        RaycastHit minHit = hits.get(0);
        double min = minHit.getDistance();
        for (RaycastHit hit:hits) {
            if(hit.getDistance() < min){
                minHit = hit;
                min = hit.getDistance();
            }
        }
        return minHit;
    }

    /**
     * Get colliders which the raycast hit.
     *
     * @return
     */
    public ArrayList<Collider> getColliders(){
        HashSet<Collider> colliders = new HashSet<>(hits.size()/2+1);
        for (RaycastHit hit:hits) {
            colliders.add(hit.getCollider());
        }
        return new ArrayList<>(colliders);
    }

    /**
     * Point of origin.
     *
     * @return Vector2
     */
    public Vector2 getPoint() {
        return point;
    }

    /**
     * Direction of cast
     *
     * @return Vector2
     */
    public Vector2 getDirection() {
        return direction;
    }

    /**
     * Cast distance.
     *
     * @return double
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Get hit information for each collision.
     *
     * @return ArrayList<RaycastHit>
     */
    public ArrayList<RaycastHit> getHits() {
        return hits;
    }

    public static Raycast raycast(Vector2 point, Vector2 direction, double distance){
        return GameManager.getInstance().getScene().getCollisionManager().raycast(point,direction,distance);
    }
}
