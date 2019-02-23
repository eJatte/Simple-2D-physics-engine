package Andromeda.Collision.LineCollision;

import Andromeda.Utils.Vector2;

/**
 * Classname: EdgeCollision.java
 * Author: Elias Jätte
 * Date: 18/08/04
 *
 * Class for collision between edges.
 */
public class EdgeCollision {
    private Vector2 intersect;
    private boolean collinear;
    private boolean paralell;
    private Edge e1, e2;



    public EdgeCollision(Vector2 intersect, boolean collinear, boolean paralell, Edge e1, Edge e2) {
        this.intersect = intersect;
        this.collinear = collinear;
        this.paralell = paralell;
        this.e1 = e1;
        this.e2 = e2;
    }

    public Vector2 getIntersect() {
        return intersect;
    }

    public boolean intersect(){
        return intersect != null;
    }

    public boolean isCollinear() {
        return collinear;
    }

    public boolean isParalell() {
        return paralell;
    }

    public Edge getE1() {
        return e1;
    }

    public Edge getE2() {
        return e2;
    }

    public static EdgeCollision intersect(Edge e1, Edge e2){
        double rXs = e1.getR().crossProduct(e2.getR());
        double qpr = e2.getP().difference(e1.getP()).crossProduct(e1.getR());
        double qps = e2.getP().difference(e1.getP()).crossProduct(e2.getR());

        if(rXs == 0 && qpr == 0){
            return new EdgeCollision(null, true, true,e1,e2);
        }
        else if(rXs == 0 && qpr != 0){
            return new EdgeCollision(null, false, true,e1,e2);
        }

        double t = qps/rXs;
        double u = qpr/rXs;

        if(t >= 0 && t <= 1 && u >= 0 && u <= 1){
            return new EdgeCollision(e1.getP().add(e1.getR().multiply(t)), false,false, e1,e2);
        }
        return new EdgeCollision(null,false,false,e1,e2);
    }
}
