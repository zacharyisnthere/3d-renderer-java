import javax.swing.JFrame;

public class Main {
    public static void main(String[] args){
        JFrame frame = new JFrame("My Swing Window");
        frame.setSize(400, 300); // 400 pixels wide, 300 pixels high
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    } 
}
