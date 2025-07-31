package renderer.geometry;

import java.awt.Color;
import renderer.Camera;

public class Face {
    public int v1, v2, v3;
    public Color color;

    public Face(int v1, int v2, int v3, Color color) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = color;
    }

    public Point3D getNormal(Point3D[] vertices, Point3D pos) {
        Point3D p1 = vertices[v1].add(pos);
        Point3D p2 = vertices[v2].add(pos);
        Point3D p3 = vertices[v3].add(pos);

        Point3D u = p2.subtract(p1);
        Point3D v = p3.subtract(p1);

        return u.cross(v).normalize();
    }

    public double getAverageDepth(Point3D[] vertices, Point3D pos, Camera cam) {
        Point3D p1 = cam.toCameraSpace(vertices[v1].add(pos));
        Point3D p2 = cam.toCameraSpace(vertices[v2].add(pos));
        Point3D p3 = cam.toCameraSpace(vertices[v3].add(pos));      

        return (p1.z + p2.z + p3.z) / 3.0;
    }
}