import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Entity here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Entity extends Actor
{
    protected Animation animation;
    protected StatBar stats;
    
    protected int maxHealth;
    protected int speed;
    protected int health;
    protected int width;
    protected int height;
    
    protected double exactX;
    protected double exactY;
    protected double rotation = 0;
    protected double collisionPrecision = 1;
    
    public Entity(int width, int height, int speed, int health) {
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.health = health;
        maxHealth = health;
        
        initAnimations();
    }
    
    public void turnTowards (Actor a){
        turnTowards (a.getX(), a.getY());
    }
    
    @Override
    public void turnTowards (int x, int y){
        setRotation(Math.toDegrees(Math.atan2(y - getY() , x - getX())));
    }
    
    @Override
    public void move(int distance)
    {
        move((double)distance);
    }
    
    public void move(double distance)
    {
        double radians = Math.toRadians(rotation);
        double dx = Math.cos(radians) * collisionPrecision;
        double dy = Math.sin(radians) * collisionPrecision;
        
        // move x and y seperately, so enemies don't stick to walls
        for (double i = 0; i <= distance; i += collisionPrecision) {
            setLocation(exactX + dx, exactY);
            while (isTouching(Wall.class)) setLocation(exactX - dx, exactY);
            
            setLocation(exactX, exactY + dy);
            while (isTouching(Wall.class)) setLocation(exactX, exactY - dy);
        }
    }
    
    @Override
    public void setRotation (int rotation){
        setRotation((double)rotation);
    }
    
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
    
    public void setLocation(double x, double y) 
    {
        exactX = x;
        exactY = y;
        super.setLocation((int) (x + 0.5), (int) (y + 0.5));
    }
    
    protected int getAngleTo (Actor a){
        return (int) (Math.toDegrees(Math.atan2(a.getY() - getY() , a.getX() - getX())) + 0.5);
    }
    
    protected void checkFacingDir(double angle) {
        double radians = Math.toRadians(angle);
        if (Math.cos(radians) > 0) {
            animation.setDir("right");
        } else if (Math.cos(radians) < 0) {
            animation.setDir("left");
        }
    }
    
    protected void damageMe(int attackDamage) {
        health = Math.max(0, health - attackDamage);
        stats.update(health);
        if (health == 0) {
            die();
        }
    }
    
    protected boolean isAdjacentTo(Class cls) {
        double radians = Math.toRadians(rotation);
        double dx = Math.cos(radians) * collisionPrecision;
        double dy = Math.sin(radians) * collisionPrecision;
        
        boolean ret = false;
        
        setLocation(exactX + dx, exactY + dy);
        ret |= isTouching(cls);
        setLocation(exactX - dx, exactY - dy);
        
        return ret;
    }
    
    protected abstract void initAnimations();
    protected abstract void die();
}
