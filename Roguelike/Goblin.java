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
    private int attackDamage = 1;
    
    public Goblin(int width, int height) {
        super(width, height, 1, 50, 16);
    }
    
    public void act()
    {
        List<Player> players = getObjectsInRange(visionRange, Player.class);
        
        if (!players.isEmpty()) {
            moveTowardsOrAttackPlayer(players.get(0));
        }
    }
    
    protected void initAnimations() {
        
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
