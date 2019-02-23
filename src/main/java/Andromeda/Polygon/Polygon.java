package Andromeda.Polygon;

import Andromeda.Collision.LineCollision.Edge;
import Andromeda.Collision.LineCollision.EdgeCollision;
import Andromeda.Collision.Projection;
import Andromeda.Collision.Transform;
import Andromeda.Utils.Matrix_2x2;
import Andromeda.Utils.Vector2;

import java.awt.*;
import java.util.ArrayList;

/**
 * Classname: Polygon
 * Author: Elias Jätte
 * Date: 18/08/04
 *
 * Class representing a polygon
 */
public class Polygon {
    private Vector2[] vertices;
    private Edge[] edges;
    private ArrayList<Vector2> minimizedNormals;

    public Polygon(Vector2[] vertices){
        initiate(vertices);
    }

    public Polygon(ArrayList<Vector2> vertices){
        Vector2[] vs = new Vector2[vertices.size()];
        for(int i = 0; i < vs.length; i++){
            vs[i] = vertices.get(i);
        }
        initiate(vs);
    }

    public Polygon(double[] vertices){
        initiate(verticesFromValues(vertices));
    }

    private void initiate(Vector2[] vertices){
        this.vertices = vertices;
        this.edges = createEdges();
        this.minimizedNormals = minimizeNormals();
    }

    private ArrayList<Vector2> minimizeNormals(){
        ArrayList<Vector2> minNormals = new ArrayList<>(edges.length);
        if(vertices.length < 2)
            return minNormals;
        Vector2[] normals = new Vector2[edges.length];
        for(int i = 0; i < edges.length; i++){
            normals[i] = edges[i].getLeftNormal();
        }

        for(int i = 0; i < edges.length; i++){
            if(normals[i] == null)
                continue;
            minNormals.add(normals[i]);
            for(int j = i+1; j < edges.length; j++){
                if(normals[j] == null)
                    continue;
                if(Math.abs(normals[i].dotProduct(normals[j])) == 1.0){
                    normals[j] = null;
                }
            }
        }
        return minNormals;
    }

    private Vector2[] verticesFromValues(double[] verticeValues){
        Vector2[] vertices = new Vector2[verticeValues.length/2];
        for(int i = 0; i < vertices.length; i++){
            vertices[i] = new Vector2(verticeValues[i*2],verticeValues[i*2+1]);
        }
        return vertices;
    }

    private Edge[] createEdges(){
        Edge[] edges = new Edge[vertices.length];
        Vector2 lastVertice = vertices[0];
        int lastI = 0;
        for(int i = 1; i <= vertices.length; i++){
            Vector2 currentVertice = vertices[i%vertices.length];
            edges[i%vertices.length] = new Edge(lastVertice,currentVertice,lastI%vertices.length, i%vertices.length);
            lastVertice = currentVertice;
            lastI = i;
        }
        return edges;
    }

    /**
     * Project polygon onto vector.
     *
     * @param p         vector to project onto
     * @param transform transform.
     *
     * @return  projection.
     */
    public Projection project(Vector2 p, Transform transform){
        double min = vertices[0].multiply(transform).dotProduct(p);
        double max = min;
        for (Vector2 v:vertices) {
            double projection = v.multiply(transform).dotProduct(p);
            if(projection < min)
                min = projection;
            else if(projection > max)
                max = projection;
        }
        return new Projection(min,max);
    }

    public ArrayList<Vector2> getMinimizedNormals() {
        return minimizedNormals;
    }

    public Vector2[] getVertices() {
        return vertices;
    }

    public Edge[] getEdges() {
        return edges;
    }

    public void drawPolygon(Graphics g, Transform transform){
        if(vertices.length == 1){
            Vector2 point = vertices[0].multiply(transform);
            g.fillOval((int) point.getX() - 4, (int) point.getY() - 4, 8, 8);
        } else {
            for (Edge e : edges) {
                e.drawEdge(g, transform);
            }
        }
    }

    public void drawNormals(Graphics g, Transform transform){
        for (Edge e:edges) {
            e.drawLeftNormal(g,transform);
        }
    }

    /**
     * Split polygon along specified edge.
     *
     * @param e edge to split along
     *
     * @return  new Polygons.
     */
    public ArrayList<Polygon> split(Edge e){
        ArrayList<EdgeCollision> edgeCollisions = new ArrayList<>();
        for(int i = 0; i < edges.length; i++){
            EdgeCollision l = EdgeCollision.intersect(e,edges[i]);
            if(l.intersect()){
                edgeCollisions.add(l);
            }
        }
        ArrayList<Polygon> polygons = new ArrayList<>();
        if(edgeCollisions.size() == 2){
            EdgeCollision l1 = edgeCollisions.get(0);
            EdgeCollision l2 = edgeCollisions.get(1);
            Vector2 v1 = l1.getIntersect();
            Vector2 v2 = l2.getIntersect();
            int p1_1 = l1.getE2().getP1Indice();
            int p2_1 = l1.getE2().getP2Indice();
            int p1_2 = l2.getE2().getP1Indice();
            int p2_2 = l2.getE2().getP2Indice();

            ArrayList<Vector2> p1 = new ArrayList<>();
            p1.add(v1);
            for(int i = 0; i < (vertices.length+(p2_2-p2_1))%vertices.length; i++){
                p1.add(vertices[(i+p2_1)%vertices.length]);
            }
            p1.add(v2);

            ArrayList<Vector2> p2 = new ArrayList<>();
            p2.add(v2);
            for(int i = 0; i < (vertices.length+(p2_1-p2_2))%vertices.length; i++){
                p2.add(vertices[(i+p2_2)%vertices.length]);
            }
            p2.add(v1);

            polygons.add(new Polygon(p1));
            polygons.add(new Polygon(p2));
        }
        else{
            polygons.add(this);
        }
        return polygons;
    }

    //Premade polygons

    public static Polygon square(){
        return new Polygon(new double[]{0.5,0.5, 0.5,-0.5, -0.5,-0.5, -0.5,0.5});
    }

    public static Polygon hexagon(){
        return new Polygon(new double[]{0,0.5, 0.4330125,0.25, 0.4330125,-0.25, 0,-0.5, -0.4330125,-0.25, -0.4330125,0.25});
    }

    public static Polygon point(){
        return new Polygon(new double[]{0,0});
    }

    public static Polygon equilateralTriangle(){
        return new Polygon(new double[]{0,1, 0.8660254037844386,-0.5, -0.8660254037844386,-0.5});
    }

    public static Polygon regularPolygon(int sides){
        if(sides < 3)
            sides = 3;
        Vector2 vert[] = new Vector2[sides];
        Matrix_2x2 matrix_2x2 = new Matrix_2x2().rotate_degree(-360.0/sides);
        Vector2 v = new Vector2(0,0.5);
        for(int i = 0; i < sides; i++){
            vert[i] = v;
            v = v.multiply(matrix_2x2);
        }
        return new Polygon(vert);
    }

    public String toString(){
        String s = "";
        for (Vector2 v:vertices) {
            s += v.toString()+"\n";
        }
        return s;
    }
}