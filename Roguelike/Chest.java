import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * A chest of goodies!
 * 
 * @author Eddie Zhuang
 * @version Jan. 22, 2022
 */
public class Chest extends Actor
{
    private Animation animation;
    
    private String state = "closed";
    private double itemForce = 10;
    
    public Chest(int width, int height) {
        GreenfootImage[] frames = Animation.generateFrames(3, "chest/chest_full_open_anim_f", ".png", width, height);
        animation = new Animation(this, frames, 50);
        animation.setActiveState(true);
        animation.run();
    }
    
    /**
     * Act - do whatever the Chest wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (state == "opening") {
            animation.run();
            
            // Opening animation only runs once, release items when animation finishes
            if (animation.endOfAnimation()) {
                state = "opened";
                releaseItems();
            }
        }
    }
    
    /**
     * Throw out items from chest
     */
    private void releaseItems() {
        HealthPotion healthPotion = new HealthPotion(getImage().getWidth() / 2, -1);
        getWorld().addObject(healthPotion, getX(), getY());
        
        Random random = new Random();
        int randomAngle = random.nextInt(360);
        healthPotion.addForce(randomAngle, itemForce);
    }
    
    /**
     * Opens the chest
     */
    public void open() {
        state = "opening";
        // Play chest opening sound
        GreenfootSound sound = new GreenfootSound("chest_open.mp3");
        sound.play();
    }
    
    /**
     * Returns whether the chest is closed
     */
    public boolean isClosed() {
        return state == "closed";
    }
}