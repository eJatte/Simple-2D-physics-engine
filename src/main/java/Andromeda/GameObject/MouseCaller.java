package Andromeda.GameObject;

import Andromeda.Collision.Collider;
import Andromeda.Collision.PolygonCollider;
import Andromeda.Collision.SATResult;
import Andromeda.Collision.Transform;
import Andromeda.Input.Input;
import Andromeda.Input.KeyCode;
import Andromeda.Polygon.Polygon;

/**
 * Classname: MouseCaller
 * Author: Elias Jätte
 * Date: 18/08/27
 */
public class MouseCaller extends GameObject{
    public MouseCaller(){
        collider = new PolygonCollider(Polygon.point(), this);
    }

    @Override
    public void update() {
        transform.setPosition(Input.getMouseWorldPosition());
    }

    @Override
    public void onTriggerEnter(Collider collider, SATResult satResult) {
        collider.gameObject.onMouseEnter();
    }

    @Override
    public void onTriggerStay(Collider collider, SATResult satResult) {
        collider.gameObject.onMouseOver();
    }

    @Override
    public void onTriggerLeave(Collider collider, SATResult satResult) {
        collider.gameObject.onMouseLeave();
    }
}
