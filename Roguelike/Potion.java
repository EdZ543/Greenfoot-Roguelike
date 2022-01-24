import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Potion here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Potion extends Item
{
    public Potion(int width, int height) {
        super(width, height);
    }
    
    protected void pickUp(Player player) {
        getWorld().removeObject(this);
        apply(player);
    }
    
    protected abstract void apply(Player player);
}