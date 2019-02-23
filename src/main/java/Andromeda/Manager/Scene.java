package Andromeda.Manager;

import Andromeda.Collision.Collision;
import Andromeda.Collision.CollisionClip;
import Andromeda.Collision.Transform;
import Andromeda.GameObject.GameObject;
import Andromeda.Physics.Physics;
import Andromeda.Physics.ResolvedContact;
import Andromeda.Render.Drawable;
import Andromeda.Render.Drawer;
import Andromeda.Render.RenderList;
import Andromeda.Updatable.Updatable;
import Andromeda.Utils.Matrix_2x2;
import Andromeda.Utils.Vector2;

import java.awt.*;
import java.util.ArrayList;

/**
 * Classname: Scene
 * Author: Elias Jätte
 * Date: 18/08/12
 *
 * Scene containing game objects.
 */
public class Scene implements Updatable, Drawable{
    private UpdateManager updateManager;
    private CollisionManager collisionManager;
    private PhysicsManager physicsManager;
    private RenderList renderList;
    private Drawer drawer;

    public Scene(){
        updateManager = new UpdateManager();
        collisionManager = new CollisionManager();
        physicsManager = new PhysicsManager();
        renderList = new RenderList();
    }

    public void addUpdatable(Updatable updatable){
        updateManager.addUpdatable(updatable);
    }

    public void addGameObject(GameObject gameObject){
        updateManager.addUpdatable(gameObject);
        if(gameObject.hasCollider()) {
            collisionManager.addPolygonCollider(gameObject.collider);
            if(!gameObject.collider.isTrigger && gameObject.hasRigidbody()){
                physicsManager.addRigidbody(gameObject.rigidbody);
            }
        }

        if(gameObject.hasDrawable()){
            renderList.addDrawable(gameObject.drawable);
        }
    }

    public void removeGameObject(GameObject gameObject){
        updateManager.removeUpdatable(gameObject);
        if(gameObject.hasCollider()) {
            collisionManager.removePolygonCollider(gameObject.collider);
            if(!gameObject.collider.isTrigger && gameObject.hasRigidbody()){
                physicsManager.removeRigidbody(gameObject.rigidbody);
            }
        }

        if(gameObject.hasDrawable()){
            renderList.removeDrawable(gameObject.drawable);
        }
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public PhysicsManager getPhysicsManager() {
        return physicsManager;
    }

    private ArrayList<Collision> collisions = new ArrayList<>();

    @Override
    public void update() {
        collisionManager.checkTriggers();
        collisionManager.callCollision(collisions);
        updateManager.update();

        physicsManager.applyForces();
        physicsManager.tickPhysics();
        ArrayList<CollisionClip> collisionClips = collisionManager.getCollisionClips();

        collisions = Physics.resolveOverlap(collisionClips);

        //renderList.removeDrawable(drawer);
        //drawer = new Drawer(collisions);
        //renderList.addDrawable(drawer);

        Physics.resolveCollisions(collisions);
        Physics.resolveFrictions(collisions);
    }

    @Override
    public void start() {

    }

    @Override
    public void awake() {

    }

    @Override
    public int getTickInterval() {
        return 1;
    }

    @Override
    public void draw(Graphics g, Transform transform) {
        ArrayList<Drawable> r = renderList.getDrawables();
        for (Drawable d:r) {
            d.draw(g,transform);
        }
    }

    @Override
    public int getLayer() {
        return 0;
    }
}
