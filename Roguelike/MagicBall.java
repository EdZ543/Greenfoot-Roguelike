import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * A magic ball that homes in on the nearest target!
 * 
 * @author Eddie Zhuang
 * @version Jan. 24, 2022
 */
public class MagicBall extends Projectile
{
    private int homingFrequency = 10;
    private int homingCountdown = 10;
    
    public MagicBall(boolean playerShot, int rotation) {
        super(50, 50, playerShot, rotation, 10, 20, "bow_fire.wav", 100, 5);
    }
    
    /**
     * Act - do whatever the MagicBall wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        if (homingCountdown > 0) {
            homingCountdown--;
        }
        
        // Target nearest target every so often
        if (homingCountdown == 0) {
            homingCountdown = homingFrequency;
            
            if (playerShot) {
                targetNearest(Enemy.class);
            } else {
                targetNearest(Player.class);
            }
        }
        
        super.act();
    }
    
    /**
     * Turns towards nearest enemy or player
     */
    private void targetNearest(Class cls) {
        List<Actor> actors = getWorld().getObjects(cls);
        if (!actors.isEmpty()) {
            Actor targetActor = actors.get(0);
            
            // Get nearest enemy
            for (Actor actor : actors) {
                if (GameWorld.getDistance(this, actor) < GameWorld.getDistance(this, targetActor)) {
                    targetActor = actor;
                }
            }
            
            turnTowards(targetActor.getX(), targetActor.getY());
        }
    }
}
