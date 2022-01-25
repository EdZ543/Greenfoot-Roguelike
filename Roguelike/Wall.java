import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A wall. Enemies, players, and projectiles cannot pass through these. Epic.
 * 
 * @author Eddie Zhuang
 * @version Jan. 21, 2022
 */
public class Wall extends Actor
{
    private int rotation;
    
    public Wall (String imagePath, int rotation) {
        // set image based on image, width, height, and rotation
        this.rotation = rotation;
        
        setImage(drawImage(imagePath));
    }
    
    /**
     * Constructor for drawing invisible walls
     */
    public Wall (int width, int height) {
        getImage().scale(width, height);
        getImage().setTransparency(0);
    }
    
    protected GreenfootImage drawImage(String imagePath) {
        GreenfootImage image = new GreenfootImage(imagePath);
        image.rotate(rotation);
        return image;
    }
}