import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Minimap here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Minimap extends Actor
{
    private int[] floorPlan;
    private int startRoomNum;
    private int curRoomNum;
    private int bossRoomNum;
    
    private int cellWidth = 50;
    private int cellHeight = 50;
    private int minX = Integer.MAX_VALUE;
    private int minY = Integer.MAX_VALUE;
    private int maxX = 0;
    private int maxY = 0;
    
    public Minimap(int[] floorPlan, int startRoomNum, int curRoomNum, int bossRoomNum) {
        this.floorPlan = floorPlan;
        this.startRoomNum = startRoomNum;
        this.curRoomNum = curRoomNum;
        this.bossRoomNum = bossRoomNum;
        
        scopeMinimap();
        drawMinimap();
    }
    
    private void scopeMinimap() {
        for (int i = 0; i < floorPlan.length; i++) {
            if (floorPlan[i] == 1) {
                int x = i % 10;
                int y = (i - x) / 10;
                int posX = x * cellWidth;
                int posY = y * cellHeight;
                
                minX = Math.min(minX, posX);
                minY = Math.min(minY, posY);
                maxX = Math.max(maxX, posX);
                maxY = Math.max(maxY, posY);
            }
        }
    }
        
    private void drawMinimap () {
        GreenfootImage image = new GreenfootImage(maxX - minX + cellWidth, maxY - minY + cellHeight);
        
        image.setColor(Color.WHITE);
        image.fill();
        
        for (int i = 0; i < floorPlan.length; i++) {
            if (floorPlan[i] == 1) {
                int x = i % 10;
                int y = (i - x) / 10;
                int posX = x * cellWidth - minX;
                int posY = y * cellHeight - minY;
                
                if (i == curRoomNum) {
                    image.setColor(Color.GREEN);
                } else if (i == bossRoomNum) {
                    image.setColor(Color.RED);
                } else {
                    image.setColor(Color.BLACK);
                }

                image.fillRect(posX, posY, cellWidth, cellHeight);
            }
        }
        
        setImage(image);
    }
}
