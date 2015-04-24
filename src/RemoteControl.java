import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by matiaskaskimies on 28/01/15.
 */
public class RemoteControl extends KeyAdapter {

        RemotePlayer game;

        public RemoteControl (RemotePlayer game) {
            this.game = game;
        }

        public void keyPressed(KeyEvent e) {
            game.keyPressed(e);
        }

        public void keyRelease(KeyEvent e) {
            //game.keyRelease(e);
        }

}
