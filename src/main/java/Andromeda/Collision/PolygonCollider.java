package Andromeda.Collision;

import Andromeda.Collision.LineCollision.Edge;
import Andromeda.GameObject.GameObject;
import Andromeda.Polygon.PolygonGeometry;
import Andromeda.Render.Drawable;
import Andromeda.Utils.Matrix_2x2;
import Andromeda.Utils.Vector2;
import Andromeda.Polygon.Polygon;

import java.awt.*;
import java.util.ArrayList;

/**
 * Classname: PolygonCollider
 * Author: Elias Jätte
 * Date: 18/07/28
 *
 * Class for a polygon collider.
 */
public class PolygonCollider extends Collider implements Drawable{
    private Polygon polygon;
    public CollisionClip collisionClip;
    public double m = 1.0;
    private PolygonGeometry pg;

    public PolygonCollider(Polygon polygon, GameObject gameObject){
        super(gameObject);
        this.polygon = polygon;
        pg = PolygonGeometry.getPolygonGeometry(polygon);

    }

    public Vector2[] getNormals(){
        ArrayList<Vector2> preNormals = polygon.getMinimizedNormals();
        Vector2 normals[] = new Vector2[preNormals.size()];
        for(int i = 0; i < preNormals.size(); i++){
            normals[i] = preNormals.get(i).multiply(this.getTransform().getMatrix()).normalize();
        }
        return normals;
    }

    public Vector2[] getVertices(){
        Vector2 preVertices[] = polygon.getVertices();
        Vector2 vertices[] = new Vector2[preVertices.length];
        for(int i = 0; i < preVertices.length; i++){
            vertices[i] = preVertices[i].multiply(this.getTransform().getMatrix()).add(this.getTransform().getPosition());
        }
        return vertices;
    }

    public Vector2 getWorldCentroid() {
        return getCentroid().add(getTransform().getPosition());
    }

    public Vector2 getCentroid() {
        return pg.getCentroid(getTransform().getMatrix());
    }

    public double getArea() {
        return pg.getArea(getTransform().getMatrix());
    }

    public Polygon getPolygon(){
        return polygon;
    }

    public Projection getProjection(Vector2 p){
        return polygon.project(p,getTransform());
    }

    public Edge[] getEdges(){
        Edge edges[] = new Edge[polygon.getEdges().length];
        for(int i = 0; i < edges.length; i++){
            edges[i] = polygon.getEdges()[i].multiply(getTransform());
        }
        return edges;
    }

    @Override
    public void draw(Graphics g, Transform transform) {
        if(!isEnabled)
            return;
        Transform t = this.getTransform();
        Color color = this.getColor();
        Polygon polygon = this.polygon;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));

        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 255));

        polygon.drawPolygon(g,t.multiply(transform));

        //t.drawTransformArrow(g,transform);

    }

    @Override
    public int getLayer() {
        return 1;
    }
}
