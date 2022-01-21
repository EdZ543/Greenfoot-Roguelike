import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * An enemy that shoots at the player and runs around randomly
 * 
 * @author Eddie Zhuang
 * @version Jan. 21, 2022
 */
public class Skelebro extends Enemy
{
    private Random random = new Random(); // java randomness for turning randomly
    
    private int turnFrequency = 50;
    private int turnCountdown = 1;
    private int shotFrequency = 70;
    private int shotCountdown = shotFrequency;
    
    public Skelebro(int width, int height) {
        super(width, height, 2, 30, 60);
        
        initAnimations();
    }
    
    /**
     * Initialize animations
     */
    protected void initAnimations() {
        GreenfootImage runningFrames[] = Animation.generateFrames(4, "skelebro/run/skelet_run_anim_f", ".png", width, height);
        animation = new Animation(this, "running", runningFrames, 15, "right");
        animation.setActiveState(true);
    }
    
    public void act() {
        // Run animation
        animation.run();
        
        // Turn a random direction every so often
        turnCountdown--;
        
        if (turnCountdown == 0) {
            turnRandomly();
            
            //Reset turning countdown
            turnCountdown = turnFrequency;
        }
        
        // Shoot at player every so often
        shotCountdown--;
        
        if (shotCountdown == 0) {
            shootAtPlayer();
            
            // Reset shooting countdown
            shotCountdown = shotFrequency;
        }
        
        // Move in direction currently facing
        move(speed);
    }
    
    /**
     * Turns skelebro in a random direction. Helps him dodge the player and be unpredictable.
     */
    private void turnRandomly() {
        setRotation(random.nextInt(360));
    }
    
    /**
     * Shoot an arrow at the player
     */
    private void shootAtPlayer() {
        List<Player> players = getWorld().getObjects(Player.class);
        
        if (!players.isEmpty()) {
            // Gets angle of skelebro to player
            Player p = players.get(0);
            int angleToPlayer = getAngleTo(p);
            
            // Face player when shooting at it
            checkFacingDir(angleToPlayer);
            
            // Shoot arrow
            Arrow arrow = new Arrow(false, angleToPlayer);
            getWorld().addObject(arrow, getX(), getY());
        }
    }
}