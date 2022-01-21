import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A minimap to show the map of the rooms!
 * 
 * @author Eddie Zhuang
 * @version Jan. 21, 2022
 */
public class Minimap extends Actor
{
    private int[] floorPlan; // the layout of rooms
    private int curRoomNum; // room player is currently in
    private int bossRoomNum; // room of boss
    private int cellWidth = 20; // width of each cell in minimap
    private int cellHeight = 20; // height of each cell in minimap
    private int minX = Integer.MAX_VALUE; // x coordinate of leftmost room
    private int minY = Integer.MAX_VALUE; // x coordinate of rightmost room
    private int maxX = 0; // y coordinate of topmost room
    private int maxY = 0; // y coordinate of bottommost room
    
    public Minimap(int[] floorPlan, int curRoomNum, int bossRoomNum) {
        this.floorPlan = floorPlan;
        this.curRoomNum = curRoomNum;
        this.bossRoomNum = bossRoomNum;
        
        scopeMinimap();
        setImage(drawImage());
    }
    
    /*
     * Gets the x and y coordinates of rooms at edges, so minimap can be drawn without empty space
     */
    private void scopeMinimap() {
        for (int i = 0; i < floorPlan.length; i++) {
            if (floorPlan[i] != 0) {
                int x = i % 10; // x index is represented by ones digit
                int y = (i - x) / 10; // y index is represented by tens digit
                int posX = x * cellWidth;
                int posY = y * cellHeight;
                
                // set appropriate min and max coordinates
                minX = Math.min(minX, posX);
                minY = Math.min(minY, posY);
                maxX = Math.max(maxX, posX);
                maxY = Math.max(maxY, posY);
            }
        }
    }
        
    /*
     * Draws the minimap
     */
    private GreenfootImage drawImage () {
        // Sets image dimensions to only contain non-empty rooms
        GreenfootImage image = new GreenfootImage(maxX - minX + cellWidth, maxY - minY + cellHeight);
        
        for (int i = 0; i < floorPlan.length; i++) {
            // 0 represents no room at this position, so skip those
            if (floorPlan[i] != 0) {
                int x = i % 10;
                int y = (i - x) / 10;
                int posX = x * cellWidth - minX;
                int posY = y * cellHeight - minY;
                
                // If player is in this room, make it white, else make it gray
                if (i == curRoomNum) {
                    image.setColor(Color.WHITE);
                } else {
                    image.setColor(Color.GRAY);
                }
                image.fillRect(posX, posY, cellWidth, cellHeight);
                
                // Indicate the boss room with a little skull icon :)
                if (i == bossRoomNum) {
                    GreenfootImage skull = new GreenfootImage("skull.png");
                    skull.scale(cellWidth, cellHeight);
                    image.drawImage(skull, posX, posY);
                }
                
                // Draw outline
                image.setColor(Color.BLACK);
                image.drawRect(posX, posY, cellWidth, cellHeight);
            }
        }
        
        return image;
    }
    
    /*
     * Update minimap when player enters another room
     */
    public void updateCurRoomNum(int curRoomNum) {
        this.curRoomNum = curRoomNum;
        setImage(drawImage());
    }
}
