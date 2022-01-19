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
        setRotation( Math.toDegrees(Math.atan2(y - getY() , x - getX())));
    }
    
    @Override
    public void move(int distance)
    {
        move((double)distance);
    }
    
    public void move(double distance)
    {
        double radians = Math.toRadians(rotation);
        double dx = Math.cos(radians) * distance;
        double dy = Math.sin(radians) * distance;
        setLocation(exactX + dx, exactY + dy);
        checkWalls();
    }
    
    @Override
    public void setRotation (int rotation){
        setRotation((double)rotation);
    }
    
    public void setRotation (double rotation){
        this.rotation = rotation;
        checkFacingDir();
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
    
    protected void checkFacingDir() {
        if (rotation > -90 && rotation < 90) {
            animation.setDir("right");
        } else if (rotation < -90 || rotation > 90) {
            animation.setDir("left");
        }
    }
    
    protected void checkWalls() {
        while (isTouching(Wall.class)) {
            move(-collisionPrecision);
        }
    }
    
    protected void damageMe(int attackDamage) {
        health = Math.max(0, health - attackDamage);
        stats.update(health);
        if (health == 0) {
            die();
        }
    }
    
    protected abstract void initAnimations();
    protected abstract void die();
}
