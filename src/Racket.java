import java.awt.*;

/**
 * Created by matiaskaskimies on 27/01/15.
 */
public class Racket extends Rectangle {

    private int y,x;

    // Racket's movement speed of one press
    private int speed = 18;

    // dimensions
    private final int WIDTH = 15;
    private final int HEIGHT = 60;

    /*
        Constructor, make racket to parameter coordinates and set bounds for Rectangle for collision detection
     */
    public Racket (int x, int y) {
        this.x = x;
        this.y = y;
        setBounds(x, y, WIDTH, HEIGHT);
    }

    /*
        Tick
     */
    //public void tick() {
      //  setBounds(x, y, WIDTH, HEIGHT);
    //}

    /*
        Render graphics
        Draw rectangle based on Rectangle object's bounds, so that collision area and graphics match
     */
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRect((int)this.getBounds().getX(), (int)this.getBounds().getY(), WIDTH, HEIGHT);
    }

    /*
        Move racket up
     */
    public void moveUp() {
        if (y > 5) {
            y -= speed;
        }
    }

    /*
        Move racket down
     */

    public void moveDown() {
        if (y < 340) {
            y +=speed;
        }
    }

    /*
        Get Racket's y position
     */

    public int getRacketY() {
        return y;
    }

    /*
        Set Racket's y position
     */
    public void setY(int y) {
        this.y = y;
    }


}
