import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The potion class, contains code common to all potions
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Potion extends Item
{
    /**
     * When picked up, removes potion, and applies effect on player
     */
    protected void pickUp(Player player) {
        getWorld().removeObject(this);
        apply(player);
    }
    
    protected abstract void apply(Player player);
}