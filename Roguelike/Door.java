import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A door, lets player travel to different rooms!
 * 
 * @author Eddie Zhuang 
 * @version Jan. 21, 2022
 */
public class Door extends Wall
{
    GreenfootImage openImage = new GreenfootImage("door-open.png");
    GreenfootImage closedImage = new GreenfootImage("door-closed.png");
    
    private String dir; // direction the door leads to
    private boolean locked = false; // whether the door is locked or not
    
    public Door (int rotation, String dir) {
        this.dir = dir;
        
        openImage.rotate(rotation);
        closedImage.rotate(rotation);
        unlock();
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
        setImage(closedImage);
    }
    
    // Opens the door
    public void unlock() {
        locked = false;
        setImage(openImage);
    }
}
