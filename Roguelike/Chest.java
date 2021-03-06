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
    private static GreenfootImage[] frames;
    
    private Animation animation;
    private String state = "closed";
    private double itemForce = 10;
    
    public Chest() {
        if (frames == null) {
            frames = Animation.generateFrames(0, 3, "chest/chest", ".png");
        }
        
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
        Potion potion;
        Weapon weapon;
        
        Random random = new Random();
        int randomPotion = random.nextInt(2);
        int randomWeapon = random.nextInt(2);
        
        // Pick a random potion
        if (randomPotion == 0) {
            potion = new HealthPotion();
        } else {
            potion = new SpeedPotion();
        }
        
        // Pick a random weapon
        if (randomWeapon == 0) {
            weapon = new Shotgun(null);
        } else {
            weapon = new MagicWand(null);
        }
        
        getWorld().addObject(potion, getX(), getY());
        int randomAngle = random.nextInt(360);
        potion.addForce(randomAngle, itemForce);
        
        getWorld().addObject(weapon, getX(), getY());
        randomAngle = random.nextInt(360);
        weapon.addForce(randomAngle, itemForce);
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