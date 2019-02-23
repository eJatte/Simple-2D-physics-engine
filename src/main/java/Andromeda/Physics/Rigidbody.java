package Andromeda.Physics;

import Andromeda.Collision.Collider;
import Andromeda.Collision.Transform;
import Andromeda.GameObject.Component;
import Andromeda.Manager.Time;
import Andromeda.Utils.Vector2;

/**
 * Classname: Rigidbody
 * Author: Elias Jätte
 * Date: 18/08/12
 *
 * Class for rigid body.
 */
public class Rigidbody extends Component{
    public Collider collider;

    //Velocity in distance units per tick
    private Vector2 momentum;
    private Vector2 momentumForce;
    private Vector2 momentumReactionForce;

    //Angular velocity is in radians per tick
    private double angMomentum;
    private double angMomentumForce;
    private double angMomentumReactionForce;

    private double mass = 1;
    private double drag = 0;
    private double angularDrag = 0.00005;
    private double momentOfInertia;

    public double elasticity = 0.5;
    public double staticFriction = 0.2;
    public double dynamicFriction = 0.15;

    public boolean isKinematic = true;
    public boolean isRotatable = true;

    public Rigidbody(Collider collider){
        super(collider.gameObject);

        this.collider = collider;
        this.collider.isTrigger = false;
        angularDrag = 0;
        momentum = new Vector2();
        momentumForce = new Vector2();
        momentumReactionForce = new Vector2();

        angMomentum = 0;
        angMomentumForce = 0;
        angMomentumReactionForce = 0;
        calculateMomentOfInertia();
    }

    public void calculateMomentOfInertia(){
        //momentOfInertia = Physics.momentOfInertia(this);
        momentOfInertia = getMass();
    }

    public void tickPhysics(){
        gameObject.transform.translate(getVelocity().multiply(1/Time.updateRate*Time.timeScale));

        if(isRotatable && getAngularVelocity() != 0.0) {
            gameObject.transform.rotate(Math.toDegrees(getAngularVelocity()/Time.updateRate*Time.timeScale), collider
                    .getWorldCentroid());

        }
    }

    public void applyForces(){
        momentum = momentum.add(momentumForce);
        momentumForce = new Vector2();

        momentum = momentum.add(momentumReactionForce);
        momentumReactionForce = new Vector2();

        if(isRotatable) {
            angMomentum = angMomentum + angMomentumForce;
            angMomentumForce = 0;

            angMomentum = angMomentum + angMomentumReactionForce;

            angMomentumReactionForce = 0;
            //System.out.println(getAngularVelocity());
        }

        double vel = getVelocity().magnitude();

        double dragForce = 0.5*Math.pow(vel,2)*drag;
        addReactionForceLinear(dragForce,getVelocity().normalize().inverse(), collider.getWorldCentroid());

        double angularVel = getAngularVelocity();
        double angDragForce = 0.5*angularVel*angularVel*angularDrag;

        if(angularVel > 0)
            angDragForce = -angDragForce;
        angMomentumReactionForce = angMomentumReactionForce + angDragForce;
    }

    public void addForce(double force, Vector2 n, Vector2 point){
        if(force == 0 || !isKinematic)
            return;
        momentumForce = momentumForce.add(n.multiply(force));

        if(!isRotatable)
            return;
        Vector2 r = point.difference(collider.getWorldCentroid());
        angMomentumForce = angMomentumForce + force*(r.rightNormal().dotProduct(n));
    }

    public void addReactionForce(double force, Vector2 n, Vector2 point){

        addReactionForceLinear(force,n,point);

        addReactionForceAngular(force,n,point);
    }

    public void addReactionForceAngular(double force, Vector2 n, Vector2 point){
        if(force == 0 || !isRotatable || !isKinematic)
            return;
        Vector2 r = point.difference(collider.getWorldCentroid());
        double f = ((force)* (r.rightNormal().dotProduct(n)));
        angMomentumReactionForce = angMomentumReactionForce + f;
    }

    public void addReactionForceLinear(double force, Vector2 n, Vector2 point){
        if(force == 0 || !isKinematic)
            return;
        //System.out.println("Linear: "+force);
        Vector2 f = n.multiply(force);
        momentumReactionForce = momentumReactionForce.add(f);
    }

    public Vector2 getMomentumForce() {
        return momentumForce;
    }

    public Vector2 getVelocity() {
        if(isKinematic)
            return momentum.multiply(1.0/mass);
        else
            return new Vector2();
    }

    public double getAngularVelocity() {
        if(isKinematic)
            return angMomentum/(momentOfInertia);
        else
            return 0;
    }

    public void setAngMomentum(double angMomentum) {
        this.angMomentum = angMomentum;
    }

    public void setMomentum(Vector2 momentum) {
        this.momentum = momentum;
    }

    public Vector2 getMomentum(){
        return momentum;
    }

    public double getAngMomentum(){
        return angMomentum;
    }

    public Vector2 getCrossRadialVelocity(Vector2 point){
        Vector2 r = point.difference(collider.getWorldCentroid());
        double rm = r.magnitude();
        Vector2 n = r.rightNormal();
        return n.multiply(rm*getAngularVelocity());
    }

    public double getMass() {
        return mass;
    }

    public double getMomentOfInertia() {
        return momentOfInertia;
    }

    public void setMass(double mass) {
        if(mass <= 0.0000001)
            mass = 0.0000001;
        this.mass = mass;
        calculateMomentOfInertia();
    }

    public double getDrag() {
        return drag;
    }

    public void setDrag(double drag) {
        if(drag < 0)
            drag = 0;
        else if(drag > 1)
            drag = 1;
        this.drag = drag;
    }
}
