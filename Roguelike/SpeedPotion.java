import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class SpeedPotion here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SpeedPotion extends Item
{
    protected void apply(Player player) {
        player.changeSpeed(0.5);
    }
}
