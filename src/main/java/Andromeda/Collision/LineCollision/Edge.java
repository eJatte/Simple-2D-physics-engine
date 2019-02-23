package Andromeda.Collision.LineCollision;

import Andromeda.Collision.Transform;
import Andromeda.Utils.Matrix_2x2;
import Andromeda.Utils.Vector2;

import java.awt.*;


/**
 * Classname: Edge
 * Author: Elias Jätte
 * Date: 18/08/04
 *
 * A class to represent an edge in 2 dimensions.
 */
public class Edge {
    //Origin point
    private Vector2 p;
    //Is relative to p. Defines edge together with p.
    private Vector2 r;
    private Vector2 leftNormal;
    private Vector2 rightNormal;
    private Vector2 middlePoint;
    private int p1Indice, p2Indice;

    public Edge(Vector2 point1, Vector2 point2){
        p = point1;
        r = point2.difference(point1);
        leftNormal = r.leftNormal();
        rightNormal = r.rightNormal();
        middlePoint = p.add(r.multiply(0.5));
    }

    public Edge(Vector2 point1, Vector2 point2, int p1Indice, int p2Indice){
        p = point1;
        r = point2.difference(point1);
        leftNormal = r.leftNormal();
        rightNormal = r.rightNormal();
        middlePoint = p.add(r.multiply(0.5));

        this.p1Indice = p1Indice;
        this.p2Indice = p2Indice;
    }

    /**
     * Transform edge.
     *
     * @param transform the transform to apply
     *
     * @return  Edge
     */
    public Edge multiply(Transform transform){
        Vector2 p1 = p.multiply(transform.getMatrix()).add(transform.getPosition());
        Vector2 p2 = p.add(r).multiply(transform.getMatrix()).add(transform.getPosition());
        return new Edge(p1,p2);
    }

    public int getP1Indice() {
        return p1Indice;
    }

    public int getP2Indice() {
        return p2Indice;
    }

    public void drawEdge(Graphics g, Transform transform){
        Vector2 start = p.multiply(transform);
        Vector2 end = p.add(r).multiply(transform);
        g.drawLine((int)start.getX(),(int)start.getY(),(int)end.getX(),(int)end.getY());
    }

    /**
     * !!!!!
     * Swing dependent, bad design. Should be moved out to edgeDrawer or something similar.
     */
    public void drawLeftNormal(Graphics g, Transform transform){
        Vector2 start = middlePoint.multiply(transform);
        Vector2 end = middlePoint.add(leftNormal.multiply(0.2)).multiply(transform);
        g.drawLine((int)start.getX(),(int)start.getY(),(int)end.getX(),(int)end.getY());
    }

    /**
     * !!!!!
     * Swing dependent, bad design. Should be moved out to edgeDrawer or something similar.
     */
    public void drawRightNormal(Graphics g, Transform transform){
        Vector2 start = middlePoint.multiply(transform);
        Vector2 end = middlePoint.add(rightNormal.multiply(0.2)).multiply(transform);
        g.drawLine((int)start.getX(),(int)start.getY(),(int)end.getX(),(int)end.getY());
    }

    public Vector2 getMiddlePoint() {
        return middlePoint;
    }

    public Vector2 getLeftNormal() {
        return leftNormal;
    }

    public Vector2 getRightNormal() {
        return rightNormal;
    }

    public Vector2 getP1(){
        return p;
    }

    public Vector2 getP2(){
        return p.add(r);
    }

    public Vector2 getP() {
        return p;
    }

    public Vector2 getR() {
        return r;
    }

    public String toString(){
        return p.toString()+" - "+p.add(r).toString();
    }
}
