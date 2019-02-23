package Andromeda.Collision;

import Andromeda.Utils.Vector2;

/**
 * Classname:SATResult
 * Author: Elias Jätte
 * Date: 18/08/06
 *
 * Information container for SAT result.
 */
public class SATResult {
    private Vector2 normal;
    private double depth;
    private boolean collision;
    private boolean p2ContainsP1;
    private boolean p1ContainsP2;

    public SATResult(Vector2 normal, double depth, boolean collision,boolean p2ContainsP1, boolean p1ContainsP2) {
        this.normal = normal;
        this.depth = depth;
        this.collision = collision;
        this.p2ContainsP1 = p2ContainsP1;
        this.p1ContainsP2 = p1ContainsP2;
    }

    /**
     * Inverse of the sat result.
     *
     * @return
     */
    public SATResult getInverse(){
        return new SATResult(normal.inverse(), depth, collision, p1ContainsP2, p2ContainsP1);
    }

    /**
     * Get normal of collision.
     * @return
     */
    public Vector2 getNormal() {
        return normal;
    }

    /**
     * Depth of the collision. Depth of clipping between the two colliders. Depth is relative to the collision normal.
     * @return
     */
    public double getDepth() {
        return depth;
    }

    public boolean isCollision() {
        return collision;
    }

    /**
     * Check wheter one collider is completeley within the other
     * @return
     */
    public boolean isP2ContainsP1() {
        return p2ContainsP1;
    }

    /**
     * Check wheter one collider is completeley within the other
     * @return
     */
    public boolean isP1ContainsP2() {
        return p1ContainsP2;
    }

    /**
     * Get minimum translation vector for the collision.
     *
     * @return  MTV
     */
    public Vector2 getMTV(){
        return normal.inverse().multiply(depth);
    }
}
