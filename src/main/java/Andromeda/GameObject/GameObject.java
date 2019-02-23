package Andromeda.GameObject;

import Andromeda.Collision.*;
import Andromeda.Manager.GameManager;
import Andromeda.Manager.Scene;
import Andromeda.Physics.Rigidbody;
import Andromeda.Render.Drawable;
import Andromeda.Updatable.Updatable;
import Andromeda.Utils.Vector2;

/**
 * Classname: GameObject
 * Author: Elias Jätte
 * Date: 18/08/11
 *
 */
public abstract class GameObject implements Updatable{
    public Transform transform;
    public PolygonCollider collider;
    public Drawable drawable;
    public Rigidbody rigidbody;

    public GameObject(){
        transform = new Transform(this);
    }

    public void update() {}

    public void start() {}

    public void awake() {}


    public void onCollisionStay(Collision collision){}

    public void onCollisionEnter(Collision collision){}

    public void onCollisionLeave(Collision collision){}


    public void onTriggerStay(Collider collider, SATResult satResult){}

    public void onTriggerEnter(Collider collider, SATResult satResult){}

    public void onTriggerLeave(Collider collider, SATResult satResult){}

    /**
     * Called each frame when the mouse is over this object, based on its collider.
     */
    public void onMouseOver(){}

    /**
     * Called when the mouse enters this object, based on its collider.
     */
    public void onMouseEnter(){}

    /**
     * Called when the mouse leaves this object, based on its collider.
     */
    public void onMouseLeave(){}

    /**
     * Called each frame when the mouse is clicked and over this object, based on its collider.
     */
    public void onMouse(){}

    /**
     * Called when the mouse is down over this object, based on its collider.
     */
    public void onMouseDown(){}

    /**
     * Called when the mouse is up over this object, based on its collider.
     */
    public void onMouseUp(){}


    public boolean hasCollider(){
        return collider != null;
    }

    public boolean hasDrawable(){
        return drawable != null;
    }

    public boolean hasRigidbody(){ return rigidbody != null; }

    @Override
    public int getTickInterval(){
        return 1;
    }

    public static void destroy(GameObject gameObject){
        GameManager.getInstance().getScene().removeGameObject(gameObject);
    }

    public static void instantiate(GameObject gameObject){
        GameManager.getInstance().getScene().addGameObject(gameObject);
    }

    public static void instantiate(GameObject gameObject, Vector2 position){
        gameObject.transform.setPosition(position);
        instantiate(gameObject);
    }
}
