package Andromeda.Manager;

import Andromeda.Collision.*;
import Andromeda.Collision.LineCollision.Edge;
import Andromeda.Collision.LineCollision.EdgeCollision;
import Andromeda.Collision.Raycast.Raycast;
import Andromeda.Collision.Raycast.RaycastHit;
import Andromeda.Utils.Vector2;

import java.util.ArrayList;

/**
 * Classname: CollisionManager
 * Author: Elias Jätte
 * Date: 18/08/12
 *
 * Manager for collisions.
 */
public class CollisionManager{
    private ArrayList<PolygonCollider> polygonColliders;
    private ArrayList<PolygonCollider> triggerPolyColliders;

    public CollisionManager(){
        polygonColliders = new ArrayList<>();
        triggerPolyColliders = new ArrayList<>();
    }


    /**
     * Perform a raycast on the current colliders.
     *
     * @param point     origin
     * @param direction direction
     * @param distance  distance
     *
     * @return  Raycast
     */
    public Raycast raycast(Vector2 point, Vector2 direction, double distance){
        Edge ray = new Edge(point, point.add(direction.multiply(distance)));
        ArrayList<RaycastHit> hits = new ArrayList<>();
        for (PolygonCollider p:polygonColliders) {
            Edge[] edges = p.getEdges();
            for (Edge e:edges) {
                EdgeCollision edgeCollision = EdgeCollision.intersect(ray,e);
                if(edgeCollision.intersect()){
                    if(!p.getTransform().isFlipped()) {
                        hits.add(new RaycastHit(p, p.gameObject.rigidbody, edgeCollision.getIntersect(), e
                                .getLeftNormal(), edgeCollision.getIntersect().difference(point).magnitude()));
                    } else {
                        hits.add(new RaycastHit(p, p.gameObject.rigidbody, edgeCollision.getIntersect(), e
                                .getRightNormal(), edgeCollision.getIntersect().difference(point).magnitude()));
                    }
                }
            }
        }
        return new Raycast(point,direction,distance,hits);
    }

    public void addPolygonCollider(PolygonCollider polygonCollider) {
        if(polygonCollider.isTrigger){
            triggerPolyColliders.add(polygonCollider);
        }
        else {
            polygonColliders.add(polygonCollider);
        }
    }

    public void removePolygonCollider(PolygonCollider polygonCollider){
        if(polygonCollider.isTrigger){
            triggerPolyColliders.remove(polygonCollider);
        }
        else {
            polygonColliders.remove(polygonCollider);
        }
    }

    /**
     * Check trigger collisions
     */
    public void checkTriggers(){
        for(PolygonCollider p1: new ArrayList<>(polygonColliders)) {
            if(!p1.isEnabled)
                continue;
            for (PolygonCollider trigger : new ArrayList<>(triggerPolyColliders)) {
                CollisionClip c = CollisionClip.poly2Collision(p1, trigger);
                boolean isColliding = trigger.isColliding(p1);
                if (c.isCollision()) {
                    if (isColliding) {
                        trigger.gameObject.onTriggerStay(p1, c.getSatResult());
                    } else {
                        trigger.addColliding(p1);
                        trigger.gameObject.onTriggerEnter(p1, c.getSatResult());
                    }
                } else if(isColliding){
                    trigger.removeColliding(p1);
                    trigger.gameObject.onTriggerLeave(p1, c.getSatResult());
                }
            }
        }
    }

    /**
     * Call collision methods in game objects.
     *
     * @param collisions .
     */
    public void callCollision(ArrayList<Collision> collisions){
        for (Collision c:collisions) {
            Collider c1 = c.getC1();
            Collider c2 = c.getC2();
            boolean isColliding = c1.isColliding(c2);
            if (c.isCollision()) {
                if (isColliding) {
                    c1.gameObject.onCollisionStay(c);
                    c2.gameObject.onCollisionStay(c.getInverse());
                } else {
                    c1.addColliding(c2);
                    c2.addColliding(c1);
                    c1.gameObject.onCollisionEnter(c);
                    c2.gameObject.onCollisionEnter(c.getInverse());
                    c1.gameObject.onCollisionStay(c);
                    c2.gameObject.onCollisionStay(c.getInverse());
                }
            } else if(isColliding){
                c1.removeColliding(c2);
                c2.removeColliding(c1);

                c1.gameObject.onCollisionLeave(c);
                c2.gameObject.onCollisionLeave(c.getInverse());
            }
        }
    }

    /**
     * Get all collisions between all colliders.
     *
     * @return Collision information.
     */
    public ArrayList<CollisionClip> getCollisionClips(){
        ArrayList<CollisionClip> collisionClips = new ArrayList<>();
        for(int i = 0; i < polygonColliders.size(); i++){
            PolygonCollider p1 = polygonColliders.get(i);
            if(!p1.isEnabled)
                continue;
            for(int j = i+1; j < polygonColliders.size(); j++){
                PolygonCollider p2 = polygonColliders.get(j);
                if(!p2.isEnabled)
                    continue;
                CollisionClip c = CollisionClip.poly2Collision(p1, p2);
                boolean isColliding = p1.isColliding(p2);
                if(c.isCollision() || isColliding) {
                    collisionClips.add(c);
                }
            }
        }
        return collisionClips;
    }
}
