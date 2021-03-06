import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A floor that damages the player if they step on it
 * 
 * @author Eddie Zhuang 
 * @version Jan. 21, 2022
 */
public class SpikedFloor extends Actor
{
    private int damage = 1;
    
    public void act() {
        // Damage player if they step on this floor
        Player p = (Player)getOneIntersectingObject(Player.class);
        if (p != null){
            p.damageMe(damage);
        }
    }
}
