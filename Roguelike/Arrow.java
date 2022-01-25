import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The most basic projectile.
 * 
 * @author Eddie Zhuang
 * @version Jan. 21, 2022
 */
public class Arrow extends Projectile
{
    public Arrow(boolean playerShot, int rotation) {
        super(playerShot, rotation, 10, 10, "bow_fire.wav", 100, 5);
    }
}