import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Tile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Floor extends Actor
{
    private GreenfootImage image;
    
    public Floor (int width, int height) {
        
        image = new GreenfootImage("v1.1 dungeon crawler 16x16 pixel pack/tiles/floor/floor_1.png");
        image.scale(width, height);
        setImage(image);
    }
    
    /**
     * Act - do whatever the Tile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
    }
}
