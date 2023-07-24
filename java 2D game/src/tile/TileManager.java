package tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import main.GamePanel;
import java.awt.Graphics2D;

public class TileManager
{
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][];
    
    public TileManager(GamePanel gp) // constructor
    {
        this.gp = gp;

        tile = new Tile[10]; // 10 different kind of tiles.
        getTileImage();
        mapTileNum = new int [gp.maxWorldCol][gp.maxWorldRow]; // place the numbers in map.txt into here, then use this 2d array to draw the map.

        getTileImage();
        loadMap("/maps/world01.txt");
    }

    public void getTileImage() 
    {
        try
        {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png")); 
            tile[1].collision = true; // solid collision tile.

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png")); 
            tile[2].collision = true; // solid collision tile.

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
            tile[4].collision = true; // solid collision tile.

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    public void loadMap(String filePath)
    {
        try
        {
            InputStream is = getClass().getResourceAsStream(filePath); //input the txt file.
            BufferedReader br = new BufferedReader(new InputStreamReader(is)); // read the contents of the txt file.

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow)
            {
                String line = br.readLine(); // read a single line and place it in String line.

                while (col < gp.maxWorldCol)
                {
                    String numbers[] = line.split(" "); // splits the string around matches of " " (spaces).

                    int num = Integer.parseInt(numbers[col]); // from string to integer.
                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol)
                {
                    col = 0;
                    row++;
                }
            
            }
            br.close(); // close buffered reader.
        } catch (Exception e)
        {

        }
    }
    public void draw(Graphics2D g2)
    {
        // g2.drawImage(tile[0].image, 0, 0, gp.tileSize, gp.tileSize, null);
        // g2.drawImage(tile[1].image, 48, 0, gp.tileSize, gp.tileSize, null);
        // g2.drawImage(tile[2].image, 96, 0, gp.tileSize, gp.tileSize, null);
        //768 pixels by 576 pixels. block increments of 48.
        

        int worldCol = 0;
        int worldRow = 0;


        while(worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow)
        {
            int tileNum = mapTileNum[worldCol][worldRow]; // works as an index as the tile type array.

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX; // subtract player position from [0,0], add screenx to make sure player doesnt see outside of map while staying in center of screen frame.
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && 
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && 
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && 
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) // only draw whats on the screen to improve performance: +/- gp.tileSize to draw slightly bigger than frame (when you move you wont see the black border outside drawn frame)
            {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
            
            worldCol++;
  

            if (worldCol == gp.maxWorldCol)
            {
                worldCol = 0;
                worldRow++;

            }
        }

    }
}
