import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Player class. This is the actor that the user controls.
 * 
 * @author Eddie Zhuang
 * @version (a version number or a date)
 */
public class Player extends Entity
{
    private static GreenfootImage[] runningFrames = new GreenfootImage[4];
    private static GreenfootImage[] idleFrames = new GreenfootImage[4];
    
    private int shootDelay = 20;
    private int shootDelayTimer = 0;
    
    public Player (int width, int height) {
        super(width, height, 5, 100);
    }
    
    protected void initAnimations() {
        for (int i = 0; i < idleFrames.length; i++) {
            String framePath = "player/idle/knight_f_idle_anim_f" + i + ".png";
            GreenfootImage frame = new GreenfootImage(framePath);
            
            GameWorld.scaleWithAspectRatio(frame, width, height);
            idleFrames[i] = frame;
        }
        
        for (int i = 0; i < runningFrames.length; i++) {
            String framePath = "player/run/knight_f_run_anim_f" + i + ".png";
            GreenfootImage frame = new GreenfootImage(framePath);
            
            GameWorld.scaleWithAspectRatio(frame, width, height);
            runningFrames[i] = frame;
        }
        
        animation = new Animation(this, runningFrames);
        animation.addState("idle", idleFrames, 40, "right");
        animation.addState("running", runningFrames, 15, "right");
        animation.setState("idle");
        animation.setActiveState(true);
    }
    
    public void addedToWorld(World w) {
        stats = new StatBar​(health, health, null, 150, 25, 0, Color.GREEN, Color.RED, false);
        
        w.addObject(stats, 100, 25);
    }
    
    protected void die() {
        GameWorld g = (GameWorld)getWorld();
        g.gameOver(false);
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
        checkDoor();
    }
    
    private void checkDoor() {
        Door door = (Door)getOneIntersectingObject(Door.class);
        
        if (door != null) {
            GameWorld g = (GameWorld)getWorld();
        
            g.exitRoom(door.getDir());
        }
    }
    
    private void checkKeys() {
        String key = Greenfoot.getKey();
        
        if (!Greenfoot.isKeyDown("W") && !Greenfoot.isKeyDown("A") && !Greenfoot.isKeyDown("S") && !Greenfoot.isKeyDown("D")) {
            animation.setState("idle");
        } else {
            animation.setState("running");
        }
        
        if (Greenfoot.isKeyDown("W")){
            moveInDir(270);
        }
        
        if (Greenfoot.isKeyDown("A")){
            moveInDir(180);
            animation.setDir("left");
        }
        
        if (Greenfoot.isKeyDown("S")){
            moveInDir(90);
        }
        
        if (Greenfoot.isKeyDown("D")){
            moveInDir(0);
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
    
    private void shoot(int rotation) {
        Bullet bullet = new Bullet(true, rotation);
        getWorld().addObject(bullet, getX(), getY());
    }
}
