import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A destructible wall
 * 
 * @author Eddie Zhuang
 * @version Jan. 24, 2022
 */
public class Rock extends Wall
{
    private int hits = 4;
    private int transparency = 255;
    
    public Rock() {
        super("rock.png", 0);
    }
    
    /**
     * Damages rock
     */
    public void damage() {
        hits--;
        transparency -= 50;
        getImage().setTransparency(transparency); // Make rock more transparent as it gets damaged
        
        // If taken all hits it can, remove the rock
        if (hits == 0) {
            getWorld().removeObject(this);
        }
    }
}
