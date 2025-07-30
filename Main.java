import javax.swing.*;
import java.awt.*;
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
                System.out.println("CAUGHT ERROR: Sleep was interrupted");
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