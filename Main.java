import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.JComponent;

import java.awt.desktop.ScreenSleepEvent; //can I remove this?

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

        scene.add(new Cube());
        running = true;
    }

    private void runLoop() {
        long frameTime = 1000 / TARGET_FPS;

        while (running) {
            long start = System.currentTimeMillis();

            update();
            renderer.renderScene(scene, camera);

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

    private void update() {
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
        setBackground(Color.BLACK);
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

    public void setObjects(java.util.List<SceneObject> new_objects) {
        this.objects = new_objects;
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
    public Point3D pos = new Point3D(5,3,-15);
    //rotation variable

    //camera variables, will be more useful later
    public double fov = 60;
    public double clip_far = 100;
    public double clip_near = 0.01;


    public Point2D project(Point3D point, int screenWidth, int screenHeight) {
        double x = point.x - pos.x;
        double y = point.y - pos.y;
        double z = point.z - pos.z;

        if (z<=clip_near) return null;

        // projection math and stuff. screenY is flipped because 0,0 is top left which is odd.
        // f is the vertical fov, so when the screen isn't square the horizontal scaling is weird.
        // by dividing f by the ratio of screen width/height, the screenX value is corrected.

        double aspect = (double) screenWidth / screenHeight;
        double f = 1.0 / Math.tan(Math.toRadians(fov) / 2);
        
        double screenX = (x / z) * f/aspect * screenWidth/2 + screenWidth/2;
        double screenY = (-y / z) * f * screenHeight/2 + screenHeight/2;

        return new Point2D.Double(screenX, screenY);
    }
    
}


class Cube extends SceneObject {
    //maybe there should just be a general transform class on every Scene object?
    public Point3D pos = new Point3D(0,0,0);

    private Point3D[] vertices;
    private Edge[] edges;


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
    }

    @Override
    public void render(Graphics2D g, Camera cam, int width, int height) {
        //project all vertices first
        Point2D[] projected = new Point2D[vertices.length];
        for (int i=0; i<vertices.length; i++) {
            Point3D world_v = vertices[i].add(pos);
            projected[i] = cam.project(world_v, width, height);
        }

        //draw edges
        g.setColor(Color.WHITE);
        for (Edge e : edges) {
            Point2D p1 = projected[e.start];
            Point2D p2 = projected[e.end];

            if (p1!=null && p2!=null) {
                g.drawLine( (int) p1.getX(), (int) p1.getY(), 
                            (int) p2.getX(), (int) p2.getY());
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