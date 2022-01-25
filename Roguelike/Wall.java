import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A wall. Enemies, players, and projectiles cannot pass through these. Epic.
 * 
 * @author Eddie Zhuang
 * @version Jan. 21, 2022
 */
public class Wall extends Actor
{
    public Wall () {}
    
    /**
     * Constructor for drawing invisible walls
     */
    public Wall (int width, int height) {
        getImage().setTransparency(0);
        getImage().scale(width, height);
    }
}