import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

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
        
    }
    
    /**
     * Opens the chest
     */
    public void open() {
        if (state == "closed") {
            state = "opening";
        }
    }
}