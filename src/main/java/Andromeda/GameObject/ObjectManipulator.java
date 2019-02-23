package Andromeda.GameObject;

import Andromeda.Collision.Collider;
import Andromeda.Collision.LineCollision.Edge;
import Andromeda.Collision.PolygonCollider;
import Andromeda.Collision.Raycast.Raycast;
import Andromeda.Input.Input;
import Andromeda.Input.KeyCode;
import Andromeda.Manager.GameManager;
import Andromeda.Manager.Time;
import Andromeda.Polygon.Polygon;
import Andromeda.Utils.Vector2;


import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Classname: ObjectManipulator
 * Author: Elias Jätte
 * Date: 18/08/26
 *
 * Quick and dirty class for manipulating game objects.
 *
 * LeftClick to move.
 * Right click and drag to "slice" objects in half.
 * Q to stop/start time.
 * E to spawn box
 * R to set kinematic or not
 * Z and X to scale
 */
public class ObjectManipulator extends GameObject{
    public ObjectManipulator(){
        super();
    }

    private Vector2 last = new Vector2();
    @Override
    public void update() {
        if(Input.getKeyUp(KeyCode.E)){
            GameObject g = new BoxObject();
            g.transform.setPosition(Input.getMouseWorldPosition());

            g.transform.scale(new Vector2(3, 3));
            g.rigidbody.setMass(30);
            g.transform.rotate(0);
            g.rigidbody.isRotatable = true;
            g.rigidbody.elasticity = 0.1;

            g.collider.setDefaultColor(randomColor());

            GameManager.getInstance().getScene().addGameObject(g);
        }

        if(Input.getKeyDown(KeyCode.RightMouse)){
            last = Input.getMouseWorldPosition();
        }

        if(Input.getKeyUp(KeyCode.Q)){
            if(Time.timeScale != 0.0)
                Time.timeScale = 0.0;
            else
                Time.timeScale = 1.0;
        }

        if(Input.getKeyUp(KeyCode.RightMouse)){
            Vector2 v = Input.getMouseWorldPosition().difference(last);
            Raycast r = Raycast.raycast(last, v.normalize(), v.magnitude());
            for (Collider c:r.getColliders()) {
                if(!c.gameObject.rigidbody.isKinematic)
                    continue;
                PolygonCollider p = c.gameObject.collider;
                double totArea = p.getArea();
                ArrayList<Polygon> polygons = p.getPolygon().split(new Edge(last, Input.getMouseWorldPosition())
                        .multiply(c.getTransform().inverse()));
                for (Polygon polygon:polygons) {
                    GameObject g = new BoxObject(polygon);
                    g.transform.localMultiply(c.getTransform());
                    g.rigidbody.setMass(c.gameObject.rigidbody.getMass()*(g.collider.getArea()/totArea));
                    g.rigidbody.setMomentum(c.gameObject.rigidbody.getVelocity().multiply(g.rigidbody
                            .getMass()));
                    g.rigidbody.setAngMomentum(c.gameObject.rigidbody.getAngularVelocity()*g.rigidbody.getMomentOfInertia());
                    g.collider.setDefaultColor(randomColor());
                    GameManager.getInstance().getScene().addGameObject(g);
                }
                if(polygons.size() > 0)
                    destroy(c.gameObject);
            }
        }
    }

    private Color randomColor(){
        Random rand = new Random();

        int r = rand.nextInt(200)+50;
        int g = rand.nextInt(100)+150;
        int b = rand.nextInt(200)+50;

        return new Color(r, g, b);
    }
}
