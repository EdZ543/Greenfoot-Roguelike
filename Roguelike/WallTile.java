import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class WallTile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class WallTile extends Actor
{
    private GreenfootImage image;
    
    public WallTile (String imagePath, int width, int height, int rotation) {
        image = new GreenfootImage(imagePath);
        
        image.scale(width, height);
        image.rotate(rotation);
        setImage(image);
    }
}
