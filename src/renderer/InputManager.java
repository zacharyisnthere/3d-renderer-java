package renderer;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

// implements means InputManager needs to provide all methods declared in interface
public class InputManager implements KeyListener {
    private final Set<Integer> pressedKeys = new HashSet<>();

    public InputManager(Window window) {
        //add key/mouse listeners
        window.addKeyListener(this);
        window.setFocusable(true);
        window.requestFocusInWindow();
    }
    
    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}   