import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Floor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Floor extends Actor
{
    private GreenfootImage image;
    
    public Floor (int tileWidth, int tileHeight) {
        image = new GreenfootImage("corkboard.jpg");
        image.scale(tileWidth, tileHeight);
        setImage(image);
    }
}
