package Andromeda.GameObject;

import Andromeda.Collision.PolygonCollider;
import Andromeda.Input.Input;
import Andromeda.Input.KeyCode;
import Andromeda.Manager.Time;
import Andromeda.Physics.Rigidbody;
import Andromeda.Polygon.Polygon;
import Andromeda.Utils.Vector2;

import java.awt.*;
import java.security.Key;

/**
 * Classname: BoxObject
 * Author: Elias Jätte
 * Date: 18/08/12
 *
 * Basic game object for testing.
 */
public class BoxObject extends GameObject {
    public BoxObject(){
        collider = new PolygonCollider(Polygon.square(), this);
        rigidbody = new Rigidbody(collider);
        drawable = collider;
    }

    public BoxObject(Polygon polygon){
        collider = new PolygonCollider(polygon, this);
        rigidbody = new Rigidbody(collider);
        drawable = collider;
    }

    public void start() {

    }

    public void update(){
        rigidbody.addForce(9.82*rigidbody.getMass()/Time.updateRate*Time.timeScale*0, new Vector2(0,1), collider
                .getWorldCentroid
                        ());
    }

    @Override
    public void onMouseEnter() {
        collider.setColor(new Color(225, 82, 74));
    }
    Vector2 last = new Vector2();
    public void onMouseOver(){
        if(Input.getKey(KeyCode.LeftMouse) && !Input.getKey(KeyCode.Shift)){
            last =Input.getMouseWorldPosition();

            this.transform.setPositionCentroid(Input.getMouseWorldPosition());
            this.rigidbody.setAngMomentum(0);
            this.rigidbody.setMomentum(new Vector2());
        }
        double m = this.rigidbody.getMass()/this.rigidbody.collider.getArea();
        if(Input.getKey(KeyCode.X)){
            this.transform.scale(1.01, Input.getMouseWorldPosition());
        } else if(Input.getKey(KeyCode.Z)){
            this.transform.scale(0.99, Input.getMouseWorldPosition());
        }
        this.rigidbody.setMass(m*this.rigidbody.collider.getArea());

        if(Input.getKeyUp(KeyCode.LeftMouse) && this.hasRigidbody()){
            Vector2 v = Input.getMouseWorldPosition().difference(last);
            this.rigidbody.addForce(v.magnitude()*10*rigidbody.getMass(), v.normalize(), collider.getWorldCentroid());
        }
        if(Input.getKeyUp(KeyCode.R)){
            this.rigidbody.isKinematic = !this.rigidbody.isKinematic;
        }

        if(Input.getKeyUp(KeyCode.F)){
            this.transform.setScale(1);
        }

        if(Input.getKeyUp(KeyCode.ScrollWheel)){
            destroy(this);
        }
    }

    @Override
    public void onMouseLeave() {
        collider.setColor(collider.getDefaultColor());
    }
}

