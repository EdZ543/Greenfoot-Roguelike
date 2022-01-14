import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class SpikedFloor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SpikedFloor extends FloorTile
{
    private int damage = 1;
    
    public SpikedFloor (int tileWidth, int tileHeight) {
        super(tileWidth, tileHeight);
    }
    
    public void act() {
        Player p = (Player)getOneIntersectingObject(Player.class);
        if (p != null){
            p.damageMe(damage);
        }
    }
}
