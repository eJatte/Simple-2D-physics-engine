package Andromeda.Collision;

import Andromeda.GameObject.GameObject;
import Andromeda.Utils.Matrix_2x2;
import Andromeda.Utils.Vector2;

import java.awt.*;

/**
 * Classname: Transform
 * Author: Elias Jätte
 * Date: 18/07/30
 *
 * Contains information regarding rotation, position and scale in 2d-space. Contains methods for manipulating
 * rotation, position and scale in 2d-space.
 */
public class Transform {
    protected Vector2 position;
    protected Matrix_2x2 matrix;
    public GameObject gameObject;

    public Transform(GameObject gameObject){
        this.gameObject = gameObject;
        position = new Vector2();
        matrix = new Matrix_2x2();
    }

    public Transform(Matrix_2x2 matrix, Vector2 position, GameObject gameObject){
        this.matrix = matrix;
        this.position = position;
        this.gameObject = gameObject;
    }

    public void translate(Vector2 v){
        position = position.add(v);
    }

    public void setPosition(Vector2 v){
        position = v;
    }

    public void setPositionCentroid(Vector2 v){
        Vector2 centroid;
        if(gameObject.hasCollider())
            centroid = gameObject.collider.getWorldCentroid();
        else
            centroid = new Vector2();
        position = v.difference(centroid.difference(position));
    }

    public Vector2 getPosition(){
        return position;
    }

    public Matrix_2x2 getMatrix(){
        return matrix;
    }

    public void setMatrix(Matrix_2x2 matrix){
        this.matrix = matrix;
    }

    public Vector2 worldToLocalPoint(Vector2 point){
        return point.difference(position).multiply(matrix.inverse());
    }

    public Vector2 localToWorldPoint(Vector2 point){
        return point.multiply(matrix).add(position);
    }

    public Vector2 worldToLocalVector(Vector2 v){
        return v.multiply(matrix.inverse());
    }

    public Vector2 localToWorldVector(Vector2 v){
        return v.multiply(matrix);
    }

    public void scale(double scale){
        scale(new Vector2(scale,scale));
    }

    public void scale(double scale, Vector2 point){
        scale(new Vector2(scale,scale), point);
    }

    public void scale(Vector2 scale){
        matrix = matrix.scale(scale);
        if(gameObject != null)
            if(gameObject.hasRigidbody())
                gameObject.rigidbody.calculateMomentOfInertia();
    }

    public void scale(Vector2 scale, Vector2 point){
        matrix = matrix.scale(scale);
        Vector2 distance = point.difference(this.position).multiply(new Vector2(1,1).difference(scale));
        this.translate(distance);
        if(gameObject != null)
            if(gameObject.hasRigidbody())
                gameObject.rigidbody.calculateMomentOfInertia();
    }

    public void setScale(double scale){
        setScale(new Vector2(scale,scale));
    }

    public void setScale(Vector2 scale){
        matrix = matrix.setScale(scale);
        if(gameObject != null)
            if(gameObject.hasRigidbody())
                gameObject.rigidbody.calculateMomentOfInertia();
    }

    public void rotate(double degrees){
        matrix = matrix.rotate_degree(degrees);
    }

    public void rotate(double degrees,Vector2 point){
        rotate(degrees);

        Vector2 oldRelative = position.difference(point);
        Vector2 newRelative = oldRelative.multiply(new Matrix_2x2().rotate_degree(degrees));
        position = position.add(newRelative.difference(oldRelative));
    }

    public void align(Vector2 a, Vector2 b){
        matrix = matrix.align(a,b);
    }

    public Transform multiply(Transform transform){
        Matrix_2x2 matrix = this.matrix.multiply(transform.getMatrix());
        Vector2 position = transform.getPosition().add(this.position.multiply(transform.getMatrix()));
        return new Transform(matrix,position,gameObject);
    }

    public Transform multiply(Matrix_2x2 matrix){
        Matrix_2x2 m = this.matrix.multiply(matrix);
        return new Transform(m,position,gameObject);
    }

    public void  localMultiply(Transform transform){
        this.matrix = this.matrix.multiply(transform.getMatrix());
        this.position = transform.getPosition().add(this.position.multiply(transform.getMatrix()));
    }

    public void  localMultiply(Matrix_2x2 matrix){
        this.matrix = this.matrix.multiply(matrix);
    }

    public Transform inverse(){
        return new Transform(matrix.inverse(),position.inverse().multiply(matrix.inverse()),gameObject);
    }

    public boolean isFlipped(){
        return matrix.getI().leftNormal().dotProduct(matrix.getJ()) < 0;
    }

    public Transform clone(){
        return new Transform(matrix.clone(),position.clone(),gameObject);
    }

    public String toString(){
        return "Position: "+position.getX()+","+position.getY()+"\n"+matrix.toString();
    }

    public void drawTransformArrow(Graphics g, Transform transform){
        Vector2 origin = position.multiply(transform);
        Vector2 iEnd = this.matrix.getI().normalize().multiply(0.4).add(position).multiply(transform);
        g.setColor(new Color(56, 65, 192));
        g.drawLine((int)origin.getX(),(int)origin.getY(),(int)iEnd.getX(),(int)iEnd.getY());

        Vector2 jEnd = this.matrix.getJ().normalize().multiply(0.4).add(position).multiply(transform);
        g.setColor(new Color(192, 37, 38));
        g.drawLine((int)origin.getX(),(int)origin.getY(),(int)jEnd.getX(),(int)jEnd.getY());
    }
}
