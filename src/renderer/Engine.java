package renderer;

import java.awt.event.KeyEvent;
import renderer.geometry.*;
import renderer.rendering.*;

public class Engine {
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