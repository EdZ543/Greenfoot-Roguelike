import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class Enemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Enemy extends Entity
{
    protected int pointsValue;
    
    public Enemy(int width, int height, int speed, int health, int pointsValue) {
        super(width, height, speed, health);
        this.pointsValue = pointsValue;
    }
    
    public void addedToWorld (World w) {
        stats = new StatBar​(health, this);
        
        w.addObject(stats, 0, 0);
    }
    
    protected void die() {
        GameWorld g = (GameWorld)getWorld();
        g.removeObject(this);
        g.updateScore(pointsValue);
    }
    
    protected abstract void initAnimations();
}
