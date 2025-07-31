package renderer.rendering;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import renderer.Scene;
import renderer.SceneObject;
import renderer.Camera;
import renderer.geometry.Face;


public class RenderPanel extends JPanel {
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

        List<RenderableFace> allFaces = new ArrayList<>();

        for (SceneObject obj : scene.getObjects()) {
            allFaces.addAll(obj.getRenderableFaces(camera, getWidth(), getHeight()));
        }

        allFaces.sort((a,b) -> Double.compare(b.depth, a.depth));

        for (RenderableFace rf : allFaces) {
            Face f = rf.face;
            Point2D p1 = rf.projected[f.v1];
            Point2D p2 = rf.projected[f.v2];
            Point2D p3 = rf.projected[f.v3];

            if (p1 != null && p2 != null && p3 != null) {
                int[] xPoints = {(int) p1.getX(), (int) p2.getX(), (int) p3.getX()};
                int[] yPoints = {(int) p1.getY(), (int) p2.getY(), (int) p3.getY()};

                g2d.setColor(f.color);
                g2d.fillPolygon(xPoints, yPoints, 3);

                g2d.setColor(Color.BLACK);
                g2d.drawPolygon(xPoints, yPoints, 3);
            }
        }
    }
}