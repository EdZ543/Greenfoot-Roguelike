import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Goblin here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Goblin extends Enemy
{
    private GreenfootImage[] idleFrames;
    private GreenfootImage[] runningFrames;
    
    private int visionRange = 300;
    private int attackRange = 25;
    private int attackDamage = 1;
    
    public Goblin(int width, int height) {
        super(width, height, 1, 30, 16);
    }
    
    public void act()
    {
        animation.run();
        
        checkPlayer();
    }
    
    protected void initAnimations() {
        idleFrames = new GreenfootImage[4];
        for (int i = 0; i < idleFrames.length; i++) {
            String framePath = "goblin/idle/goblin_idle_anim_f" + i + ".png";
            GreenfootImage frame = new GreenfootImage(framePath);
            
            GameWorld.scaleWithAspectRatio(frame, width, height);
            idleFrames[i] = frame;
        }
        
        runningFrames = new GreenfootImage[4];
        for (int i = 0; i < runningFrames.length; i++) {
            String framePath = "goblin/run/goblin_run_anim_f" + i + ".png";
            GreenfootImage frame = new GreenfootImage(framePath);
            
            GameWorld.scaleWithAspectRatio(frame, width, height);
            runningFrames[i] = frame;
        }
        
        animation = new Animation(this, "idle", idleFrames, 40, "right");
        animation.addState("running", runningFrames, 15, "right");
        animation.setActiveState(true);
    }
    
    private void checkPlayer() {
        List<Player> players = getObjectsInRange(visionRange, Player.class);
        
        if (!players.isEmpty()) {
            moveTowardsOrAttackPlayer(players.get(0));
            animation.setState("running");
        } else {
            animation.setState("idle");
        }
    }
    
    private void moveTowardsOrAttackPlayer(Player player) {
        if (GameWorld.getDistance(this, player) <= attackRange)
        {
            player.damageMe(attackDamage);
        }
        else
        {
            turnTowards(player.getX(), player.getY());
            move(speed);
        }
    }
}
