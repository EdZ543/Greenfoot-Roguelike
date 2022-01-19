import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Bullet here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Arrow extends Projectile
{
    private GreenfootImage image;
    
    public Arrow(boolean playerShot, int rotation) {
        super(64, -1, playerShot, rotation, 10, 10);
    }
}