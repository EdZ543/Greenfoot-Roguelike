import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class Enemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemy extends Actor
{
    protected GreenfootImage image;
    
    protected int health;
    
    /**
     * Enemy constructor
     */
    public Enemy(int width, int height, int health, String imagePath) {
        this.health = health;
        
        image = new GreenfootImage(imagePath);
        image.scale(width, height);
        setImage(image);
    }
}
