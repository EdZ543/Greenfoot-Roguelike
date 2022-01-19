import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Door here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Door extends WallTile
{
    private String dir;
    private boolean locked = false;
    
    public Door (String imagePath, int width, int height, int rotation, String dir) {
        super(imagePath, width, height, rotation);
        
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
        getImage().setTransparency(100);
    }
    
    public void unlock() {
        locked = false;
        getImage().setTransparency(255);
    }
}
