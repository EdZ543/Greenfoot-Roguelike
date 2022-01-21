import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Code common to all entities, including players and enemies.
 * Keeps track of position and rotation using doubles for more precise movement.
 * 
 * @author Eddie Zhuang
 * @version Jan. 21, 2022
 */
public abstract class Entity extends Actor
{
    protected Animation animation; // Animation of the entity
    protected StatBar stats; // Statbar of entity
    
    protected int maxHealth;
    protected int speed;
    protected int health;
    protected int width;
    protected int height;
    
    // Variables for making movement and rotation more precise than Greenfoot's default system
    // Also provides rotation seperate from image rotation, because my sprites aren't meant to rotate
    protected double exactX;
    protected double exactY;
    protected double rotation = 0;
    
    public Entity(int width, int height, int speed, int health) {
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.health = health;
        maxHealth = health;
    }
    
    /**
     * Turns towards an actor
     * 
     * @param a actor to turn towards
     */
    public void turnTowards (Actor a){
        turnTowards (a.getX(), a.getY());
    }
    
    /**
     * More precise turnTowards method
     */
    @Override
    public void turnTowards (int x, int y){
        setRotation(Math.toDegrees(Math.atan2(y - getY() , x - getX())));
    }
    
    /**
     * Overrides to use more precise method
     */
    @Override
    public void move(int distance)
    {
        move((double)distance);
    }
    
    /**
     * More precise movement
     */
    public void move(double distance)
    {
        double radians = Math.toRadians(rotation);
        double dx = Math.cos(radians) * distance;
        double dy = Math.sin(radians) * distance;
        
        if (dx != 0){
            setLocation(exactX + dx, exactY);
            
            double signX = dx / Math.abs(dx);
            while (isTouching(Wall.class)) setLocation(exactX - signX, exactY);
        }    
        
        if (dy != 0){
            setLocation(exactX, exactY + dy);
            
            double signY = dy / Math.abs(dy);
            while (isTouching(Wall.class)) setLocation(exactX, exactY - signY);
        }
    }
    
    /**
     * Movement but without detecting wall collisions
     */
    protected void moveWithoutCollision(double distance) {
        double radians = Math.toRadians(rotation);
        double dx = Math.cos(radians) * distance;
        double dy = Math.sin(radians) * distance;
        
        setLocation(exactX + dx, exactY + dy);
    }
    
    /**
     * Overrides to use more precise rotation method
     */
    @Override
    public void setRotation (int rotation){
        setRotation((double)rotation);
    }
    
    /**
     * Rotates, and changes facing direction of image to match
     */
    public void setRotation (double rotation){
        this.rotation = rotation;
        checkFacingDir(rotation);
    }
    
    @Override
    public void setLocation(int x, int y) 
    {
        exactX = x;
        exactY = y;
        super.setLocation(x, y);
    }
    
    /**
     * Sets location using exact double coordinates
     */
    public void setLocation(double x, double y) 
    {
        exactX = x;
        exactY = y;
        super.setLocation((int) (x + 0.5), (int) (y + 0.5));
    }
    
    /**
     * Method for getting the angle to a specific actor
     */
    protected int getAngleTo (Actor a){
        return (int) (Math.toDegrees(Math.atan2(a.getY() - getY() , a.getX() - getX())) + 0.5);
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
     * Method called when entity dies
     */
    protected abstract void die();
}