import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.JComponent;

public class Main {
    public static void main(String[] args) {
        Engine engine = new Engine();
        engine.start();        
    }
}


class Engine {
    private Window window;
    private Renderer renderer;
    private InputManager input;
    private Scene scene;
    private Camera camera;

    private boolean running;
    private final int TARGET_FPS = 60;

    public void start() {
        init();
        new Thread(this::runLoop).start();
    }

    private void init() {
        scene = new Scene();
        camera = new Camera();
        window = new Window("3D Renderer", 800, 600, scene, camera);
        renderer = new Renderer(window);
        input = new InputManager(window);

        Cube cube1 = new Cube();
        Cube cube2 = new Cube();

        cube2.pos.x = 5;

        scene.add(cube1);
        scene.add(cube2);
        running = true;
    }

    private void runLoop() {
        long frameTime = 1000 / TARGET_FPS;
        long lastFrameTime = System.currentTimeMillis();

        while (running) {
            long start = System.currentTimeMillis();
            double delta = (start - lastFrameTime) / 1000.0;
            delta = Math.min(delta, 0.05); //cap at ~20fps 

            update(delta);
            renderer.renderScene(scene, camera);

            lastFrameTime = start;
            long duration = System.currentTimeMillis() - start;
            sleep(frameTime - duration);
        }
    }

    private void sleep(long ms) {
        if (ms > 0) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                // restore the interrupted status so higher-level code can handle it
                Thread.currentThread().interrupt();
                System.out.println("Sleep was interrupted");
            }
        }
    }

    private void update(double delta) {
        double moveSpeed = 7;
        double rotSpeed = .9;

        //movement (relative to yaw)
        double forwardX = Math.sin(camera.yaw);
        double forwardZ = Math.cos(camera.yaw);

        if (input.isKeyPressed(KeyEvent.VK_W)) {
            camera.pos.x += forwardX * moveSpeed * delta;
            camera.pos.z += forwardZ * moveSpeed * delta;
        }
        if (input.isKeyPressed(KeyEvent.VK_S)) {
            camera.pos.x -= forwardX * moveSpeed * delta;
            camera.pos.z -= forwardZ * moveSpeed * delta;
        }
        if (input.isKeyPressed(KeyEvent.VK_A)) {
            camera.pos.x -= forwardZ * moveSpeed * delta;
            camera.pos.z += forwardX * moveSpeed * delta;
        }
        if (input.isKeyPressed(KeyEvent.VK_D)) {
            camera.pos.x += forwardZ * moveSpeed * delta;
            camera.pos.z -= forwardX * moveSpeed * delta;
        }
        if (input.isKeyPressed(KeyEvent.VK_Q)) {
            camera.pos.y -= moveSpeed * delta;
        }
        if (input.isKeyPressed(KeyEvent.VK_E)) {
            camera.pos.y += moveSpeed * delta;
        }

        // Rotation
        if (input.isKeyPressed(KeyEvent.VK_LEFT))  camera.yaw -= rotSpeed * delta;
        if (input.isKeyPressed(KeyEvent.VK_RIGHT)) camera.yaw += rotSpeed * delta;
        if (input.isKeyPressed(KeyEvent.VK_UP))    camera.pitch += rotSpeed * delta;
        if (input.isKeyPressed(KeyEvent.VK_DOWN))  camera.pitch -= rotSpeed * delta;

        if (input.isKeyPressed(KeyEvent.VK_SPACE)) {
            System.out.println("Space pressed");
        }
    }
}


class Window extends JFrame {
    public RenderPanel panel;

    public Window(String title, int width, int height, Scene scene, Camera camera) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new RenderPanel(scene, camera);
        add(panel);
        setVisible(true);
    }
}


class RenderPanel extends JPanel {
    private Scene scene;
    private Camera camera;

    public RenderPanel(Scene scene, Camera camera) {
        this.scene = scene;
        this.camera = camera;
        setBackground(Color.GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        for (SceneObject obj : scene.getObjects()) {
            obj.render(g2d, camera, getWidth(), getHeight());
        }
    }
}


class Renderer {
    private Window window;

    public Renderer(Window window) {
        this.window = window;
    }

    public void renderScene(Scene scene, Camera camera) {
        SwingUtilities.invokeLater(() -> window.panel.repaint());

        // Graphics2D g = (Graphics2D) window.getGraphics();

        // //clear screen
        // g.setColor(Color.BLACK);
        // g.fillRect(0,0,window.getWidth(),window.getHeight());

        // //draw stuff
        // g.setColor(Color.WHITE);
        // for (SceneObject obj : scene.getObjects()) {
        //     obj.render(g, camera, window.getWidth(), window.getHeight());
        // }
    }
}

// implements means InputManager needs to provide all methods declared in interface
class InputManager implements KeyListener {
    private final Set<Integer> pressedKeys = new HashSet<>();

    public InputManager(Window window) {
        //add key/mouse listeners
        window.addKeyListener(this);
        window.setFocusable(true);
        window.requestFocusInWindow();
    }
    
    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}   



class Scene {
    private java.util.List<SceneObject> objects = new java.util.ArrayList<>();

