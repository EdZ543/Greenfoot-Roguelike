import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Player class. This is the actor that the user controls.
 * 
 * @author Eddie Zhuang
 * @version (a version number or a date)
 */
public class Player extends Entity
{
    private String[] idlePrefixes = new String[]{"knight/idle/knight_f_idle_anim_f", "female elf/idle/elf_f_idle_anim_f", "male elf/idle/elf_m_idle_anim_f"};
    private String[] runPrefixes = new String[]{"knight/run/knight_f_run_anim_f", "female elf/run/elf_f_run_anim_f", "male elf/run/elf_m_run_anim_f"};
    
    private GreenfootSound[] shootSounds;
    private int shootSoundsIndex = 0;
    
    private int shootDelay = 20;
    private int shootDelayTimer = 0;
    private boolean previewMode;
    
    public Player (int width, int height, int characterSelection) {
        this(width, height, characterSelection, false);
    }
    
    /**
     * Extra constructor for creating a preview player, e.g. on the start screen
     */
    public Player (int width, int height, int characterSelection, boolean previewMode) {
        super(width, height, 5, 100);
        
        this.previewMode = previewMode;
        initSounds();
        initAnimations(characterSelection);
    }
    
    /**
     * Returns number of character models available
     */
    public int getNumCharacters() {
        return idlePrefixes.length;
    }
    
    private void initAnimations(int characterSelection) {
        GreenfootImage[] idleFrames = Animation.generateFrames(4, idlePrefixes[characterSelection], ".png", width, height);
        GreenfootImage[] runningFrames = Animation.generateFrames(4, runPrefixes[characterSelection], ".png", width, height);
            
        animation = new Animation(this, "idle", idleFrames, 40, "right");
        animation.addState("running", runningFrames, 15, "right");
        animation.setActiveState(true);
    }
    
    private void initSounds() {
        shootSounds = new GreenfootSound[20];
        for (int i = 0; i < shootSounds.length; i++) {
            shootSounds[i] = new GreenfootSound("bow_fire.wav");
        }
    }
    
    public void addedToWorld(World w) {
        if (!previewMode) {
            stats = new StatBar​(maxHealth, health, null, 150, 25, 0, Color.GREEN, Color.RED, false);
            w.addObject(stats, stats.getImage().getWidth() / 2, stats.getImage().getHeight() / 2);
        }
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
        animation.run();
        
        // Don't move if in preview mode
        if (!previewMode) {
            checkKeys();
        }
        
        checkDoor();
    }
    
    private void checkDoor() {
        double radians = Math.toRadians(rotation);
        int halfWidth = getImage().getWidth() / 2 + (int)Math.ceil(collisionPrecision);
        int halfHeight = getImage().getHeight() / 2 + (int)Math.ceil(collisionPrecision);
        int dx = (int)Math.cos(radians) * halfWidth;
        int dy = (int)Math.sin(radians) * halfHeight;
        
        Door door = (Door)getOneObjectAtOffset(dx, dy, Door.class);
        
        if (door != null && !door.isLocked()){
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
            setRotation(270);
            move(speed);
        }
        
        if (Greenfoot.isKeyDown("A")){
            animation.setDir("left");
            setRotation(180);
            move(speed);
        }
        
        if (Greenfoot.isKeyDown("S")){
            setRotation(90);
            move(speed);
        }
        
        if (Greenfoot.isKeyDown("D")){
            animation.setDir("right");
            setRotation(0);
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
