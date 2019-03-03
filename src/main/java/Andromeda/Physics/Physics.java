package Andromeda.Physics;

import Andromeda.Collision.Collision;
import Andromeda.Collision.CollisionClip;
import Andromeda.Collision.LineCollision.Edge;
import Andromeda.Polygon.Polygon;
import Andromeda.Polygon.PolygonGeometry;
import Andromeda.Utils.Matrix_2x2;
import Andromeda.Utils.Vector2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Classname: Physics
 * Author: Elias Jätte
 * Date: 18/08/13
 */
public class Physics {

    public static double momentOfInertia(Rigidbody rigidbody){
        Vector2 c = rigidbody.collider.getCentroid();
        double totArea = rigidbody.collider.getArea();
        Vector2 vertices[] = rigidbody.gameObject.collider.getPolygon().getVertices();
        Matrix_2x2 m = rigidbody.collider.getTransform().getMatrix();

        double iSum = 0;
        double massDiameterSum = 0;

        for(int i = 0; i < vertices.length; i++){
            Vector2 v1 = vertices[i].multiply(m);
            Vector2 v2 = vertices[(i+1)%vertices.length].multiply(m);

            Vector2 bEdge = v2.difference(c);
            double b = bEdge.magnitude();
            Vector2 nh = bEdge.leftNormal();
            double hMin = c.dotProduct(nh);
            double hMax = v1.dotProduct(nh);
            double h = Math.abs(hMax - hMin);

            Vector2 na = bEdge.normalize();
            double aMin = c.dotProduct(na);
            double aMax = v1.dotProduct(na);
            double a = Math.abs(aMax-aMin);

            double I = (b*b*b*h-b*b*h*a+b*h*a*a+b*h*h*h)/36;

            iSum += I;

            Polygon p = new Polygon(new Vector2[]{v1,v2,c});
            PolygonGeometry pg = PolygonGeometry.getPolygonGeometry(p);
            double triArea = pg.getArea();
            Vector2 cT = pg.getCentroid();

            double d2 = cT.difference(c).nonSqrtMagnitude();

            massDiameterSum += (triArea/totArea)*d2;
        }
        return iSum+massDiameterSum*rigidbody.getMass();
    }

    /**
     * Resolves overlap based on velocity. Such that each collider is moved as much as they contributed to the overlap.
     * For example if v1=5 and v2=15 then p1 is responsible for 25% of the overlap and p2 75%
     *
     * This overlap resolver is very simple. It only resolves overlap on a one collision at a time basis.
     *
     * @param collisionClips    Collision clips
     *
     * @return  Resolved collision. With adjusted contact information.
     */
    public static ArrayList<Collision> resolveOverlap(Collection<CollisionClip> collisionClips){
        ArrayList<Collision> resolvedCollision = new ArrayList<>();

        for(CollisionClip collisionClip : collisionClips){
            if(!collisionClip.isCollision()){
                resolvedCollision.add(new Collision(collisionClip,new ArrayList<>()));
                continue;
            }
            Rigidbody rig1 = collisionClip.getC1().gameObject.rigidbody;
            Rigidbody rig2 = collisionClip.getC2().gameObject.rigidbody;
            Vector2 mtv = collisionClip.getMTV();
            if ((!rig1.isKinematic && !rig2.isKinematic))
                continue;
            if(mtv.magnitude() == 0){
                continue;
            }

            double m;

            double d1 = rig1.getVelocity().dotProduct(mtv);
            double d2 = rig2.getVelocity().dotProduct(mtv);

            //Calculate percentage of overlap contribution.
            if(!rig1.isKinematic || !rig2.isKinematic){
                m = 0.5;
                if(rig1.isKinematic) {
                    m += 0.5;
                } else if(rig2.isKinematic) {
                    m -= 0.5;
                }
            }
            else if(d1*d2 < 0){
                m = Math.abs(d1) / (Math.abs(d1) + Math.abs(d2));
            } else {
                if(Math.abs(d1) > Math.abs(d2)){
                    m = 1.0;
                } else if(Math.abs(d2) > Math.abs(d1)){
                    m = 0.0;
                } else{
                    m = 0.5;
                }
            }

            rig1.gameObject.transform.translate(mtv.multiply(m));
            rig2.gameObject.transform.translate(mtv.inverse().multiply(1 - m));

            resolvedCollision.add(new Collision(collisionClip,collisionClip.getCollisionManifold(m)));
        }

        return resolvedCollision;
    }

