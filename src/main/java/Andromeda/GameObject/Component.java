package Andromeda.GameObject;

import Andromeda.Collision.Transform;

/**
 * Classname: Component
 * Author: Elias Jätte
 * Date: 18/08/24
 */
public abstract class Component {
    public boolean isEnabled = true;
    public GameObject gameObject;
    public Transform transform;

    public Component(GameObject gameObject) {
        this.gameObject = gameObject;
        this.transform = new Transform(gameObject);
    }

    public Transform getTransform() {
        return transform.multiply(gameObject.transform);
    }
}
