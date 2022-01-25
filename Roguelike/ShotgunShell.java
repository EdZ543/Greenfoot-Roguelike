import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * One of the many tiny shells that comes out of the shotgun weapon.
 * 
 * @author Eddie Zhuang
 * @version Jan. 24, 2022
 */
public class ShotgunShell extends Projectile
{
    public ShotgunShell(boolean playerShot, int rotation) {
        super(20, 20, playerShot, rotation, 20, 4, "bow_fire.wav", 0, 5);
    }
}