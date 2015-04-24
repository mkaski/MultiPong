import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by matiaskaskimies on 27/01/15.
 */
public class Control extends KeyAdapter {

    Game game;

    public Control (Game game) {
        this.game = game;
    }

    public void keyPressed(KeyEvent e) {
        game.keyPressed(e);
    }

    public void keyRelease(KeyEvent e) {
        //game.keyRelease(e);
    }

}