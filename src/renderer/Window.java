package renderer;

import javax.swing.JFrame;

import renderer.Scene;
import renderer.Camera;
import renderer.rendering.RenderPanel;


public class Window extends JFrame {
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