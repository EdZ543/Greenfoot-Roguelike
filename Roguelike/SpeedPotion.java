import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Increases the speed of the player
 * 
 * @author Eddie Zhuang 
 * @version Jan. 24, 2022
 */
public class SpeedPotion extends Item
{
    public SpeedPotion(int width, int height) {
        super(width, height);
    }
    
    /**
     * Increases player speed a bit
     */ 
    protected void apply(Player player) {
        player.changeSpeed(1);
    }
}
