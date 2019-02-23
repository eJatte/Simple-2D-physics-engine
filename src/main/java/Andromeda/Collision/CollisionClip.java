package Andromeda.Collision;

import Andromeda.Collision.LineCollision.Edge;
import Andromeda.Utils.Vector2;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Classname: CollisionClip
 * Author: Elias Jätte
 * Date: 18/08/04
 *
 * Class for collisions between colliders.
 */
public class CollisionClip {
    private SATResult satResult;
    private boolean collision;
    private boolean flip;
    private ArrayList<ContactPoint> contactPointManifold;
    private Collider c1, c2;
    private Edge e1, e2;

    public CollisionClip(SATResult satResult, boolean collision, boolean flip, ArrayList<ContactPoint> contactPointManifold, Collider c1, Collider c2, Edge e1, Edge e2) {
        this.satResult = satResult;
        this.collision = collision;
        this.flip = flip;
        this.contactPointManifold = contactPointManifold;
        this.c1 = c1;
        this.c2 = c2;
        this.e1 = e1;
        this.e2 = e2;
    }

    public CollisionClip getInverse(){
        return new CollisionClip(satResult.getInverse(), collision, !flip, contactPointManifold,c2,c1,e2,e1);
    }

    public Vector2 getMTV() {
        return satResult.getMTV();
    }

    public Vector2 getSeperationNormal(){
        return satResult.getNormal();
    }

    public double getDepth(){
        return satResult.getDepth();
    }

    public boolean isCollision() {
        return collision;
    }

    public boolean isFlip() {
        return flip;
    }

    public SATResult getSatResult() {
        return satResult;
    }

    public ArrayList<ContactPoint> getContactPointManifold(){
        return contactPointManifold;
    }

    /**
     * Get list containing all contact points of collision. Not adjusted for overlap.
     *
     * @return  collision manifold
     */
    public ArrayList<Vector2> getCollisionManifold(){
        ArrayList<Vector2> adjusted = new ArrayList<>(contactPointManifold.size());
        if(contactPointManifold.size() < 1)
            return adjusted;
        ContactPoint deepest = contactPointManifold.get(0);
        for (ContactPoint contactPoint : contactPointManifold) {
            if (contactPoint.getDepth() > deepest.getDepth()) {
                deepest = contactPoint;
            }
        }
        for (ContactPoint contactPoint : contactPointManifold) {
            //0.0000001
            if (contactPoint.getDepth() >= deepest.getDepth() - 0.01) {
                Vector2 point;
                if (this.isFlip()) {
                    point = contactPoint.getAdjusted(this.getSeperationNormal().inverse());
                } else {
                    point = contactPoint.getPoint();
                }
                adjusted.add(point);
            }
        }
        return adjusted;
    }

    /**
     * Get list containing all contact points of collision. Adjusted according to m along the MTV.
     *
     * @param m 0-1, percentage of MTW to be offset.
     *
     * @return  Adjusted collision manifold
     */
    public ArrayList<Vector2> getCollisionManifold(double m){
        ArrayList<Vector2> collisionManifold = new ArrayList<>(contactPointManifold.size());

        for (Vector2 point : getCollisionManifold()) {
            collisionManifold.add(point.add(this.getMTV().inverse().multiply(1 - m)));
        }
        return collisionManifold;
    }

    /**
     * Get list containing all contact points of collision. Adjusted according to m along the MTV.
     * Also translated according to v
     *
     * @param m 0-1, percentage of MTW to be offset.
     * @param v translation vector.
     *
     * @return  Adjusted collision manifold
     */
    public ArrayList<Vector2> getCollisionManifold(double m, Vector2 v){
        ArrayList<Vector2> collisionManifold = new ArrayList<>(contactPointManifold.size());

        for (Vector2 point : getCollisionManifold()) {
            collisionManifold.add(point.add(this.getMTV().inverse().multiply(1 - m)).add(v));
        }
        return collisionManifold;
    }

    public Edge getE1() {
        return e1;
    }

    public Edge getE2() {
        return e2;
    }

    public Collider getC1() {
        return c1;
    }

    public Collider getC2() {
        return c2;
    }

