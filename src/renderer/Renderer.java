package renderer;

import javax.swing.SwingUtilities;

public class Renderer {
    private Window window;

    public Renderer(Window window) {
        this.window = window;
    }

    public void renderScene(Scene scene, Camera camera) {
        SwingUtilities.invokeLater(() -> window.panel.repaint());
    }
}