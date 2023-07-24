package main;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable // inherits all the attributes of hte Jpanel class.
{
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile. (for each character)
    final int scale = 3; //16*3 = 48 to account for large monitor display.

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12; // 3 to 4 ratio.
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels.
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels.

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    // public final int worldWidth = tileSize * maxWorldCol;
    // public final int worldHeight = tileSize * maxWorldRow;


    // FPS
    int FPS = 60;

    // SYSYEM
    TileManager tileM = new TileManager(this); // pass this game panel class.
    KeyHandler keyH = new KeyHandler(); 
    Sound music = new Sound();
    Sound se = new Sound(); // se
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread; 

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10]; // 10 object array
    // pass gamepanel class and keyH.


    // constructor
    public GamePanel()
    {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); // improves performance.
        this.addKeyListener(keyH); // key input. 
        this.setFocusable(true); // game panel can be "focused" to receive key input.
    }

    public void setupGame()
    {
        aSetter.setObject();

        //playMusic(0); < ---------------------------------
    }

    public void startGameThread()
    {
        gameThread = new Thread(this); // pass GamePanel pass "this" into thread. 
        gameThread.start(); // runs run() 
    }

    @Override
    /*public void run() // create a GAMELOOP
    {
        double drawInterval = 1000000000/FPS; // nano seconds (-9) / 60 : draw screen ever 0.0167s
        double nextDrawTime = System.nanoTime() + drawInterval; // curr system time + draw Interval
        while(gameThread != null) // as long as game thread exists.
        {
            // System.out.println("The game is running.\n");
            
            // 1 UPDATE: update information (character position)
            update();
            // 2 DRAW: draw the screen (with updated information)
            repaint(); // calls paint component.

            try
            {
                double remainingTime = nextDrawTime - System.nanoTime(); // how much time remains until next draw interval. SLEEP during this remaining interval
                remainingTime = remainingTime/1000000; // convert from nano to miliseconds for sleep method.

                if (remainingTime < 0)
                {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime); // pause game until next interval begins.

                nextDrawTime += drawInterval;
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }
    } 
    */
    public void run()
    {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0; //FPS UI
        //int drawCount = 0; // FPS UI

        while (gameThread != null)
        {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval; // add how much time has passed / draw interval to delta
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1)
            {
                update();
                repaint();
                delta--;
                //drawCount++;
            }
            if (timer >= 1000000000) // at the end of 1 second, display counted frames.
            {
                //waSystem.out.println("FPS: " + drawCount);
                //drawCount = 0;
                timer = 0;
            }
        }
    
    }

    public void update()
    {
        
        player.update(); 
    } // after player position is updated, paintComponent is called to draw the updated player in its new location.
    public void paintComponent(Graphics g) // implemented into java.
    {
        super.paintComponent(g); // super: the parent class of said class; Jpanel is the parent of paint componment

        Graphics2D g2 = (Graphics2D)g;
        // TILE
        tileM.draw(g2); // draw tiles first, then player. (layering)
        // OBJECT
        for (int i = 0; i < obj.length; i++) // iterate through obj array and print objects
        {
            if (obj[i] != null)
            {
                obj[i].draw(g2, this);
            }
        }
        // PLAYER
        player.draw(g2);   
        // UI
        // ui.draw(g2);  <---------------------------------------------------
        
        g2.dispose(); // release memory
    }
    public void playMusic(int i)
    {
        music.setFile(i);
        music.play();
        music.loop();

    }
    public void stopMusic()
    {
        music.stop();
    }
    public void playSE(int i)
    {
        se.setFile(i);
        se.play(); // no loop
    }
}