    public java.util.List<SceneObject> getObjects(){
        return objects;
    }

    public void setObjects(java.util.List<SceneObject> newObjects) {
        this.objects = newObjects;
    }

    public void add(SceneObject o) {
        objects.add(o);
    }

    public void remove(SceneObject o) {
        objects.remove(o);
    } 
}


// Polymorphism!
// abstract means it cannot be instantiated on its own, which is important.
abstract class SceneObject {
    // abstract on a method requires every class that extends this one to provide its own implementation of this method.
    public abstract void render(Graphics2D g, Camera camera, int width, int height);
}


class Camera {
    public Point3D pos = new Point3D(0,3,-15);
    //rotation done with radians
    public double yaw = 0;      // rotation around global y-axis
    public double pitch = 0;    // rotation around local x-axis
    
    public double fov = 60;
    public double clipFar = 100;
    public double clipNear = 0.01;


    public Point2D project(Point3D point, int screenWidth, int screenHeight) {
        //translate point relative to camera
        double relX = point.x - pos.x;
        double relY = point.y - pos.y;
        double relZ = point.z - pos.z;

        //apply yaw rotation (global y axis)
        double cosYaw = Math.cos(yaw), sinYaw = Math.sin(yaw);
        double rotX = cosYaw * relX - sinYaw * relZ;
        double rotZ1 = sinYaw * relX + cosYaw * relZ;

        //apply pitch rotatoin (local x axis)
        double cosPitch = Math.cos(pitch), sinPitch = Math.sin(pitch);
        double rotY = cosPitch * relY - sinPitch * rotZ1;
        double rotZ = sinPitch * relY + cosPitch * rotZ1;

        if (rotZ<=clipNear) return null;
        if (rotZ>=clipFar) return null;

        // projection math and stuff. screenY is flipped because 0,0 is top left which is odd.
        // f is the vertical fov, so when the screen isn't square the horizontal scaling is weird.
        // by dividing f by the ratio of screen width/height, the screenX value is corrected.

        double aspect = (double) screenWidth / screenHeight;
        double f = 1.0 / Math.tan(Math.toRadians(fov) / 2);
        
        double screenX = (rotX / rotZ) * f/aspect * screenWidth/2 + screenWidth/2;
        double screenY = (-rotY / rotZ) * f * screenHeight/2 + screenHeight/2;

        return new Point2D.Double(screenX, screenY);
    }
    
}


class Cube extends SceneObject {
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
    public void render(Graphics2D g, Camera cam, int width, int height) {
        //project all vertices first
        Point2D[] projected = new Point2D[vertices.length];
        for (int i=0; i<vertices.length; i++) {
            Point3D worldV = vertices[i].add(pos);
            projected[i] = cam.project(worldV, width, height);
        }

        // //draw edges
        // g.setColor(Color.WHITE);
        // for (Edge e : edges) {
        //     Point2D p1 = projected[e.start];
        //     Point2D p2 = projected[e.end];

        //     if (p1!=null && p2!=null) {
        //         g.drawLine( (int) p1.getX(), (int) p1.getY(), 
        //                     (int) p2.getX(), (int) p2.getY());
        //     }
        // }

        //draw faces
        for (Face f : faces) {
            Point2D p1 = projected[f.v1];
            Point2D p2 = projected[f.v2];
            Point2D p3 = projected[f.v3];
            if (p1!=null && p2!=null && p3!=null) {
                int[] xPoints = {(int)p1.getX(), (int)p2.getX(), (int)p3.getX()};
                int[] yPoints = {(int)p1.getY(), (int)p2.getY(), (int)p3.getY()};
                g.setColor(f.color);
                g.fillPolygon(xPoints, yPoints, 3);
                g.setColor(Color.BLACK);
                g.drawPolygon(xPoints, yPoints, 3);
            }
        }
    }
}




class Point3D {
    public double x;
    public double y;
    public double z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //copy constructor, used for duplicating the properties of exising points.
    public Point3D(Point3D p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    //add, subtract, scale functions for convenince

    public Point3D add(Point3D other) {
        return new Point3D(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Point3D subtract(Point3D other) {
        return new Point3D(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Point3D scale(double factor) {
        return new Point3D(this.x * factor, this.y * factor, this.z * factor);
    }

    public Point3D cross(Point3D other) {
        return new Point3D(
            this.y * other.z - this.z * other.y,
            this.z * other.x - this.x * other.z,
            this.x * other.y - this.y * other.x
        );
    }

    public double dot(Point3D other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Point3D normalize() {
        double len = Math.sqrt(x*x + y*y + z*z);
        return new Point3D(x/len, y/len, z/len);
    }

    //overridden toString function for good debugging later on
    @Override
    public String toString() {
        return "Point3D(" + x + ", " + y + ", " + z + ")";
    }
}


class Edge {
    // the int start and end refer to the specific points in the mesh array.
    public int start;
    public int end;

    public Edge(int start, int end) {
        this.start = start;
        this.end = end;
    }
}


class Face {
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
}