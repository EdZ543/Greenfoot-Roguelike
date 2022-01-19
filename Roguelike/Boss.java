import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Boss here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Boss extends Enemy
{
    private GreenfootImage runningFrames[];
    private Random random = new Random();
    
    public Boss(int width, int height) {
        super(width, height, 5, 100, 69420);
        
        setRotation(random.nextInt(360));
    }
    
    public void act()
    {
        animation.run();
        bounceAround();
    }
    
    protected void initAnimations() {
        runningFrames = new GreenfootImage[4];
        for (int i = 0; i < runningFrames.length; i++) {
            String framePath = "boss/run/big_demon_run_anim_f" + i + ".png";
            GreenfootImage frame = new GreenfootImage(framePath);
            GameWorld.scaleWithAspectRatio(frame, width, height);
            runningFrames[i] = frame;
        }
        
        animation = new Animation(this);
        animation.addState("running", runningFrames, 20, "right");
        animation.setState("running");
        
        animation.setActiveState(true);
    }
    
    private void bounceAround() {
        move(speed);
        
        setLocation(exactX + collisionPrecision, exactY);
        if (isTouching(Wall.class)) bounce("left");
        setLocation(exactX - collisionPrecision, exactY);
        
        setLocation(exactX - collisionPrecision, exactY);
        if (isTouching(Wall.class)) bounce("right");
        setLocation(exactX + collisionPrecision, exactY);
        
        setLocation(exactX, exactY + collisionPrecision);
        if (isTouching(Wall.class)) bounce("up");
        setLocation(exactX, exactY - collisionPrecision);
        
        setLocation(exactX, exactY - collisionPrecision);
        if (isTouching(Wall.class)) bounce("down");
        setLocation(exactX, exactY + collisionPrecision);
    }
    
    private void bounce(String dir) {
        double radians = Math.toRadians(rotation);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        
        if (dir == "left" || dir == "right") cos = -cos;
        else if (dir == "up" || dir == "down") sin = -sin;
        
        double newRadians = Math.atan2(sin, cos);
        setRotation(Math.toDegrees(newRadians));
    }
    
    private void spreadShot() {
        for (int i = 0; i < 360; i += 45) {
            Arrow arrow = new Arrow(false, i);
            getWorld().addObject(arrow, getX(), getY());
        }
    }
}
