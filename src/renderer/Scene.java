package renderer;

import java.util.List;
import java.util.ArrayList;

public class Scene {
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