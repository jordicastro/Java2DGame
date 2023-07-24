package entity;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
//import java.awt.Graphics;

import main.GamePanel;
import main.KeyHandler;



public class Player extends Entity
{
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public int hasKey = 0;

    public Player(GamePanel gp, KeyHandler keyH)
    {
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize/2); // middle of screen. (top left of sprite) - half a tile = actual middle.
        screenY = gp.screenHeight /2 - (gp.tileSize/2); 

        solidArea = new Rectangle(8, 16, 32, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        setDefaultValues();
        getPlayerImage();

    }

    public void setDefaultValues()
    {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage()
    {
        try // load player images.
        {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/jord_up_1.png"));

            up2 = ImageIO.read(getClass().getResourceAsStream("/player/jord_up_2.png"));

            down1 = ImageIO.read(getClass().getResourceAsStream("/player/jord_down_1.png"));

            down2 = ImageIO.read(getClass().getResourceAsStream("/player/jord_down_2.png"));

            left1 = ImageIO.read(getClass().getResourceAsStream("/player/jord_left_1.png"));
  
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/jord_left_2.png"));

            right1 = ImageIO.read(getClass().getResourceAsStream("/player/jord_right_1.png"));

            right2 = ImageIO.read(getClass().getResourceAsStream("/player/jord_right_2.png"));

            static1 = ImageIO.read(getClass().getResourceAsStream("/player/jord_static_1.png"));

            static2 = ImageIO.read(getClass().getResourceAsStream("/player/jord_static_2.png"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void update() // called 60 times per second.
    {
        if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true)
        {
                if (keyH.upPressed == true) // key handler's upPressed, move character UP
            {
                direction = "up";
            }
            else if (keyH.downPressed == true)
            {
                direction = "down";
            }
            else if (keyH.leftPressed == true)
            {
                direction = "left";
            }
            else if (keyH.rightPressed == true)
            {
                direction = "right";
            }


            // CHECK TILE COLLISION
            collisionOn = false;

            gp.cChecker.checkTile(this); // player class can be passed to CollisionChecker class as an entity.

            // CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (collisionOn == false)
            {
                switch(direction)
                {
                    case "up":
                        worldY -= speed; // going up subtracts. (0,0 is in top left corner)
                        break;

                    case "down":
                        worldY += speed; // increment/go down by the speed # of tiles
                        break; 

                    case "left":
                        worldX -= speed;
                        break;

                    case "right":
                        worldX += speed;
                        break;
                }
            }

            spriteCounter++;
            if (spriteCounter > 12)
            {
                if (spriteNum == 1)
                {
                    spriteNum = 2;
                }
                else if(spriteNum == 2)
                {
                    spriteNum = 1;
                }

                spriteCounter = 0;
            }
        }
        
        
    }
    public void pickUpObject(int i)
    {
        if (i != 999) // actually touched an object.
        {
            String objectName = gp.obj[i].name;
            
            switch (objectName)
            {
                case "Key":
                    gp.playSE(1);
                    hasKey++;
                    gp.obj[i] = null;
                    System.out.println("Key: " + hasKey);
                    break;
                case "Door":
                    gp.playSE(3);
                    if (hasKey > 0)
                    {
                        gp.obj[i] = null;

                        hasKey--;
                    }
                    System.out.println("Key: " + hasKey);
                    break;
                
                case "Boots":
                    gp.playSE(2);
                    speed +=2;
                    gp.obj[i] = null;
            }
            
        }
    }

    public void draw(Graphics2D g2)
    {
        // g2.setColor(Color.white);
        
        BufferedImage image = null;


        if (direction == "up")
        {
            if(spriteNum == 1)
                image = up1;
            if(spriteNum == 2)
                image = up2;
        }
        else if (direction == "down") 
        {
            if (spriteNum == 1)
                image = down1;
            if (spriteNum == 2)
                image = down2;
        }
        else if (direction == "left")
        {
            if (spriteNum == 1)
                image = left1;
            if (spriteNum == 2)
                image = left2;
        }
        else if (direction == "right")
        {
            if (spriteNum == 1)
            image = right1;
            if (spriteNum == 2)
            image = right2;
        }
        else if (direction == "up" && keyH.upPressed == false) // not working
        {
            image = static1;
        }
        else if (direction == "down" && keyH.downPressed == false) // not working
        {
            image = static2;
        }

        // g2.fillRect(x, y, gp.tileSize, gp.tileSize);
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }
}
