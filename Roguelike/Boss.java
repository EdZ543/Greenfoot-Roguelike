import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * The final boss!
 * Bounces around, periodically firing at the player or spawning minions.
 * Also sometimes charges at player. If it misses the player and hits a wall, releases arrows all around it.
 * 
 * @author Eddie Zhuang
 * @version Jan. 21, 2022
 */
public class Boss extends Enemy
{
    private Random random = new Random();
    private int attackDelay = 100;
    private int attackTimer = attackDelay;
    private int chargeDelay = 2;
    private int chargeTimer = chargeDelay;
    private boolean attacking = false;
    private int spawnTimer = 0, shootTimer = 0, chargingTimer = 0;
    private int chargingSpeed = 15;
    
    public Boss(int width, int height) {
        super(width, height, 5, 250, 200);
        
        initAnimations();
        
        // Rotates boss in random orientation
        setRotation(random.nextInt(360));
    }
    
    private void initAnimations() {
        GreenfootImage[] runFrames = Animation.generateFrames(4, "boss/run/big_demon_run_anim_f", ".png", width, height);
        animation = new Animation(this, "running", runFrames, 20, "right");
        animation.setActiveState(true);
    }
    
    public void act()
    {
        animation.run();
        
        System.out.println(chargeTimer);
        
        if (spawnTimer > 0) {
            spawnMinions();
            spawnTimer--;
        } else if (shootTimer > 0) {
            megaEpicAttack();
            shootTimer--;
        } else if (chargingTimer > 0) {
            charge();
        } else if (attackTimer == 0) {
            attackTimer = attackDelay;
            if (chargeTimer == 0) {
                chargeTimer = chargeDelay;
                charge();
            } else {
                int prob = random.nextInt(100);
                if (prob < 37) {
                    spawnMinions();
                } else {
                    megaEpicAttack();
                }
                chargeTimer--;
            }
        } else {
            bounceAround();
            attackTimer--;
        }
    }
    
    private void charge() {
        if (chargingTimer == 0) {
            chargingTimer = 100;
        } else if (chargingTimer >= 50) {
            Player player = ((GameWorld)getWorld()).getPlayer();
            turnTowards(player);
            animation.setCycleActs(5);
            chargingTimer--;
        } else if (isTouching(Player.class)) {
            Player player = ((GameWorld)getWorld()).getPlayer();
            player.damageMe(50);
            chargingTimer = 0;
            animation.setCycleActs(20);
        } else if (isTouching(Wall.class)) {
            spreadShot();
            chargingTimer = 0;
            animation.setCycleActs(20);
        } else {
            moveWithoutCollision(chargingSpeed);
        }
    }
    
    private void spawnMinions() {
        if (spawnTimer == 0) spawnTimer = 50;
        else if (spawnTimer == 25) getWorld().addObject(new Goblin(64, -1), getX(), getY());
    }
    
    private void bounceAround() {
        move(speed);
        
        setLocation(exactX + 1, exactY);
        if (isTouching(Wall.class)) bounce("left");
        setLocation(exactX - 1, exactY);
        
        setLocation(exactX - 1, exactY);
        if (isTouching(Wall.class)) bounce("right");
        setLocation(exactX + 1, exactY);
        
        setLocation(exactX, exactY + 1);
        if (isTouching(Wall.class)) bounce("up");
        setLocation(exactX, exactY - 1);
        
        setLocation(exactX, exactY - 1);
        if (isTouching(Wall.class)) bounce("down");
        setLocation(exactX, exactY + 1);
    }
    
    private void bounce(String dir) {
        double radians = Math.toRadians(rotation);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        
        if (dir == "left" || dir == "right") cos = -cos;
        else if (dir == "up" || dir == "down") sin = -sin;
        
        double newRadians = Math.atan2(sin, cos);
        setRotation(Math.toDegrees(newRadians));
    }
    
    private void megaEpicAttack() {
        if (shootTimer == 0) shootTimer = 50;
        else if (shootTimer == 25) {
            Player player = getWorld().getObjects(Player.class).get(0);
            int angleToPlayer = getAngleTo(player);
            getWorld().addObject(new HugeBaller(false, angleToPlayer), getX(), getY());
        }
    }
    
    private void spreadShot() {
        for (int i = 0; i < 360; i += 45) {
            getWorld().addObject(new Arrow(false, i), getX(), getY());
        }
    }
    
    protected void die() {
        GameWorld g = (GameWorld)getWorld();
        g.removeObject(this);
        g.updateScore(pointsValue);
        g.gameOver(true);
    }
}
