import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Skelebro here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Skelebro extends Enemy
{
    private GreenfootImage[] runningFrames;
    private int turnDelay = 50;
    private int turnTimer = 0;
    Random random = new Random();
    
    public Skelebro(int width, int height) {
        super(width, height, 2, 30, 60);
    }
    
    protected void initAnimations() {
        runningFrames = new GreenfootImage[4];
        for (int i = 0; i < runningFrames.length; i++) {
            String framePath = "skelebro/run/skelet_run_anim_f" + i + ".png";
            GreenfootImage frame = new GreenfootImage(framePath);
            
            GameWorld.scaleWithAspectRatio(frame, width, height);
            runningFrames[i] = frame;
        }
        
        animation = new Animation(this, runningFrames);
        animation.addState("running", runningFrames, 15, "right");
        animation.setState("running");
        animation.setActiveState(true);
    }
    
    public void act() {
        animation.run();
        
        if (turnTimer == 0) {
            turnTimer = turnDelay;
            setRotation(random.nextInt(360));
            shootAtPlayer();
        }
        turnTimer--;
        
        move(speed);
    }
    
    private void shootAtPlayer() {
        Player player = getWorld().getObjects(Player.class).get(0);
        int angleToPlayer = getAngleTo(player);
        getWorld().addObject(new Arrow(false, angleToPlayer), getX(), getY());
        checkFacingDir(angleToPlayer);
    }
}