    public static void resolveCollisions(Collection<Collision> collisions){
        for (Collision collision : collisions) {
            resolveCollision(collision);
        }
    }

    /**
     * Resolve collision between two rigidbodies.
     * Utilizes an Impulse-based reaction model.
     * Computes reactionary forces for the two bodies involved in the collision.
     * Computes angular and linear forces.
     *
     * @param collision Collision information.
     */
    public static void resolveCollision(Collision collision){
        if(!collision.isCollision())
            return;
        Rigidbody rig1 = collision.getC1().gameObject.rigidbody;
        Rigidbody rig2 = collision.getC2().gameObject.rigidbody;

        Vector2 n = collision.getContactNormal();


        ArrayList<Vector2> collisionManifold = collision.getContactManifold();

        int size = collisionManifold.size();
        Edge e1 = collision.getE1();
        Edge e2 = collision.getE2();
        boolean closeParalell = false;

        if(size == 2){
            Vector2 v1 = collisionManifold.get(0);
            Vector2 v2 = collisionManifold.get(1);
            Vector2 p = n.leftNormal();
            double d1 = v1.dotProduct(p);
            double d2 = v2.dotProduct(p);
            double c1 = rig1.collider.getWorldCentroid().dotProduct(p);
            double c2 = rig2.collider.getWorldCentroid().dotProduct(p);
            double c1d1 = d1-c1;
            double c1d2 = d2-c1;
            double c2d1 = d1-c2;
            double c2d2 = d2-c2;

            boolean diffSides1 = c1d1*c1d2 <= 0;
            boolean diffSides2 = c2d1*c2d2 <= 0;


            if(e1 != null && e2 != null) {
                Vector2 en1 = e1.getR().leftNormal();
                Vector2 en2 = e2.getR().leftNormal();

                //This also uses the fact that in collision clip when two clip points are 0.01 units apart they count as
                // being the same depth
                closeParalell = en1.dotProduct(en2) < -0.9995 && size == 2;


                //This is to fix rocking which arises when using constant forces such as gravity
                if (closeParalell && Math.abs(rig1.getAngularVelocity()) < 0.2 && Math.abs(rig2.getAngularVelocity()) < 0.2
                        && (!rig1.isKinematic || !rig2.isKinematic)) {
                    if (collision.isFlip() && rig1.isKinematic && rig1.isRotatable && diffSides1) {
                        rig1.gameObject.transform.align(en1, en2.inverse());
                        rig1.setAngMomentum(0);
                    } else if (rig2.isKinematic && rig2.isRotatable && diffSides2) {
                        rig2.gameObject.transform.align(en2, en1.inverse());
                        rig2.setAngMomentum(0);
                    } else{
                        closeParalell = false;
                    }


                } else {
                    closeParalell = false;
                }
            }


            double jr1 = getJr(rig1,rig2,v1,n);
            double jr2 = getJr(rig1,rig2,v2,n);
            Vector2 middle = v1.add(v2.difference(v1).multiply(0.5));
            double jrMiddle = getJr(rig1,rig2,middle,n);

            if(rig1.isKinematic) {
                if (diffSides1) {
                    if(!closeParalell) {
                        rig1.addReactionForceAngular(jr1, n.inverse(), v1);
                        rig1.addReactionForceAngular(jr2, n.inverse(), v2);;
                    }
                    rig1.addReactionForceLinear(jrMiddle, n.inverse(), middle);
                } else {
                    if(!closeParalell) {
                        rig1.addReactionForceAngular(jrMiddle, n.inverse(), middle);
                    }
                    rig1.addReactionForceLinear(jrMiddle, n.inverse(), middle);
                }
            }

            if(rig2.isKinematic) {
                if (diffSides2) {
                    if(!closeParalell) {
                        rig2.addReactionForceAngular(jr1, n, v1);
                        rig2.addReactionForceAngular(jr2, n, v2);
                    }
                    rig2.addReactionForceLinear(jrMiddle, n, middle);
                } else {
                    if(!closeParalell) {
                        rig2.addReactionForceAngular(jrMiddle, n, middle);
                    }
                    rig2.addReactionForceLinear(jrMiddle, n, middle);
                }
            }
            collision.setReactionImpulse(jrMiddle);
        }
        else if(size == 1){
            Vector2 cPoint = collisionManifold.get(0);
            double jr = getJr(rig1,rig2, cPoint,n);
            if (rig1.isKinematic) {
                if(!closeParalell) {
                    rig1.addReactionForceAngular(jr, n.inverse(), cPoint);
                }
                rig1.addReactionForceLinear(jr, n.inverse(), cPoint);
            }
            if (rig2.isKinematic) {
                if(!closeParalell) {
                    rig2.addReactionForceAngular(jr, n, cPoint);
                }
                rig2.addReactionForceLinear(jr, n, cPoint);
            }
            collision.setReactionImpulse(jr);
        }

    }

