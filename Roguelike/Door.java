import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Door here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Door extends Wall
{
    private String dir;
    private boolean locked = false;
    
    public Door (int width, int height, int rotation, String dir) {
        super("door-open.png", width, height, rotation);
        
        this.dir = dir;
    }
    
    public String getDir() {
        return dir;
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public void lock() {
        locked = true;
        setImage(drawImage("door-closed.png"));
    }
    
    public void unlock() {
        locked = false;
        setImage(drawImage("door-open.png"));
    }
}
