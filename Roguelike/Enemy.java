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
    protected GreenfootImage image;
    protected StatBar stats;
    protected int points;
    protected int health;
    
    /**
     * Enemy constructor
     */
    public Enemy(int health, int points) {
        this.health = health;
        this.points = points;
        
        stats = new StatBar​(health, this);
    }
    
    public void addedToWorld (World w) {
        w.addObject(stats, 0, 0);
    }
    
    public void damageMe(int attackDamage) {
        health = Math.max(0, health - attackDamage);
        stats.update(health);
        if (health == 0) {
            die();
        }
    }
    
    private void die() {
        GameWorld g = (GameWorld)getWorld();
        g.removeObject(this);
        g.updateScore(points);
    }
}
