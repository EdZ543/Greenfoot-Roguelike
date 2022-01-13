import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class Enemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemy extends Actor
{
    private GreenfootImage image;
    private StatBar stats;
    
    protected int health;
    
    /**
     * Enemy constructor
     */
    public Enemy(int health) {
        this.health = health;
        
        stats = new StatBar​(health, this);
    }
    
    public void addedToWorld (World w) {
        w.addObject(stats, 0, 0);
    }
    
    public void damageMe(int attackDamage) {
        health = Math.max(0, health - attackDamage);
        stats.update(health);
        if (health == 0) {
            GameWorld g = (GameWorld)getWorld();
            
            g.removeObject(this);
        }
    }
}
