import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Boss here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Boss extends Enemy
{
    private GreenfootImage runningFrames[];
    private Random random = new Random();
    private int attackDelay = 100;
    private int attackTimer = attackDelay;
    private int chargeDelay = 2;
    private int chargeTimer = chargeDelay;
    private boolean attacking = false;
    private int spawnTimer = 0, shootTimer = 0, chargingTimer = 0;
    
    public Boss(int width, int height) {
        super(width, height, 5, 200, 69420);
        
        setRotation(random.nextInt(360));
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
    
    protected void initAnimations() {
        runningFrames = new GreenfootImage[4];
        for (int i = 0; i < runningFrames.length; i++) {
            String framePath = "boss/run/big_demon_run_anim_f" + i + ".png";
            GreenfootImage frame = new GreenfootImage(framePath);
            GameWorld.scaleWithAspectRatio(frame, width, height);
            runningFrames[i] = frame;
        }
        
        animation = new Animation(this, "running", runningFrames, 20, "right");
        animation.setActiveState(true);
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
        } else if (isAdjacentTo(Wall.class)) {
            spreadShot();
            chargingTimer = 0;
            animation.setCycleActs(20);
        } else {
            move(speed * 3);
        }
    }
    
    private void spawnMinions() {
        if (spawnTimer == 0) spawnTimer = 50;
        else if (spawnTimer == 25) getWorld().addObject(new Goblin(64, -1), getX(), getY());
    }
    
    private void bounceAround() {
        move(speed);
        
        setLocation(exactX + collisionPrecision, exactY);
        if (isTouching(Wall.class)) bounce("left");
        setLocation(exactX - collisionPrecision, exactY);
        
        setLocation(exactX - collisionPrecision, exactY);
        if (isTouching(Wall.class)) bounce("right");
        setLocation(exactX + collisionPrecision, exactY);
        
        setLocation(exactX, exactY + collisionPrecision);
        if (isTouching(Wall.class)) bounce("up");
        setLocation(exactX, exactY - collisionPrecision);
        
        setLocation(exactX, exactY - collisionPrecision);
        if (isTouching(Wall.class)) bounce("down");
        setLocation(exactX, exactY + collisionPrecision);
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
