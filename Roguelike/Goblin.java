import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Pretty much the base enemy. Runs at player and attacks them if they're near.
 * 
 * @author Eddie Zhuang
 * @version Jan. 21, 2022
 */
public class Goblin extends Enemy
{
    // Punching sound
    private GreenfootSound[] punchSounds;
    private int punchSoundsIndex = 0;
    
    private int visionRange = 500;
    private int attackRange = 25;
    private int attackDamage = 10;
    private int punchFrequency = 50; // How often the goblin can punch
    private int punchCountdown = 0;
    
    public Goblin(int width, int height) {
        super(width, height, 30, 16, 0.3, 3.0);
        
        initAnimations();
        initSounds();
    }
    
    /**
     * Initializes animations
     */
    private void initAnimations() {
        GreenfootImage idleFrames[] = Animation.generateFrames(0, 4, "goblin/idle/goblin_idle_anim_f", ".png", width, height);
        GreenfootImage runFrames[] = Animation.generateFrames(0, 4, "goblin/run/goblin_run_anim_f", ".png", width, height);
        
        animation = new Animation(this, "idle", idleFrames, 40, "right");
        animation.addState("running", runFrames, 15, "right");
        animation.setActiveState(true);
    }
    
    /**
     * Initializes sounds
     */
    private void initSounds() {
        punchSounds = new GreenfootSound[20];
        for (int i = 0; i < punchSounds.length; i++) {
            punchSounds[i] = new GreenfootSound("punch.wav");
            punchSounds[i].setVolume(75);
        }
    }
    
    public void act()
    {
        applyPhysics();
        animation.run();
        
        // Cooldown so the goblin doesn't punch too often
        if (punchCountdown > 0) {
            punchCountdown--;
        }
        
        checkPlayer();
    }

    /**
     * Checks if player is in range of vision. If it is, charge at it
     */
    private void checkPlayer() {
        List<Player> players = getObjectsInRange(visionRange, Player.class);
        
        if (!players.isEmpty()) {
            Player p = players.get(0);
            moveTowardsOrAttackPlayer(p);
            animation.setState("running");
        } else {
            animation.setState("idle");
        }
    }
    
    /**
     * If player is in attack range, attack player. If not, move towards player.
     */
    private void moveTowardsOrAttackPlayer(Player player) {
        if (GameWorld.getDistance(this, player) <= attackRange) {
            // Damage the player, only if cooldown is over
            if (punchCountdown == 0) {
                player.damageMe(attackDamage);
                
                // Play punching sound
                punchSounds[punchSoundsIndex++].play();
                punchSoundsIndex %= punchSounds.length;
                
                // Resets cooldown
                punchCountdown =  punchFrequency;
            }
        } else {
            turnTowards(player.getX(), player.getY());
            checkFacingDir(getExactRotation());
            addForce(speed);
        }
    }
}
