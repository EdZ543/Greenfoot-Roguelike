import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Minimap here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Minimap extends Actor
{
    int[] floorPlan;
    int cellWidth = 50;
    int cellHeight = 50;
    
    public Minimap(int[] floorPlan) {
        this.floorPlan = floorPlan;
        
        setImage(visualizeMap());
    }
    
    private GreenfootImage visualizeMap() {
        GreenfootImage image = new GreenfootImage(cellWidth * 9, cellWidth * 8);
        
        image.setColor(Color.BLUE);
        image.fill();
        
        image.setColor(Color.BLACK);
        
        for (int i = 0; i < floorPlan.length; i++) {
            if (floorPlan[i] == 1) {
                int x = i % 10;
                int y = (i - x) / 10;
                
                image.setColor(Color.BLACK);
                image.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
            }
        }
        
        return image;
    }
    
    /**
     * Act - do whatever the Minimap wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }
}
