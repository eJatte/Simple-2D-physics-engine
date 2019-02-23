package Andromeda.Physics;

import Andromeda.Collision.Collider;
import Andromeda.Collision.Collision;
import Andromeda.GameObject.GameObject;
import Andromeda.Utils.Vector2;

import java.util.ArrayList;

/**
 * Classname: ResolvedContact
 * Author: Elias Jätte
 * Date: 18/08/14
 *
 * Information container for resolved contact between two colliders.
 */
public class ResolvedContact {
    private Collider c1, c2;
    private Vector2 contactNormal;
    private double reactionImpulse;
    private Vector2 contactPoint;

    public ResolvedContact(Collider c1, Collider c2, Vector2 contactNormal, double reactionImpulse, Vector2 contactPoint) {
        this.c1 = c1;
        this.c2 = c2;
        this.contactNormal = contactNormal;
        this.reactionImpulse = reactionImpulse;
        this.contactPoint = contactPoint;
    }

    public Vector2 getContactPoint() {
        return contactPoint;
    }

    public double getReactionImpulse() {
        return reactionImpulse;
    }

    public Vector2 getContactNormal() {
        return contactNormal;
    }

    public Collider getC1() {
        return c1;
    }

    public Collider getC2() {
        return c2;
    }
}
