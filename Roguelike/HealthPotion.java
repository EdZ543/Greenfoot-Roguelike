import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A potion that gives the player some health.
 * 
 * @author Eddie Zhuang
 * @version Jan. 21, 2022
 */
public class HealthPotion extends Potion
{
    private int healthIncrease = 30;
    private String name;
    
    /**
     * When applied, heal the player
     */
    public void apply(Player player) {
        player.heal(healthIncrease);
    }
}