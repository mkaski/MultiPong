import java.awt.*;

/**
 * Created by matiaskaskimies on 27/01/15.
 */


public class Ball extends Rectangle{

    private final int width = 15, height = 15;
    private int x,y;
    private int speed = 6;
    private int vX = -speed, vY = -speed;
    private Racket serverRacket;
    private Racket remoteRacket;


    private int serverScore = 0;
    private int remoteScore = 0;

    /*
        Constructor:
        Make new ball to x,y coorinates
        take serverRacket and remoteRacket as parameters for collision detection
     */

    public Ball(int x, int y, Racket serverRacket, Racket remoteRacket){

        this.serverRacket = serverRacket;
        this.remoteRacket = remoteRacket;

        this.x = x;
        this.y = y;

        setBounds(x, y, width, height);
    }

    /*
        Tick method is called 30 per sec
        updates the ball location by calling move() and rectangle bounds
     */
    public void tick() {
        setBounds(x, y, width, height);
        move();
    }

    /*
        increase(or decrease) x and y coordinate by vX and vY to make the ball move

     */
    public void move() {
        if (y <= 0)
            vY = speed;
        if (y >= 400 - height)
            vY = -speed;
        if (x <= 0) { // left side

            // commented, because no need to bounce back
            // vX = speed;
            remoteScore++;
            resetBall();
        }
        if (x >= 600 - width) { // right side
            // commented, because no need to bounce back
            //vX = -speed;
            serverScore++;
            resetBall();
        }
        x += vX;
        y += vY;

        // Check collision with racket

        if (this.intersects(serverRacket) || serverRacket.intersects(this)) {
            vX = speed;
        }
        if (this.intersects(remoteRacket) || remoteRacket.intersects(this)) {
            vX = -speed;
        }
    }
    /*
        Render graphics
     */
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, width, height);
    }

    public int getServerScore() {
        return this.serverScore;
    }

    public int getRemoteScore() {
        return this.remoteScore;
    }
    /*
        Reset ball to middle after score
     */
    public void resetBall() {
        this.x = 250;
        this.y = 250;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getBallX() {
        return x;
    }
    public int getBallY() {
        return y;
    }

}
