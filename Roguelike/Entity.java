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
    
    protected int speed;
    protected int health;
    protected int width;
    protected int height;
    protected double rotation = 0;
    protected int stepWidth = 1;
    
    public Entity(int width, int height, int speed, int health) {
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.health = health;
        
        initAnimations();
    }
    
    protected void moveInDir(double angle) {
        int xVel = (int)Math.cos(angle);
        int yVel = (int)Math.sin(angle);
        
        for(int i = 0; i < speed; i++){
            setLocation(getX() + xVel, getY() + yVel);
                
            if(isTouching(Wall.class)){
                setLocation(getX() - xVel, getY() - yVel);
                break;
            }
        }
    }
    
    public void setRotation(int rotation) {
        this.rotation = Math.toRadians(rotation);
    }
    
    public void turnTowards(int x, int y) {
        rotation = Math.atan2(y - getY(), x - getX());
        System.out.println(rotation);
    }
    
    public void move(int distance) {
        int dx = (int)Math.round(Math.cos((double)rotation) * stepWidth);
        int dy = (int)Math.round(Math.sin((double)rotation) * stepWidth);
        
        for(int i = 0; i < distance; i++){
            setLocation(getX() + dx, getY());
                
            if(isTouching(Wall.class)){
                setLocation(getX() - dx, getY());
            }
            
            setLocation(getX(), getY() + dy);
                
            if(isTouching(Wall.class)){
                setLocation(getX(), getY() - dy);
            }
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