    /**
     * Utilizes SAT to calculate clipping points between colliders.
     *
     * @param p1    collider
     * @param p2    collider
     *
     * @return Information regarding collision and contact points.
     */
    public static CollisionClip poly2Collision(PolygonCollider p1, PolygonCollider p2){
        ArrayList<ContactPoint> contactPointManifold = new ArrayList<>();

        SATResult satResult = CollisionClip.poly2SAT(p1,p2);

        if(satResult.isCollision()){
            if(p1.getPolygon().getVertices().length == 1){
                contactPointManifold.add(new ContactPoint(p1.getPolygon().getVertices()[0].multiply(p1.getTransform()), satResult
                        .getDepth()));
                return new CollisionClip(satResult, true,false, contactPointManifold, p1,
                        p2,null,null);
            }
            else if(p2.getPolygon().getVertices().length == 1){
                contactPointManifold.add(new ContactPoint(p2.getPolygon().getVertices()[0].multiply(p2.getTransform()), satResult
                        .getDepth()));
                return new CollisionClip(satResult, true,false, contactPointManifold, p1,
                        p2,null,null);
            }


            Vector2 n = satResult.getNormal();
            Edge e1 = CollisionClip.bestEdge(p1,n);
            Edge e2 = CollisionClip.bestEdge(p2,n.inverse());


            //Which collider are we relative to
            boolean flip = false;

            Edge reference, incident;
            PolygonCollider refCollider;
            if(Math.abs(e1.getR().normalize().dotProduct(n)) <= Math.abs(e2.getR().normalize().dotProduct(n))){
                reference = e1;
                incident = e2;
                refCollider = p1;
            }
            else{
                reference = e2;
                incident = e1;
                refCollider = p2;
                flip = true;
            }
            //Reference and incident edge found
            //Clipping is left

            Vector2 refEdge = reference.getR().normalize();

            double o1 = reference.getP1().dotProduct(refEdge);
            Vector2 clipPoints[] = CollisionClip.clip(incident.getP1(),incident.getP2(),refEdge,o1);

            if(clipPoints[0] == null || clipPoints[1] == null){
                return new CollisionClip(satResult, true,flip, contactPointManifold, p1, p2,e1,e2);
            }

            double o2 = reference.getP2().dotProduct(refEdge);
            clipPoints = CollisionClip.clip(clipPoints[0],clipPoints[1],refEdge.inverse(),-o2);

            if(clipPoints[0] == null || clipPoints[1] == null){
                return new CollisionClip(satResult, true,flip, contactPointManifold, p1,p2,e1,e2);
            }

            //Last clipping on reference edge normal
            Vector2 refNormal = reference.getRightNormal();
            if(refCollider.getTransform().isFlipped()){
                refNormal = reference.getLeftNormal();
            }

            double o3 = reference.getP1().dotProduct(refNormal);
            double d0 = clipPoints[0].dotProduct(refNormal)-o3;
            double d1 = clipPoints[1].dotProduct(refNormal)-o3;

            if(d0 >= 0){
                contactPointManifold.add(new ContactPoint(clipPoints[0],d0));
            }
            if(d1 >= 0){
                contactPointManifold.add(new ContactPoint(clipPoints[1],d1));
            }
            return new CollisionClip(satResult, satResult.isCollision(),flip, contactPointManifold, p1,p2,e1,e2);
        }

        return new CollisionClip(satResult, false,false, contactPointManifold, p1, p2,null,null);
    }

    private static Vector2[] clip(Vector2 v1, Vector2 v2, Vector2 n, double o){
        Vector2 clipPoints[] = new Vector2[2];
        double d1 = v1.dotProduct(n)-o;
        double d2 = v2.dotProduct(n)-o;

        if(d1 >= 0.0){
            clipPoints[0] = v1;
        }
        if(d2 >= 0.0){
            clipPoints[1] = v2;
        }

        if(d1*d2 < 0.0){
            Vector2 e = v2.difference(v1);
            double u = d1 / (d1 - d2);
            e = e.multiply(u);
            e = e. add(v1);
            if(d1 < 0) {
                clipPoints[0] = e;
            }else{
                clipPoints[1] = e;
            }
        }
        return clipPoints;
    }

    /**
     * Get the edge of the polygon which "points" the most with the normal.
     *
     * @param p Collider
     * @param n Normal
     *
     * @return  Best edge.
     */
    private static Edge bestEdge(PolygonCollider p, Vector2 n){
        Vector2 vertices[] = p.getVertices();
        int l = vertices.length;
        int index = 0;
        double max = vertices[0].dotProduct(n);
        for(int i = 1; i < l; i++){
            double projection = vertices[i].dotProduct(n);
            if(projection > max){
                max = projection;
                index = i;
            }
        }

        Vector2 v1 = vertices[index];
        Vector2 v0 = vertices[(l+index-1)%l];
        Vector2 v2 = vertices[(index+1)%l];

        Vector2 left = v1.difference(v0).normalize();
        Vector2 right = v1.difference(v2).normalize();

        if(left.dotProduct(n) <= right.dotProduct(n)){
            return new Edge(v0,v1);
        }
        else{
            return new Edge(v1,v2);
        }
    }

    /**
     * Performs SAT collisionClip check between p1 and p2. The result is overlap depth and collision normal.
     *
     * @param p1    The first polygon collider
     * @param p2    The second polygon collider
     * @return      SATResult with information regarding the overlap and whether it was a collision or not.
     */
    public static SATResult poly2SAT(PolygonCollider p1, PolygonCollider p2){
        ArrayList<Vector2> normals = new ArrayList<>();

        Collections.addAll(normals,p1.getNormals());
        Collections.addAll(normals,p2.getNormals());

        Vector2 minProjection = new Vector2(0,0);
        Double minOverlap = Double.MAX_VALUE;
        boolean p2ContainsP1 = true;
        boolean p1ContainsP2 = true;

        for (Vector2 p: normals) {
            Projection p1Proj = p1.getProjection(p);
            Projection p2Proj = p2.getProjection(p);

            if(!p1Proj.overlap(p2Proj)){
                return new SATResult(new Vector2(), 0, false, false, false);
            }else{
                double overlap = p1Proj.getOverlap(p2Proj);
                if(!p2Proj.contains(p1Proj)){
                    p2ContainsP1 = false;
                }

                if(!p1Proj.contains(p2Proj)){
                    p1ContainsP2 = false;
                }
                if(Math.abs(overlap) < Math.abs(minOverlap)) {
                    minOverlap = overlap;
                    minProjection = p;
                }
            }
        }
        return new SATResult(minProjection.multiply(minOverlap).normalize(), Math.abs(minOverlap), true,
                p2ContainsP1,p1ContainsP2);
    }
}
