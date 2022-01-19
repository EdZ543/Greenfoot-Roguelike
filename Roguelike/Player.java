import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Player class. This is the actor that the user controls.
 * 
 * @author Eddie Zhuang
 * @version (a version number or a date)
 */
public class Player extends Entity
{
    private GreenfootImage[] idleFrames;
    private GreenfootImage[] runningFrames;
    private GreenfootSound[] shootSounds;
    
    private int shootDelay = 20;
    private int shootDelayTimer = 0;
    private int shootSoundsIndex = 0;
    
    public Player (int width, int height) {
        super(width, height, 5, 100);
        
        shootSounds = new GreenfootSound[20];
        for (int i = 0; i < shootSounds.length; i++) {
            shootSounds[i] = new GreenfootSound("bow_fire.wav");
        }
    }
    
    protected void initAnimations() {
        idleFrames = new GreenfootImage[4];
        for (int i = 0; i < idleFrames.length; i++) {
            String framePath = "player/idle/knight_f_idle_anim_f" + i + ".png";
            GreenfootImage frame = new GreenfootImage(framePath);
            
            GameWorld.scaleWithAspectRatio(frame, width, height);
            idleFrames[i] = frame;
        }
        
        runningFrames = new GreenfootImage[4];
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
        stats = new StatBar​(maxHealth, health, null, 150, 25, 0, Color.GREEN, Color.RED, false);
        
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
        
        if (door != null && !door.isLocked()) {
            GameWorld g = (GameWorld)getWorld();
        
            g.exitRoom(door.getDir());
        }
    }
    
    private void checkKeys() {
        String key = Greenfoot.getKey();
        
        if (Greenfoot.isKeyDown("W") || Greenfoot.isKeyDown("A") || Greenfoot.isKeyDown("S") || Greenfoot.isKeyDown("D")) {
            animation.setState("running");
        } else {
            animation.setState("idle");
        }
        
        if (Greenfoot.isKeyDown("W")){
            setRotation(Math.toRadians(270));
            move(speed);
        }
        
        if (Greenfoot.isKeyDown("A")){
            animation.setDir("left");
            setRotation(Math.toRadians(180));
            move(speed);
        }
        
        if (Greenfoot.isKeyDown("S")){
            setRotation(Math.toRadians(90));
            move(speed);
        }
        
        if (Greenfoot.isKeyDown("D")){
            animation.setDir("right");
            setRotation(Math.toRadians(0));
            move(speed);
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
        Arrow arrow = new Arrow(true, rotation);
        getWorld().addObject(arrow, getX(), getY());
        
        shootSounds[shootSoundsIndex++].play();
        shootSoundsIndex %= shootSounds.length;
    }
}
