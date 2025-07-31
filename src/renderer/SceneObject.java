package renderer;

import java.util.List;

import renderer.Camera;
import renderer.rendering.RenderableFace;

// Polymorphism!
// abstract means it cannot be instantiated on its own, which is important.
// abstract on a method requires every class that extends this one to provide its own implementation of this method.

//change from rendering every face on the object to making a list of the renderable faces and exporting it to Renderer
public abstract class SceneObject {
    public abstract List<RenderableFace> getRenderableFaces(Camera camera, int width, int height);
    // public abstract void render(Graphics2D g, Camera camera, int width, int height);
}