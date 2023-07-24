package main;

import entity.Entity;

public class CollisionChecker 
{
    GamePanel gp;
    
    public CollisionChecker(GamePanel gp)
    {
        this.gp = gp;
    }
    public void checkTile(Entity entity) // check 4 points: left x, right x, top y, bottom y: for collisions.
    { // only need to check 2 positions for one direction: if player is going up, check top corners of hitbox.
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;

        int tileNum1, tileNum2;

        switch(entity.direction)
        {
            case "up":
            entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
            tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
            if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) // if collision
            {
                entity.collisionOn = true;
            }
                break;

            case "down":
            entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
            tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
            if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) // if collision
            {
                entity.collisionOn = true;
            }
               
                break;
            case "left":
            entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
            tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
            if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) // if collision
            {
                entity.collisionOn = true;
            }
               
                break;
            case "right":
            entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
            tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
            if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) // if collision
            {
                entity.collisionOn = true;
            }
               
                break;
        }
    }
    public int checkObject(Entity entity, boolean player)
    {
        int index = 999;
        for (int i = 0; i < gp.obj.length; i++)
        {
            if(gp.obj[i] != null)
            {
                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                // Get the object's solid are position
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                switch(entity.direction)
                {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) // checks if two rectangles collide up collision
                        {
                            if (gp.obj[i].collision == true) // if obj is solid
                            {
                                entity.collisionOn = true;
                            }
                            if (player == true) // avoids npcs and monsters picking up items.
                            {
                                index = i;
                            }

                        }
                    case "down":
                        entity.solidArea.y += entity.speed; 
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) // down collision
                        {
                            if (gp.obj[i].collision == true) // if obj is solid
                            {
                                entity.collisionOn = true;
                            }
                            if (player == true) // avoids npcs and monsters picking up items.
                            {
                                index = i;
                            }
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) // left collision
                        {
                            if (gp.obj[i].collision == true) // if obj is solid
                            {
                                entity.collisionOn = true;
                            }
                            if (player == true) // avoids npcs and monsters picking up items.
                            {
                                index = i;
                            }
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if (entity.solidArea.intersects(gp.obj[i].solidArea)) // right collision
                        {
                            if (gp.obj[i].collision == true) // if obj is solid
                            {
                                entity.collisionOn = true;
                            }
                            if (player == true) // avoids npcs and monsters picking up items.
                            {
                                index = i;
                            }
                        }
                        break;
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;

            }
        }
        return index;

    }
}
