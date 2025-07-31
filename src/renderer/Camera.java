package renderer;

import renderer.geometry.Point3D;
import java.awt.geom.Point2D;


public class Camera {
    public Point3D pos = new Point3D(0,3,-15);
    //rotation done with radians
    public double yaw = 0;      // rotation around global y-axis
    public double pitch = 0;    // rotation around local x-axis
    
    public double fov = 60;
    public double clipFar = 100;
    public double clipNear = 0.01;


    public Point2D project(Point3D point, int screenWidth, int screenHeight) {
        Point3D camSpace = toCameraSpace(point);

        if (camSpace.z <= clipNear || camSpace.z >= clipFar) return null;

        double aspect = (double) screenWidth / screenHeight;
        double f = 1.0 / Math.tan(Math.toRadians(fov) / 2);
        
        double screenX = (camSpace.x / camSpace.z) * f/aspect * screenWidth/2 + screenWidth/2;
        double screenY = (-camSpace.y / camSpace.z) * f * screenHeight/2 + screenHeight/2;

        return new Point2D.Double(screenX, screenY);
    }

    public Point3D toCameraSpace(Point3D world) {
        double relX = world.x - pos.x;
        double relY = world.y - pos.y;
        double relZ = world.z - pos.z;

        double cosYaw = Math.cos(yaw), sinYaw = Math.sin(yaw);
        double rotX = cosYaw * relX - sinYaw * relZ;
        double rotZ1 = sinYaw * relX + cosYaw * relZ;

        double cosPitch = Math.cos(pitch), sinPitch = Math.sin(pitch);
        double rotY = cosPitch * relY - sinPitch * rotZ1;
        double rotZ = sinPitch * relY + cosPitch * rotZ1;

        return new Point3D(rotX, rotY, rotZ);

        // projection math and stuff. screenY is flipped because 0,0 is top left which is odd.
        // f is the vertical fov, so when the screen isn't square the horizontal scaling is weird.
        // by dividing f by the ratio of screen width/height, the screenX value is corrected.
    }    
}
