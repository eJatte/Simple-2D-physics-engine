package Andromeda.Polygon;

import Andromeda.Utils.Matrix_2x2;
import Andromeda.Utils.Vector2;

/**
 * Classname: PolygonGeometry
 * Author: Elias Jätte
 * Date: 18/08/13
 *
 * Methods for polygon geometry.
 */
public class PolygonGeometry {
    private Vector2 centroid;
    private double area;

    public PolygonGeometry(Vector2 centroid, double area) {
        this.centroid = centroid;
        this.area = area;
    }

    /**
     * The centroid or center of mass of the polygon relative to the polygon origin.
     * @return
     */
    public Vector2 getCentroid() {
        return centroid;
    }

    public Vector2 getCentroid(Matrix_2x2 matrix){
        return centroid.multiply(matrix);
    }

    /**
     * The area of the polygon
     * @return
     */
    public double getArea() {
        return area;
    }

    public double getArea(Matrix_2x2 matrix){
        double s = Math.sqrt(area);
        double newArea = matrix.getI().multiply(s).magnitude()*matrix.getJ().multiply(s).magnitude();
        return newArea;
    }

    public static PolygonGeometry getPolygonGeometry(Polygon p){
        Vector2[] vertices = p.getVertices();

        if(vertices.length == 1){
            return new PolygonGeometry(vertices[0],0);
        }

        double area = 0;
        for(int i = 0; i < vertices.length; i++){
            Vector2 v1 = vertices[i];
            Vector2 v2 = vertices[(i+1)%vertices.length];
            area += (v1.getX()*v2.getY()-v2.getX()*v1.getY());
        }
        area = area/2;

        Vector2 centroid = new Vector2(0,0);
        for(int i = 0; i < vertices.length; i++){
            Vector2 v1 = vertices[i];
            Vector2 v2 = vertices[(i+1)%vertices.length];
            centroid = centroid.add(v1.add(v2).multiply(v1.getX()*v2.getY()-v2.getX()*v1.getY()));
        }
        centroid = centroid.multiply(1/(6*area));
        return new PolygonGeometry(centroid,Math.abs(area));
    }
}
