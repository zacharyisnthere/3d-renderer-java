package renderer.rendering;

import java.awt.geom.Point2D;

import renderer.geometry.Face;


public class RenderableFace {
    public Face face;
    public Point2D[] projected;
    public double depth;

    public RenderableFace(Face face, Point2D[] projected, double depth) {
        this.face = face;
        this.projected = projected;
        this.depth = depth;
    }
}