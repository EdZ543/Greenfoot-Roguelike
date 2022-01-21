import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A door, lets player travel to different rooms!
 * 
 * @author Eddie Zhuang 
 * @version Jan. 21, 2022
 */
public class Door extends Wall
{
    
    private String dir; // direction the door leads to
    private boolean locked = false; // whether the door is locked or not
    
    public Door (int width, int height, int rotation, String dir) {
        super("door-open.png", width, height, rotation);
        this.dir = dir;
    }
    
    // Returns the direction the door is facing
    public String getDir() {
        return dir;
    }
    
    // Returns whether the door is locked or not.
    public boolean isLocked() {
        return locked;
    }
    
    // Locks the door
    public void lock() {
        locked = true;
        setImage(drawImage("door-closed.png"));
    }
    
    // Opens the door
    public void unlock() {
        locked = false;
        setImage(drawImage("door-open.png"));
    }
}
