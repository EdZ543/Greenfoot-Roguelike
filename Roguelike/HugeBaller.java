import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A very big ball attack.
 * 
 * @author Eddie Zhuang
 * @version Jan. 21, 2022
 */
public class HugeBaller extends Projectile
{
    public HugeBaller(boolean playerShot, int rotation) {
        super(playerShot, rotation, 7, 30, "bow_fire.wav", 100, 10);
    }
}
