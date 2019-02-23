package Andromeda.Render;

import Andromeda.Collision.Transform;
import Andromeda.Utils.Vector2;

/**
 * Classname: Camera
 * Author: Elias Jätte
 * Date: 18/08/21
 *
 * Camera class for rendering.
 */
public class Camera {
    private Transform transform = new Transform(null);
    private double scale = 1;

    public Transform getTransform(double hwRatio){
        Transform transform = this.transform.clone();
        if(hwRatio <= 1.0){
            transform.scale(new Vector2(scale*(1/hwRatio), scale));
        }
        else {
            transform.scale(new Vector2(scale, scale*hwRatio));
        }
        return new Transform(transform.getMatrix().inverse(), transform.getPosition().inverse().multiply(transform.getMatrix()
                .inverse()).add(new Vector2(0.5,0.5))
                ,transform.gameObject);
    }

    public void scale(double s){
        scale = s;
        if(scale < 1)
            scale = 1;
    }

    public void addScale(double s){
        scale += s;
        if(scale < 1)
            scale = 1;
    }

    public double getScale() {
        return scale;
    }

    public void translate(Vector2 v){
        transform.translate(v);
    }

    public void setPosition(Vector2 v){
        transform.setPosition(v);
    }

    public void rotate(double degrees){
        transform.rotate(degrees);
    }

    public void setTransform(Transform transform){
        this.transform = transform;
    }
}
