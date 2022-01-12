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
    private int attackRange = 50;
    private int speed = 1;
    private int attackDamage = 10;
    
    public Goblin(int width, int height) {
        super(width, height, 20, "goblin_idle_anim_f0.png");
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
            player.getAttacked(attackDamage);
        }
        else
        {
            move (speed);
        }
    }
}
