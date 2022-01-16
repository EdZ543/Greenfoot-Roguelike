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
    
    public Entity(int width, int height, int speed, int health) {
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.health = health;
        
        initAnimations();
    }
    
    protected void moveInDir(float angle) {
        int xVel = (int)Math.cos(Math.toRadians(angle));
        int yVel = (int)Math.sin(Math.toRadians(angle));
        
        for(int i = 0; i < speed; i++){
            setLocation(getX() + xVel, getY() + yVel);
                
            if(isTouching(Wall.class)){
                setLocation(getX() - xVel, getY() - yVel);
                break;
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
