import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
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
    private boolean running;
    private final int TARGET_FPS = 60;

    public void start() {
        init();
        runLoop();
    }

    private void init() {
        window = new Window("3D Renderer", 800, 600);
        renderer = new Renderer(window);
        input = new InputManager(window);
        running = true;
    }

    private void runLoop() {
        long frameTime = 1000 / TARGET_FPS;

        while (running) {
            long start = System.currentTimeMillis();

            input.update();
            update();          // game logic
            renderer.render(); // draw to screen

            long duration = System.currentTimeMillis() - start;
            sleep(frameTime - duration); // maintains fps
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
        //update camera, objects, etc.
        //need a scene class soon
    }
}


class Window extends JFrame {
    public Window(String title, int width, int height) {
        super(title);
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}


class Renderer {
    private Window window;

    public Renderer(Window window) {
        this.window = window;
    }

    public void render() {
        //clear screen
        //project 3d coordinates to 2d based on camera
        //draw shapes with Graphics2D

        Graphics2D g = (Graphics2D) window.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0,window.getWidth(),window.getHeight());

        g.setColor(Color.RED);
        g.drawLine(100,100,200,200);
    }

    public void renderScene(Scene scene, Camera camera) {
        Graphics2D g = (Graphics)
    }
}


class InputManager {
    public InputManager(Window window) {
        //add key/mouse listeners
    }

    public void update() {
        //check which keys are down, mouse positions, etc.
    }
}   



class Scene {

}


class SceneObject {

}


class Camera {
    public Point3D pos = new Point3D(0,0,0);
    //rotation variable

    //camera variables, will be more useful later
    public double fov = 60;
    public double clip_far = 100;
    public double clip_near = 0.01;


    public Point2D project(Point3D point, int screenWidth, int screenHeight) {
        double x = point.x - pos.x;
        double y = point.y - pos.y;
        double z = point.z - pos.z;

        // projection math and stuff
        double f = 1.0 / Math.tan(Math.toRadians(fov) / 2);
        double screenX = (x / z) * f * screenWidth/2 + screenWidth/2;
        double screenY = (y / z) * f * screenHeight/2 + screenHeight/2;

        return new Point2D.Double(screenX, screenY);
    }
    
}


class Cube extends SceneObject {
    // 8xyz vertices, list of edges
    // render method the projects 3d to 2d based on a given Camera position

    public Point3D pos = new Point3D(0,0,0);
    //maybe there should just be a general transform class on every Scene object?

    private Point3D[] vertices;


    public Cube() {
        vertices = new Point3D[] {
            new Point3D(-1, -1, -1),
            new Point3D( 1, -1, -1),
            new Point3D( 1,  1, -1),
            new Point3D(-1,  1, -1),
            new Point3D(-1, -1,  1),
            new Point3D( 1, -1,  1),
            new Point3D( 1,  1,  1),
            new Point3D(-1,  1,  1)
        };
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