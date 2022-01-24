import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The default weapon, fires an arrow
 * 
 * @author Eddie Zhuang 
 * @version Jan. 24, 2022
 */
public class Bow extends Weapon
{
    public Bow(Actor owner) {
        super(-1, 50, 30, owner, 8, "right");
    }
    
    /**
     * Releases a bow
     */
    public void releaseProjectiles(int angle) {
        getWorld().addObject(new Arrow(true, angle), getX(), getY());
    }
}