    /**
     * Calculate reaction impulse magnitude.
     *
     * @param rig1      rig1
     * @param rig2      rig2
     * @param cPoint    contact point
     * @param n         contact normal
     * @return          reaction impuluse magnitude.
     */
    private static double getJr(Rigidbody rig1, Rigidbody rig2, Vector2 cPoint, Vector2 n){
        Vector2 r1 = cPoint.difference(rig1.collider.getWorldCentroid()).multiply(1.0);
        Vector2 r2 = cPoint.difference(rig2.collider.getWorldCentroid()).multiply(1.0);
        Vector2 r1n = r1.rightNormal();
        Vector2 r2n = r2.rightNormal();
        double r1Mag = r1.magnitude();
        double r2Mag = r2.magnitude();
        double oneDivI1;
        if(rig1.isKinematic)
            oneDivI1 = 1/rig1.getMomentOfInertia();
        else
            oneDivI1 = 0;
        double oneDivI2;
        if(rig2.isKinematic)
            oneDivI2 = 1/rig2.getMomentOfInertia();
        else
            oneDivI2 = 0;
        double oneDivm1;
        if(rig1.isKinematic)
            oneDivm1 =  1/rig1.getMass();
        else
            oneDivm1 = 0;
        double oneDivm2;
        if(rig2.isKinematic)
            oneDivm2 =  1/rig2.getMass();
        else
            oneDivm2 = 0;

        Vector2 vp1 = rig1.getVelocity().add(rig1.getCrossRadialVelocity(cPoint));
        Vector2 vp2 = rig2.getVelocity().add(rig2.getCrossRadialVelocity(cPoint));
        Vector2 vr = vp2.difference(vp1);

        double K = oneDivm2 + oneDivm1 + (r2n.multiply((oneDivI2)*(r2n.dotProduct(n))*r2Mag).add(r1n.multiply(
                (oneDivI1)*(r1n.dotProduct(n))*r1Mag))).dotProduct(n);


        double e = (rig1.elasticity+rig2.elasticity)/2;

        double jr = (-1-e)*(vr.dotProduct(n))/K;
        return jr;
    }

    public static void resolveFrictions(ArrayList<Collision> collisions){
        for (Collision collision:collisions) {
            resolveFriction(collision);
        }
    }

