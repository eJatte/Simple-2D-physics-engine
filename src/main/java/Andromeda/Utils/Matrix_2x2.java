package Andromeda.Utils;

import Andromeda.Collision.Transform;

/**
 * Classname: Matrix_2x2
 * Author: Elias Jätte
 * Date: 18/07/28
 *
 * 2x2 Matrix class with methods to manipulate matrix.
 */
public class Matrix_2x2 {
    private Vector2 i, j;

    public Matrix_2x2(Vector2 i, Vector2 j){
        this.i = i;
        this.j = j;
    }

    public Matrix_2x2(double a, double b, double c, double d){
        this.i = new Vector2(a,c);
        this.j = new Vector2(b,d);
    }

    public Matrix_2x2(){
        this.i = new Vector2(1,0);
        this.j = new Vector2(0,1);
    }

    public Vector2 getI() {
        return i;
    }

    public Vector2 getJ() {
        return j;
    }

    public Matrix_2x2 multiply(Matrix_2x2 matrix_2x2){
        Vector2 ni = this.i.multiply(matrix_2x2);
        Vector2 nj = this.j.multiply(matrix_2x2);
        return new Matrix_2x2(ni,nj);
    }

    public Matrix_2x2 multiply(double s){
        Vector2 ni = this.i.multiply(s);
        Vector2 nj = this.j.multiply(s);
        return new Matrix_2x2(ni,nj);
    }

    public double determinant(){
        return i.getX()*j.getY()-i.getY()*j.getX();
    }

    public Matrix_2x2 inverse(){
        double d = determinant();
        if(d == 0)
            return null;
        return new Matrix_2x2(j.getY(), -j.getX(), -i.getY(), i.getX()).multiply(1/d);
    }

    public Matrix_2x2 rotate_degree(double degree){
        return rotate_radian(Math.toRadians(degree));
    }

    public Matrix_2x2 rotate_radian(double radian){
        Vector2 i = new Vector2(Math.cos(radian), -Math.sin(radian));
        Vector2 j = new Vector2(Math.sin(radian), Math.cos(radian));

        return this.multiply(new Matrix_2x2(i,j));
    }

    /**
     * Aligns unit vector a with unit vector b. Rotates the matrix such that what was a is now b.
     * @param a
     * @param b
     * @return
     */
    public Matrix_2x2 align(Vector2 a, Vector2 b){
        double abDot = a.dotProduct(b);
        if(abDot == -1.0){
            return rotate_radian(Math.PI);
        }
        else if(abDot == 1.0){
            return this;
        }
        double c = a.difference(b).magnitude();
        double radian = Math.asin(c/2)*2;

        Vector2 n = a.rightNormal();
        double r =b.dotProduct(n);
        if(r < 0){
            radian = -radian;
        }

        return rotate_radian(radian);
    }

    public Matrix_2x2 normalize(){
        return new Matrix_2x2(i.normalize(),j.normalize());
    }

    public Matrix_2x2 scale(Vector2 s){
        Vector2 i = this.i.multiply(s.getX());
        Vector2 j = this.j.multiply(s.getY());
        return new Matrix_2x2(i,j);
    }

    public Matrix_2x2 setScale(Vector2 s){
        Vector2 i = this.i.normalize().multiply(s.getX());
        Vector2 j = this.j.normalize().multiply(s.getY());
        return new Matrix_2x2(i,j);
    }

    public Matrix_2x2 clone(){
        return new Matrix_2x2(i.clone(),j.clone());
    }

    public String toString(){
        return i.getX()+" "+j.getX()+"\n"+i.getY()+" "+j.getY();
    }
}
