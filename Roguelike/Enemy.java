import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * The parent class for all enemies!
 * 
 * @author Eddie Zhuang
 * @version Jan. 21, 2022
 */
public abstract class Enemy extends Entity
{
    // How much points are given for defeating this enemy
    protected int pointsValue;
    
    public Enemy(int width, int height, int health, int pointsValue, double speed, double maxSpeed) {
        super(width, height, health, speed, maxSpeed);
        this.pointsValue = pointsValue;
    }
    
    public void addedToWorld (World w) {
        // When added to world, add health bar
        stats = new StatBar​(health, this);
        w.addObject(stats, 0, 0);
    }
    
    /**
     * Implements what happens when the enemy dies
     */
    protected void die() {
        GameWorld g = (GameWorld)getWorld();
        g.removeObject(this); // Removes enemy
        g.updateScore(pointsValue); // Gives appropriate points to player
    }
}