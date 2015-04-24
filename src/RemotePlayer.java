/**
 * Created by matiaskaskimies on 27/01/15.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class RemotePlayer extends Canvas {

    // remote racket's x position
    private final int REMOTEX = 540;
    private final int SERVERX = 40;

    Racket racket = new Racket(REMOTEX,200);
    Racket serverRacket = new Racket(SERVERX,200);
    Socket clientSocket = null;
    Ball ball = new Ball(250,250,serverRacket,racket);

    ObjectInputStream objectInputStream = null;
    ObjectOutputStream objectOutputStream = null;

    // background image
    BufferedImage image = new BufferedImage(5,5, BufferedImage.TYPE_INT_RGB);

    public int serverScore = 0;
    public int remoteScore = 0;

    public static void main(String args[]) {
        RemotePlayer remotePlayer = new RemotePlayer();

        // make frame
        remotePlayer.setSize(600, 400);
        JFrame frame = new JFrame("Pong Remote Player");
        frame.add(remotePlayer);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //frame.requestFocus();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        remotePlayer.run();

    }

    public void init() {
        try {

            clientSocket = new Socket("localhost", 3777);

            InputStream inputStream = clientSocket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);

            OutputStream outputStream = clientSocket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            outputStream.flush();

            objectOutputStream.writeInt(racket.getRacketY());
            objectOutputStream.flush();

            addKeyListener(new RemoteControl(this));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /*
        Not really a thread's run method
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

    public void tick() {

        try {
            // read game state data from server
            // update local client objects

            objectOutputStream.writeInt(racket.getRacketY());
            objectOutputStream.flush();
            // read server data from string and split to array
            String inputString = objectInputStream.readUTF();
            String[] serverValues = inputString.split(",");

            ball.setX(Integer.parseInt(serverValues[0]));
            ball.setY(Integer.parseInt(serverValues[1]));
            serverRacket.setY(Integer.parseInt(serverValues[2]));
            serverRacket.setBounds(SERVERX, serverRacket.getRacketY(), 15, 60);
            racket.setBounds(REMOTEX, racket.getRacketY(), 15, 60);

            serverScore = Integer.parseInt(serverValues[3]);
            remoteScore = Integer.parseInt(serverValues[4]);


        } catch (IOException e) {
            e.printStackTrace();
            try {
                clientSocket.close();
                System.exit(1);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    public void render() {
        // render like at server with information of the gamestate from server

        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g.setColor(Color.WHITE); // Set the color of players to white

        ball.render(g);
        serverRacket.render(g);
        racket.render(g);

        // render scores
        g.setFont(new Font("arial", Font.BOLD, 40));
        g.setColor(Color.WHITE);
        g.drawString("" + serverScore,50, 40);
        g.drawString("" + remoteScore,500, 40);

        g.dispose();
        bs.show();

    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            racket.moveUp();
        }

        if (key == KeyEvent.VK_DOWN) {
            racket.moveDown();
        }
    }
    public void keyTyped(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

}
