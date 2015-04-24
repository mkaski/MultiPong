import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

/**
 * Created by matiaskaskimies on 27/01/15.
 */
public class Game extends Canvas implements Runnable, KeyListener {

    // racket's x coordinates
    private final int SERVERX = 40;
    private final int REMOTEX = 540;

    // initialize objectinputstreams for socket communication
    private ObjectInputStream objectInputStream = null;
    private ObjectOutputStream objectOutputStream = null;
    // make rackets to default racket positions
    private Racket serverRacket = new Racket(SERVERX,200);
    private Racket remoteRacket = new Racket(REMOTEX,200);
    // make new ball to default position
    private Ball ball = new Ball(450,450,serverRacket,remoteRacket);

    // background image
    private BufferedImage image = new BufferedImage(256,256, BufferedImage.TYPE_INT_RGB);

    public Game(Socket sock) {

        try {
            // set output and input streams
            sock.setSoTimeout(5000);
            OutputStream outputStream = sock.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.flush();

            InputStream inputStream = sock.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);

        } catch (IOException e) {
            System.err.print(e);
            try {
               sock.close();
                System.exit(1);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        // make frame

        this.setSize(600,400);
        JFrame frame = new JFrame("Pong Server");
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.requestFocus();

        // start game thread and sleep for some time so jframe has time to load
        Thread thread = new Thread(this);
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    initialize
     */
    public void init() {
        addKeyListener(new Control(this));
    }

    /*
        Thread run method, the game loop with 30 fps updating
    */

    public void run() {

        // frame system
        long lastTime = System.nanoTime();
        double frameRate = 30.0;
        double ns = 1000000000 / frameRate;
        double delta = 0;
        init();

        boolean running = true;

        // the game loop
        while(running) {

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            // kutsu tickiä
            if (delta >= 1) {
                tick();
                delta--;
            }
            render();
        }

    }

    /*
        Tick is called 30 per sec
        1. Read remoteplayer's racket y
        2. Set server's local remoteracket's y position to y got from remoteplayer and new bounds
        3. Set server's racket's bounds to new bounds
        4. call ball's tick method
        5. update ball's and serverracket's position to outputstream
     */

    public void tick() {

        try {
            // read remoteplayer's racket
            int clientY = objectInputStream.readInt();
            remoteRacket.setY(clientY);
            remoteRacket.setBounds(REMOTEX, clientY, 15, 60);
            serverRacket.setBounds(SERVERX, serverRacket.getRacketY(), 15, 60);
            ball.tick();

            // update state to outputstream
            // ball's x, balls'x, serverRacket's y, serverScore
            objectOutputStream.writeUTF(ball.getBallX() + "," + ball.getBallY() + "," + serverRacket.getRacketY() + "," + ball.getServerScore() + "," + ball.getRemoteScore());
            objectOutputStream.flush();

            //System.out.println(ball.getBallX() + "," + ball.getBallY() + "," + serverRacket.getLocation());

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /*
        Render graphics
        render ball, serverRacket and remoteRacket
     */
    public void render() {

        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        ball.render(g);
        serverRacket.render(g);
        remoteRacket.render(g);

        // render scores

        g.setFont(new Font("arial", Font.BOLD, 40));
        g.setColor(Color.WHITE);
        g.drawString("" + ball.getServerScore(),50, 40);
        g.drawString("" + ball.getRemoteScore(),500, 40);

        g.dispose();
        bs.show();

    }

    /*
        Key controls
     */

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            serverRacket.moveUp();
        }

        if (key == KeyEvent.VK_DOWN) {
            serverRacket.moveDown();
        }
    }
    public void keyTyped(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

}