    /**
     * Computes and applies frictional forces induced from collision between two rigid bodies.
     * Impulse-based friction model.
     *
     * @param collision
     */
    public static void resolveFriction(Collision collision){
        if(!collision.isCollision())
            return;
        Rigidbody rig1 = collision.getC1().gameObject.rigidbody;
        Rigidbody rig2 = collision.getC2().gameObject.rigidbody;

        Vector2 n = collision.getContactNormal();
        Vector2 cPoint;
        ArrayList<Vector2> contactManifold = collision.getContactManifold();
        if(contactManifold.size() == 2){
            cPoint = contactManifold.get(0).add(contactManifold.get(1).difference(contactManifold.get(0)).multiply(0.5));
        } else if (contactManifold.size() == 1){
            cPoint = contactManifold.get(0);
        }
        else{
            return;
        }
        Vector2 vp1 = rig1.getVelocity().add(rig1.getCrossRadialVelocity(cPoint));
        Vector2 vp2 = rig2.getVelocity().add(rig2.getCrossRadialVelocity(cPoint));
        Vector2 vr1 = vp2.difference(vp1);
        Vector2 vr2 = vp1.difference(vp2);
        Vector2 fe1 = rig1.getMomentumForce();
        Vector2 fe2 = rig2.getMomentumForce();

        Vector2 t1, t2;
        double vr1nDot = vr1.dotProduct(n);
        double vr2nDot = vr2.dotProduct(n);
        double fe1Dot = fe1.dotProduct(n);
        double fe2Dot = fe2.dotProduct(n);

        if(vr1nDot != 0){
            t1 = vr1.difference(n.multiply(vr1nDot)).normalize();
        }
        else if(fe1Dot != 0){
            t1 = fe1.difference(n.multiply(fe1Dot)).normalize();
        }
        else{
            t1 = new Vector2();
        }

        if(vr2nDot != 0){
            t2 = vr2.difference(n.multiply(vr2nDot));
            t2 = t2.normalize();
        }
        else if(fe2Dot != 0){
            t2 = fe2.difference(n.multiply(fe2Dot)).normalize();
        }
        else{
            t2 = new Vector2();
        }

        double us = (rig1.staticFriction+rig2.staticFriction)/2;
        double ud = (rig1.dynamicFriction+rig2.dynamicFriction)/2;
        //ud = 0;
        //us = 0;
        double jr = collision.getReactionImpulse();
        double js = jr*us;
        double jd = jr*ud;
        double vrt1Dot = vr1.dotProduct(t1);
        double vrt2Dot = vr2.dotProduct(t2);
        double m1 = rig1.getMass();
        double m2 = rig2.getMass();

        double jf1;
        if(vrt1Dot == 0 && vrt1Dot*m1 <= js){
            jf1 = -(m1*vrt1Dot);
        }
        else{
            jf1 = -jd;
        }

        double jf2;
        if(vrt2Dot == 0 && vrt2Dot*m2 <= js){
            jf2 = -(m2*vrt2Dot);
        }
        else{
            jf2 = -jd;
        }

        if(jf2 == 0.0 && jf1 == 0)
            return;


        boolean z1 = false, z2 = false;
        if(contactManifold.size() == 2 ){
            Vector2 rn = n.rightNormal();
            double dot1 = rn.dotProduct(rig1.getVelocity());
            double dot2 = rn.dotProduct(rig2.getVelocity());

            //This is to fix micro sliding when parallel to a surface
            if(rig1.isKinematic && Math.abs(dot1) < 0.2){
                rig1.addReactionForceLinear(dot1*rig1.getMass(), rn.inverse(), rig1.collider
                        .getWorldCentroid());
                z1 = true;
            }
            if(rig2.isKinematic && Math.abs(dot2) < 0.2){
                rig2.addReactionForceLinear(dot2*rig2.getMass(), rn.inverse(), rig2.collider
                        .getWorldCentroid());
                z2 = true;
            }
        }
        if(rig1.isKinematic && !z1) {
            rig1.addReactionForce(jf1, t1.inverse(), rig1.collider.getWorldCentroid());
            rig1.addReactionForce(jf2, t2, rig1.collider.getWorldCentroid());
        }
        if(rig2.isKinematic && !z2) {
            rig2.addReactionForce(jf2, t2.inverse(), rig2.collider.getWorldCentroid());
            rig2.addReactionForce(jf1, t1, rig2.collider.getWorldCentroid());
        }
    }
}
