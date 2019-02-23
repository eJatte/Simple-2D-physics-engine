package Andromeda.Collision;

import Andromeda.Collision.LineCollision.Edge;
import Andromeda.Utils.Vector2;

import java.util.ArrayList;

/**
 * Classname: Collision
 * Author: Elias Jätte
 * Date: 18/08/19
 *
 * Class containing information regarding collision between two Colliders.
 */
public class Collision {
    private SATResult satResult;
    private boolean isCollision;
    private boolean flip;
    private ArrayList<Vector2> contactManifold;
    private Collider c1,c2;
    private Edge e1, e2;
    private double reactionImpulse;

    public Collision(SATResult satResult, boolean isCollision, boolean flip, ArrayList<Vector2> contactManifold,
                     Collider c1, Collider c2, Edge e1, Edge e2, double reactionImpulse) {
        this.satResult = satResult;
        this.isCollision = isCollision;
        this.flip = flip;
        this.contactManifold = contactManifold;
        this.c1 = c1;
        this.c2 = c2;
        this.e1 = e1;
        this.e2 = e2;
        this.reactionImpulse = reactionImpulse;
    }

    public Collision(SATResult satResult, boolean isCollision, boolean flip, ArrayList<Vector2> contactManifold,
                     Collider c1, Collider c2, Edge e1, Edge e2) {
        this.satResult = satResult;
        this.isCollision = isCollision;
        this.flip = flip;
        this.contactManifold = contactManifold;
        this.c1 = c1;
        this.c2 = c2;
        this.e1 = e1;
        this.e2 = e2;
        this.reactionImpulse = 0;
    }

    public Collision(CollisionClip collisionClip, ArrayList<Vector2> contactManifold){
        this.satResult = collisionClip.getSatResult();
        this.isCollision = collisionClip.isCollision();
        this.flip = collisionClip.isFlip();
        this.c1 = collisionClip.getC1();
        this.c2 = collisionClip.getC2();
        this.e1 = collisionClip.getE1();
        this.e2 = collisionClip.getE2();
        this.contactManifold = contactManifold;
        this.reactionImpulse = 0;
    }

    public Collision getInverse(){
        return new Collision(satResult.getInverse(),isCollision,!flip, contactManifold,c2,c1,e2,e1);
    }

    public void setReactionImpulse(double reactionImpulse) {
        this.reactionImpulse = reactionImpulse;
    }

    public double getReactionImpulse() {
        return reactionImpulse;
    }

    public Vector2 getContactNormal() {
        return satResult.getNormal();
    }

    public SATResult getSatResult() {
        return satResult;
    }

    public boolean isCollision() {
        return isCollision;
    }

    public boolean isFlip() {
        return flip;
    }

    public ArrayList<Vector2> getContactManifold() {
        return contactManifold;
    }

    public Collider getC1() {
        return c1;
    }

    public Collider getC2() {
        return c2;
    }

    public Edge getE1() {
        return e1;
    }

    public Edge getE2() {
        return e2;
    }
}
