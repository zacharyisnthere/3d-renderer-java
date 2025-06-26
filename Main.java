import javax.swing.*;
import java.awt.*;
import javax.swing.JComponent;

class ShapeDrawing extends JComponent {
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawRect(100, 150, 60, 100);
    }
}

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame("My Swing Window");
        frame.setSize(400, 300); // 400 pixels wide, 300 pixels high
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ShapeDrawing());
        frame.setVisible(true);

        // Graphics.drawPolygon(new int[] {10, 20, 30}, new int[] {100, 20, 100}, 3);
    } 
}
