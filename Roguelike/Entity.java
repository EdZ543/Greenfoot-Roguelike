import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Code common to all entities, including players and enemies.
 * Keeps track of position and rotation using doubles for more precise movement.
 * 
 * @author Eddie Zhuang
 * @version Jan. 21, 2022
 */
public abstract class Entity extends MomentumMover
{
    protected Animation animation; // Animation of the entity
    protected StatBar stats; // Statbar of entity
    
    protected int maxHealth;
    protected double speed;
    protected int health;
    protected int width;
    protected int height;
    
    public Entity(int width, int height, int health, double speed, double maxSpeed) {
        super(0.9, maxSpeed);
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.health = health;
        maxHealth = health;
    }
    
    /**
     * If angle is in 2nd or 3rd quadrant, turn sprite left.
     * Else, turn sprite right.
     */
    protected void checkFacingDir(double angle) {
        double radians = Math.toRadians(angle);
        if (Math.cos(radians) > 0) {
            animation.setDir("right");
        } else if (Math.cos(radians) < 0) {
            animation.setDir("left");
        }
    }
    
    /**
     * Damages entity.
     * If health is 0, calls die method, which must be implemented by child classes
     */
    protected void damageMe(int attackDamage) {
        health = Math.max(0, health - attackDamage);
        stats.update(health);
        if (health == 0) {
            die();
        }
    }
    
    /**
     * Method for getting the angle to a specific actor
     */
    protected int getAngleTo (Actor a){
        return (int) (Math.toDegrees(Math.atan2(a.getY() - getY() , a.getX() - getX())) + 0.5);
    }
    
    /**
     * Method called when entity dies
     */
    protected abstract void die();
}