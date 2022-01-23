import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A potion that gives the player some health.
 * 
 * @author Eddie Zhuang
 * @version Jan. 21, 2022
 */
public class HealthPotion extends Actor
{
    private int healthIncrease = 30;
    
    public HealthPotion(int width, int height) {
        GameWorld.scaleWithAspectRatio(getImage(), width, height);
    }
    
    public void use(Player player) {
        player.heal(healthIncrease);
    }
}
