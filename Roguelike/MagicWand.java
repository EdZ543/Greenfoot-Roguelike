import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Fires an epic homing magic ball
 * 
 * @author Eddie Zhuang
 * @version Jan. 24, 2022
 */
public class MagicWand extends Weapon
{
    public MagicWand(Actor owner) {
        super(-1, 64, 40, owner, 5, "right");
    }
    
    /**
     * Releases a bow
     */
    public void releaseProjectiles(int angle) {
        getWorld().addObject(new MagicBall(true, angle), getX(), getY());
    }
}
