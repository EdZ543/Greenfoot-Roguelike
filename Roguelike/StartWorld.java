import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StartWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StartWorld extends World
{
    private GreenfootImage image;
    
    /**
     * Constructor for objects of class StartWorld.
     * 
     */
    public StartWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 800, 1); 
        image = new GreenfootImage("welcome.png");
        setBackground(image);
    }
    
    public void act () {
        if (Greenfoot.isKeyDown("enter")){
            Greenfoot.setWorld (new GameWorld());
        }
    }
}
