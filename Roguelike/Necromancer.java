import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * A dangerous enemy that shoots deadly homing magic balls!
 * 
 * @author Eddie Zhuang
 * @version Jan. 24, 2022
 */
public class Necromancer extends Enemy
{
    // Frames for animation, static so they only need to be generated once
    private static GreenfootImage idleFrames[];
    
    private int shotFrequency = 120;
    private int shotCountdown = shotFrequency;
    
    public Necromancer() {
        super(100, 100, 0, 10.0);
        
        initAnimations();
    }
    
    /**
     * Initialize animations
     */
    private void initAnimations() {
        if (idleFrames == null) {
            idleFrames = Animation.generateFrames(0, 4, "necromancer/necromancer", ".png");
        }
        
        animation = new Animation(this, "idle", idleFrames, 50, "right");
        animation.setActiveState(true);
    }
    
    /**
     * Act - do whatever the Necromancer wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        animation.run();
        
        // Shoot at player
        if (shotCountdown > 0) {
            shotCountdown--;
        } else {
            shotCountdown = shotFrequency;
            shoot();
        }
    }
    
    /**
     * Fire a magical ball at the player!
     */
    private void shoot() {
        List<Player> players = getWorld().getObjects(Player.class);
        if (!players.isEmpty()) {
            Player p = players.get(0);
            int angleToPlayer = getAngleTo(p);
            checkFacingDir(angleToPlayer);
            getWorld().addObject(new MagicBall(false, angleToPlayer, 2, 100), getX(), getY());
        }
    }
}
