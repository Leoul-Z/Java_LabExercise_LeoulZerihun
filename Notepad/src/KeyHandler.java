import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;

public class KeyHandler implements KeyListener {

    Main main;

    public KeyHandler(Main main){
        this.main= main;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.isControlDown() &&  e.getKeyCode()== KeyEvent.VK_S ){
            main.File.save();
        } else if (e.isControlDown() &&  e.getKeyCode()== KeyEvent.VK_N ) {
            main.File.newFile();
        }else if (e.isControlDown() &&  e.getKeyCode()== KeyEvent.VK_O ) {
            main.File.open();
        }else if (e.isControlDown() &&  e.getKeyCode()== KeyEvent.VK_Z ) {
            main.Edit.Undo();
        }else if (e.isControlDown() &&  e.getKeyCode()== KeyEvent.VK_Y ) {
            main.Edit.Redo();
        }else if (e.isControlDown() &&  e.getKeyCode()== KeyEvent.VK_X ) {
            main.textArea.cut();
        }else if (e.isControlDown() &&  e.getKeyCode()== KeyEvent.VK_C ) {
            main.textArea.copy();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
