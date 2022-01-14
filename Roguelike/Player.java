import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Player class. This is the actor that the user controls.
 * 
 * @author Eddie Zhuang
 * @version (a version number or a date)
 */
public class Player extends Actor
{
    static GreenfootImage[] runningFrames = new GreenfootImage[4];
    static GreenfootImage[] idleFrames = new GreenfootImage[4];
    
    private GreenfootImage image;
    private StatBar stats;
    private Animation animation;
    
    private int speed = 6;
    private int health = 100;
    private int shootDelay = 20;
    private int shootDelayTimer = 0;
    private int width;
    private int height;
    
    public Player (int width, int height) {
        this.width = width;
        this.height = height;
        
        this.getImage().scale(width, height);
        
        stats = new StatBar​(health, health, null, 150, 25, 0, Color.GREEN, Color.RED, false);
        
        initAnimations();
    }
    
    private void initAnimations() {
        for (int i = 0; i < idleFrames.length; i++) {
            String framePath = "player/idle/knight_f_idle_anim_f" + i + ".png";
            GreenfootImage frame = new GreenfootImage(framePath);
            
            frame.scale(width, height);
            idleFrames[i] = frame;
        }
        
        for (int i = 0; i < runningFrames.length; i++) {
            String framePath = "player/run/knight_f_run_anim_f" + i + ".png";
            GreenfootImage frame = new GreenfootImage(framePath);
            
            frame.scale(width, height);
            runningFrames[i] = frame;
        }
        
        animation = new Animation(this, runningFrames);
        animation.addState("idle", idleFrames, 40, "right");
        animation.addState("running", runningFrames, 15, "right");
        animation.setState("idle");
        animation.setActiveState(true);
    }
    
    public void addedToWorld (World w) {
        w.addObject(stats, 100, 50);
    }
    
    /**
     * Act - do whatever the Player wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
        animation.run();
        checkKeys();
        checkExit();
    }
    
    private void checkExit() {
        GameWorld g = (GameWorld)getWorld();
        
        if (getX() <= 0) g.exitRoom("left");
        else if (getX() >= getWorld().getWidth() - 1) g.exitRoom("right");
        else if (getY() <= 0) g.exitRoom("up");
        else if (getY() >= getWorld().getHeight() - 1) g.exitRoom("down");
    }
    
    private void checkKeys() {
        String key = Greenfoot.getKey();
        
        if (!Greenfoot.isKeyDown("W") && !Greenfoot.isKeyDown("A") && !Greenfoot.isKeyDown("S") && !Greenfoot.isKeyDown("D")) {
            animation.setState("idle");
        } else {
            animation.setState("running");
        }
        
        if (Greenfoot.isKeyDown("W")){
            moveInDir(0, -1);
        }
        
        if (Greenfoot.isKeyDown("A")){
            moveInDir(-1, 0);
            animation.setDir("left");
        }
        
        if (Greenfoot.isKeyDown("S")){
            moveInDir(0, 1);
        }
        
        if (Greenfoot.isKeyDown("D")){
            moveInDir(1, 0);
            animation.setDir("right");
        }
        
        if (shootDelayTimer != 0) {
            shootDelayTimer--;
        }
        
        if (shootDelayTimer == 0) {
            if (Greenfoot.isKeyDown("Up")){
                shoot(270);
                shootDelayTimer = shootDelay;
            } else if (Greenfoot.isKeyDown("Down")){
                shoot(90);
                shootDelayTimer = shootDelay;
            } else if (Greenfoot.isKeyDown("Left")){
                shoot(180);
                shootDelayTimer = shootDelay;
            } else if (Greenfoot.isKeyDown("Right")){
                shoot(0);
                shootDelayTimer = shootDelay;
            }
        }
    }
    
    private void moveInDir(int x, int y) {
        for(int i = 0; i < speed; i++){
            setLocation(getX() + x, getY() + y);
                
            if(isTouching(Wall.class)){
                setLocation(getX() - x, getY() - y);
                break;
            }
        }
    }
    
    /**
     * Moves in increments for smooth wall collision
     */
    private void move(){
        for(int i = 0; i < speed; i++){
                move(1);
                
            if(isTouching(Wall.class)){
                move(-1);
                break;
            }
        }
    }
    
    private void shoot(int rotation) {
        Bullet bullet = new Bullet(true, rotation);
        getWorld().addObject(bullet, getX(), getY());
    }
    
    /**
     * Attack the player.
     */
    public void damageMe(int attackDamage) {
        health = Math.max(0, health - attackDamage);
        stats.update(health);
        if (health == 0) {
            GameWorld g = (GameWorld)getWorld();
            g.gameOver();
        }
    }
}
