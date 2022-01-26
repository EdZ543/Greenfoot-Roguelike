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
    // Frames for animation, static so they only need to be generated once
    private static GreenfootImage idleFrames[];
    private static GreenfootImage runFrames[];
    
    // Punching sound
    private OverlappingSound punchSound = new OverlappingSound("punch.wav", 80);
    private int punchSoundsIndex = 0;
    
    private int visionRange = 500;
    private int attackRange = 30;
    private int attackDamage = 10;
    private int punchFrequency = 50; // How often the goblin can punch
    private int punchCountdown = 0;
    private double punchForce = 5.0; // Force applied on player when punched
    
    public Goblin() {
        super(30, 16, 0.3, 3.0);
        
        initAnimations();
    }
    
    /**
     * Initializes animations
     */
    private void initAnimations() {
            if (idleFrames == null) {
                idleFrames = Animation.generateFrames(0, 4, "goblin/goblin", ".png");
            }
            
            if (runFrames == null) {
                runFrames = Animation.generateFrames(4, 4, "goblin/goblin", ".png");
            }
            
            animation = new Animation(this, "idle", idleFrames, 40, "right");
            animation.addState("running", runFrames, 15, "right");
            animation.setActiveState(true);
    }
    
    public void act() {
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
                player.damageMe(attackDamage); // Damage player
                player.setForce(getAngleTo(player), punchForce); // Push player a bit
                punchSound.play(); // Play punching sound
                punchCountdown =  punchFrequency; // Reset punching cooldown
            }
        } else {
            turnTowards(player.getX(), player.getY());
            checkFacingDir(getExactRotation());
            addForce(speed);
        }
    }
}
