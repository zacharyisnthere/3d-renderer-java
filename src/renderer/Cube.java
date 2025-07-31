package renderer;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import renderer.geometry.*;
import renderer.rendering.RenderableFace;

public class Cube extends SceneObject {
    //maybe there should just be a general transform class on every Scene object?
    public Point3D pos = new Point3D(0,0,0);

    private Point3D[] vertices;
    private Edge[] edges;
    private Face[] faces;


    public Cube() {
        vertices = new Point3D[] {
        new Point3D(-1, -1, -1), // 0
        new Point3D( 1, -1, -1), // 1
        new Point3D( 1,  1, -1), // 2
        new Point3D(-1,  1, -1), // 3
        new Point3D(-1, -1,  1), // 4
        new Point3D( 1, -1,  1), // 5
        new Point3D( 1,  1,  1), // 6
        new Point3D(-1,  1,  1)  // 7
        };

        edges = new Edge[] {
            new Edge(0, 1), new Edge(1, 2), new Edge(2, 3), new Edge(3, 0), //bottom square
            new Edge(4, 5), new Edge(5, 6), new Edge(6, 7), new Edge(7, 4), //top square
            new Edge(0, 4), new Edge(1, 5), new Edge(2, 6), new Edge(3, 7)  //verticals
        };

        //12 triangles (2 per cube face)
        faces = new Face[] {
            // front (-Z)
            new Face(0,1,2, Color.RED), new Face(0,2,3, Color.RED),
            // back (+Z)
            new Face(5,4,7, Color.GREEN), new Face(5,7,6, Color.GREEN),
            // left (-X)
            new Face(4,0,3, Color.BLUE), new Face(4,3,7, Color.BLUE),
            // right (+X)
            new Face(1,5,6, Color.CYAN), new Face(1,6,2, Color.CYAN),
            // bottom (-Y)
            new Face(4,5,1, Color.MAGENTA), new Face(4,1,0, Color.MAGENTA),
            // top (+Y)
            new Face(3,2,6, Color.YELLOW), new Face(3,6,7, Color.YELLOW)
        };
    }

    @Override
    public List<RenderableFace> getRenderableFaces(Camera cam, int width, int height) {
        List<RenderableFace> renderList = new ArrayList<>();

        Point2D[] projected = new Point2D[vertices.length];
        Point3D[] worldVerts = new Point3D[vertices.length];
        for (int i=0; i<vertices.length; i++) {
            worldVerts[i] = vertices[i].add(pos);
            projected[i] = cam.project(worldVerts[i], width, height);
        }

        for (Face f : faces) {
            Point3D normal = f.getNormal(vertices, pos); //compute face normal
            Point3D toCamera = cam.pos.subtract(worldVerts[f.v1]); //vector from camera to the first vertex of the face
            
            //if dot prodcut<0, face is visible
            if (normal.dot(toCamera) < 0) {
                double depth = f.getAverageDepth(vertices, pos, cam);
                renderList.add(new RenderableFace(f, projected, depth));
            }
        }

        return renderList;
    }
}