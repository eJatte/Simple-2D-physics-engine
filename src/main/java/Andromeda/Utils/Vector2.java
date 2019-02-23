/*
 * Classname: Vector2.java
 * Author: Elias Jätte
 * Date: 2017-12-06
 */

package Andromeda.Utils;

import Andromeda.Collision.Transform;

import java.awt.*;

/**
 * A 2d vector class.
 * @author Elias Jätte, c16eje
 */
public class Vector2 {
    private double x;
    private double y;

    /**
     * Creates a new Vector2.
     *
     * @param x     x value
     * @param y     y value
     */
    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2(){
        this.x = 0;
        this.y = 0;
    }

    public Vector2(Point point){
        this.x = point.getX();
        this.y = point.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Get the magnitude of the vector, also called length.
     *
     * @return      The magnitude
     */
    public double magnitude(){
        return Math.sqrt(x*x+y*y);
    }

    public double nonSqrtMagnitude(){
        return x*x+y*y;
    }

    /**
     * Normalizes the vector to a vector with the same direction but
     * magnitude one. Also known as a unit vector.
     *
     * @return      The normalized vector. Unit vector.
     */
    public Vector2 normalize(){
        if(x == 0 && y == 0){
            return new Vector2(0,0);
        }
        return new Vector2(x/magnitude(), y/magnitude());
    }

    /**
     * Gets the "difference" between two vectors, that is how the input
     * vector relates this vector.
     *
     * @param vector2       The vector to compare with
     * @return              The difference vector.
     */
    public Vector2 difference(Vector2 vector2){
        return new Vector2(-vector2.getX()+x, -vector2.getY()+y);
    }

    /**
     * Multiplies this vector with the input
     *
     * @param vector2       The vector to multiply with
     * @return              The new multiplied vector
     */
    public Vector2 multiply(Vector2 vector2){
        return new Vector2(x*vector2.getX(),y*vector2.getY());
    }

    /**
     * Multiplies this vector with the scalar input
     *
     * @param s             The scalar to multiply with
     * @return              The multiplied vector
     */
    public Vector2 multiply(double s){
        return new Vector2(x*s,y*s);
    }

    public Vector2 multiply(Matrix_2x2 matrix_2x2){
        return matrix_2x2.getI().multiply(x).add(matrix_2x2.getJ().multiply(y));
    }

    public Vector2 multiply(Transform transform){
        return this.multiply(transform.getMatrix()).add(transform.getPosition());
    }

    /**
     * Adds this vector together with input vector
     *
     * @param vector2       The vector to add with
     * @return              The added vector
     */
    public Vector2 add(Vector2 vector2){
        return new Vector2(vector2.getX()+x, vector2.getY()+y);
    }

    /**
     * Checks if two vectors are the same
     *
     * @param v         The vector to compare with
     * @return          Bool stating whether they are.
     */
    public boolean equals(Vector2 v){
        return v.getX() == x && v.getY() == y;
    }

    public double dotProduct(Vector2 b){
        return this.getX()*b.getX()+this.getY()*b.getY();
    }

    public double crossProduct(Vector2 b) {
        return this.getX()*b.getY()-this.getY()*b.getX();
    }

    /**
     * Projects this vector onto p
     *
     * @param p
     * @return
     */
    public double projection(Vector2 p){
       return  this.dotProduct(p.normalize());
    }

    public Vector2 leftNormal(){
        return new Vector2(-y,x).normalize();
    }

    public Vector2 rightNormal(){
        return new Vector2(y,-x).normalize();
    }

    public Vector2 inverse(){
        return new Vector2(-x,-y);
    }

    /**
     * Gets a hash based on the whole numbers of the vector
     *
     *
     * @return      hash code
     */
    public int getDiscretehashCode(){
        int tmp = ( (int)y +  (((int)x+1)/2));
        return (int)x +  ( tmp * tmp);
    }

    public Vector2 clone(){
        return new Vector2(x,y);
    }

    public String toString(){
        return x+","+y;
    }

}
