package Andromeda.Manager;

import Andromeda.Physics.Rigidbody;

import java.util.ArrayList;

/**
 * Classname: PhysicsManager
 * Author: Elias Jätte
 * Date: 18/08/12
 *
 * Manager for rigid bodies.
 */
public class PhysicsManager {
    private ArrayList<Rigidbody> rigidbodies;

    public PhysicsManager(){
        rigidbodies = new ArrayList<>();
    }

    public void applyForces(){
        for (Rigidbody r:rigidbodies) {
            r.applyForces();
        }
    }

    public void tickPhysics(){
        for (Rigidbody r:rigidbodies) {
            r.tickPhysics();
        }
    }

    public void addRigidbody(Rigidbody rigidbody){
        rigidbodies.add(rigidbody);
    }

    public void removeRigidbody(Rigidbody rigidbody){
        rigidbodies.remove(rigidbody);
    }
}
