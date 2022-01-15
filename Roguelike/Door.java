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
    
    public Door (String imagePath, int width, int height, int rotation, String dir) {
        super(imagePath, width, height, rotation);
        
        this.dir = dir;
    }
    
    public String getDir() {
        return dir;
    }
}
