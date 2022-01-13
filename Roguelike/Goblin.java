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
    private int visionRange = 300;
    private int attackRange = 100;
    private int speed = 1;
    private int attackDamage = 1;
    
    public Goblin(int width, int height) {
        super(20);
        this.getImage().scale(width, height);
    }
    
    /**
     * Act - do whatever the Goblin wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        List<Player> players = getObjectsInRange(visionRange, Player.class);
        
        if (!players.isEmpty()) {
            moveTowardsOrAttackPlayer(players.get(0));
        }
    }
    
    private void moveTowardsOrAttackPlayer(Player player) {
        turnTowards(player.getX(), player.getY());
        
        if (GameWorld.getDistance(this, player) <= attackRange)
        {
            player.damageMe(attackDamage);
        }
        else
        {
            move (speed);
        }
    }
}
