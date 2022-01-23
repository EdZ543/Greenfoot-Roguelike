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
    
    private int betweenAttacksDelay = 100; // Delay between each attack
    private int betweenAttacksTimer = betweenAttacksDelay;
    
    private int chargeDelay = 2; // number of attacks until charge
    private int chargeTimer = chargeDelay;
    private int chargeSpeed = 15;
    private int chargeDamage = 50;
    
    private int attackTimer = 0; // timer used during all attacks for timing
    private String curAttack = "none"; // attack currently doing
    
    public Boss(int width, int height) {
        super(width, height, 250, 200, 3.0, 5.0);
        
        initAnimations();
        
        // Rotates boss in random orientation
        setRotation(random.nextInt(360));
    }
    
    /**
     * Initializes animation
     */
    private void initAnimations() {
        GreenfootImage[] runFrames = Animation.generateFrames(4, "boss/run/big_demon_run_anim_f", ".png", width, height);
        animation = new Animation(this, "running", runFrames, 20, "right");
        animation.setActiveState(true);
    }
    
    public void act()
    {
        applyPhysics();
        animation.run();
        
        // Calls different methods depending on current attack
        if (curAttack == "spawning") {
            spawn();
        } else if (curAttack == "shooting") {
            shoot();
        } else if (curAttack == "charging") {
            charge();
        } else if (betweenAttacksTimer == 0) {
            betweenAttacksTimer = betweenAttacksDelay;
            if (chargeTimer == 0) { // Every chargeDelay attack, the attack will be a charge. If not, it's shooting or spawning minions.
                chargeTimer = chargeDelay;
                curAttack = "charging";
                charge();
            } else {
                // 37% chance of spawning enemies
                // 63% chance of shooting
                int prob = random.nextInt(100);
                if (prob < 37) {
                    curAttack = "spawning";
                    spawn();
                } else {
                    curAttack = "shooting";
                    shoot();
                }
                chargeTimer--;
            }
        } else {
            bounceAround();
            betweenAttacksTimer--;
        }
    }
    
    /**
     * Charge at player!
     */
    private void charge() {
        if (attackTimer == 0) { // At start, turn towards player, and start "charging up" (faster running animation)
            attackTimer = 80;
            Player player = getWorld().getObjects(Player.class).get(0);
            turnTowards(player);
            animation.setCycleActs(5);
        } else if (attackTimer > 1) {
            attackTimer--;
        } else { // After 80 runs, start charging at player extra fast!
            move(chargeSpeed);
            
            Player p = (Player)getOneIntersectingObject(Player.class);
            
            if (p != null) { // If it runs into the player, damage them
                p.damageMe(chargeDamage);
                p.setForce(getExactRotation(), 10.0);
                animation.setCycleActs(20);
                attackTimer = 0;
                curAttack = "none";
            } else if (isTouching(Wall.class)) { // If it runs into a wall, let out a huge explosion of arrows!
                move(-chargeSpeed);
                spreadShot();
                animation.setCycleActs(20);
                attackTimer = 0;
                curAttack = "none";
            }
        }
    }
    
    /**
     * Spawn some goblin minions to help hunt down the player
     */
    private void spawn() {
        if (attackTimer == 0) {
            attackTimer = 50;
        } else if (attackTimer == 25) {
            for (int angle = 0; angle < 360; angle += 90) {
                Goblin goblin = new Goblin(64, 64);
                getWorld().addObject(goblin, getX(), getY());
                goblin.setForce(angle, 5.0);
            }
        }
        
        attackTimer--;
        if (attackTimer == 0) {
            curAttack = "none";
        }
    }
    
    /**
     * Move around and bounce off the walls
     */
    private void bounceAround() {
        // Checks which direction to bounce
        setLocation(getX() + 1, getY());
        if (isTouching(Wall.class)) bounce("left");
        setLocation(getX() - 1, getY());
        
        setLocation(getX() - 1, getY());
        if (isTouching(Wall.class)) bounce("right");
        setLocation(getX() + 1, getY());
        
        setLocation(getX(), getY() + 1);
        if (isTouching(Wall.class)) bounce("up");
        setLocation(getX(), getY() - 1);
        
        setLocation(getX(), getY() - 1);
        if (isTouching(Wall.class)) bounce("down");
        setLocation(getX(), getY() + 1);
        
        addForce(speed);
    }
    
    /**
     * Depending on the bouncing direction, change angle of rotation accordingly
     */
    private void bounce(String dir) {
        double radians = Math.toRadians(getExactRotation());
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        
        if (dir == "left" || dir == "right") cos = -cos;
        else if (dir == "up" || dir == "down") sin = -sin;
        
        double newRadians = Math.atan2(sin, cos);
        setRotation(Math.toDegrees(newRadians));
    }
    
    /**
     * Shoot a large projectile at the player!
     */
    private void shoot() {
        if (attackTimer == 0) {
            attackTimer = 50;
        }  else if (attackTimer == 25) {
            List<Player> players = getWorld().getObjects(Player.class);
            
            if (!players.isEmpty()) {
                Player p = players.get(0);
                int angleToPlayer = getAngleTo(p);
                checkFacingDir(angleToPlayer);
                getWorld().addObject(new HugeBaller(false, angleToPlayer), getX(), getY());
            }
        }
        
        attackTimer--;
        if (attackTimer == 0) {
            curAttack = "none";
        }
    }
    
    /**
     * Send a wave of arrows all around!
     */
    private void spreadShot() {
        for (int i = 0; i < 360; i += 45) {
            getWorld().addObject(new Arrow(false, i), getX(), getY());
        }
    }
    
    /**
     * Method called when boss is defeated
     */
    protected void die() {
        GameWorld g = (GameWorld)getWorld();
        g.removeObject(this);
        g.updateScore(pointsValue);
        g.gameOver(true); // If you beat the boss, you win the game!
    }
}
