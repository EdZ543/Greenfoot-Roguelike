import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class WallTile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wall extends Actor
{
    protected GreenfootImage image;
    protected int width;
    protected int height;
    protected int rotation;
    
    public Wall (String imagePath, int width, int height, int rotation) {
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        setImage(drawImage(imagePath));
    }
    
    protected GreenfootImage drawImage(String imagePath) {
        image = new GreenfootImage(imagePath);
        image.scale(width, height);
        image.rotate(rotation);
        return image;
    }
}