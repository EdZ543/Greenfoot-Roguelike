import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Player class. This is the actor that the user controls.
 * 
 * @author Eddie Zhuang
 * @version (a version number or a date)
 */
public class Player extends Entity
{
    // File paths to animation frames
    private String[] idlePrefixes = new String[]{"knight/idle/knight_f_idle_anim_f", "female elf/idle/elf_f_idle_anim_f", "male elf/idle/elf_m_idle_anim_f"};
    private String[] runPrefixes = new String[]{"knight/run/knight_f_run_anim_f", "female elf/run/elf_f_run_anim_f", "male elf/run/elf_m_run_anim_f"};
    
    private int shootDelay = 20;
    private int shootDelayTimer = 0;
    private String key;
    
    // If true, doesn't spawn health bar and isn't controlled by keys
    // For previewing player, like on the start screen
    private boolean previewMode;
    
    public Player (int width, int height, int characterSelection) {
        this(width, height, characterSelection, false);
    }
    
    /**
     * Extra constructor for creating a preview player
     */
    public Player (int width, int height, int characterSelection, boolean previewMode) {
        super(width, height, 100, 1, 6.0);
        
        this.previewMode = previewMode;

        initAnimations(characterSelection);
    }
    
    /**
     * Returns number of character models available
     */
    public int getNumCharacters() {
        return idlePrefixes.length;
    }
    
    /**
     * Initializes animations
     */
    private void initAnimations(int characterSelection) {
        GreenfootImage[] idleFrames = Animation.generateFrames(4, idlePrefixes[characterSelection], ".png", width, height);
        GreenfootImage[] runningFrames = Animation.generateFrames(4, runPrefixes[characterSelection], ".png", width, height);
            
        animation = new Animation(this, "idle", idleFrames, 40, "right");
        animation.addState("running", runningFrames, 15, "right");
        animation.setActiveState(true);
    }
    
    public void addedToWorld(World w) {
        // Spawns health bar
        if (!previewMode) {
            stats = new StatBar​(maxHealth, health, null, 150, 25, 0, Color.GREEN, Color.RED, false);
            w.addObject(stats, stats.getImage().getWidth() / 2, stats.getImage().getHeight() / 2);
        }
    }
    
    /**
     * Called when player dies
     */
    protected void die() {
        GameWorld g = (GameWorld)getWorld();
        g.gameOver(false); // Go to game over screen
    }
    
    /**
     * Act - do whatever the Player wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        animation.run();
        applyPhysics();
        
        if (previewMode) return;
        
        key = Greenfoot.getKey();
        checkMove();
        checkShoot();
        checkItem();
        
        // Check if entered door
        checkDoor();
    }
    
    /**
     * Check for items
     */
    private void checkItem() {
        Chest chest = (Chest)getOneIntersectingObject(Chest.class);
        if (chest != null && chest.isClosed()) {
            chest.open();
        }
        
        Item item = (Item)getOneIntersectingObject(Item.class);
        if (item != null && Greenfoot.isKeyDown("P")) {
            item.use(this);
        }
    }
    
    /**
     * Checks whether player is adjacent to door in direction they're facing
     */
    private void checkDoor() {
        double radians = Math.toRadians(getExactRotation());
        double dx = Math.cos(radians);
        double dy = Math.sin(radians);
        
        int halfWidth = (int)(Math.ceil((double)getImage().getWidth() / 2) + 1);
        int halfHeight = (int)(Math.ceil((double)getImage().getHeight() / 2) + 1);
        
        // Checks point adjacent to player in direction they're facing
        Door door = (Door)getOneObjectAtOffset((int)dx * halfWidth, (int)dy * halfHeight, Door.class);
        
        // If door is unlocked, go to next room in door's direction
        if (door != null && !door.isLocked()){
            GameWorld g = (GameWorld)getWorld();
            g.exitRoom(door.getDir());
        }
    }
    
    /**
     * Check movement keys
     */
    private void checkMove() {
        if (Greenfoot.isKeyDown("W") && Greenfoot.isKeyDown("D")){
            animation.setDir("right");
            setRotation(315);
        } else if (Greenfoot.isKeyDown("D") && Greenfoot.isKeyDown("S")){
            animation.setDir("right");
            setRotation(45);
        } else if (Greenfoot.isKeyDown("S") && Greenfoot.isKeyDown("A")){
            animation.setDir("left");
            setRotation(135);
        } else if (Greenfoot.isKeyDown("A") && Greenfoot.isKeyDown("W")){
            animation.setDir("left");
            setRotation(225);
        } else if (Greenfoot.isKeyDown("W")){
            setRotation(270);
        } else if (Greenfoot.isKeyDown("A")){
            animation.setDir("left");
            setRotation(180);
        } else if (Greenfoot.isKeyDown("S")){
            setRotation(90);
        } else if (Greenfoot.isKeyDown("D")){
            animation.setDir("right");
            setRotation(0);
        }
        
        if (Greenfoot.isKeyDown("W") || Greenfoot.isKeyDown("A") || Greenfoot.isKeyDown("S") || Greenfoot.isKeyDown("D")) {
            animation.setState("running");
            addForce(speed);
        } else {
            animation.setState("idle");
        }
    }
    
    /**
     * Check arrow keys for shooting
     */
    private void checkShoot() {
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
    
    /**
     * Shoot a projectile in specified direction
     */
    private void shoot(int rotation) {
        Arrow arrow = new Arrow(true, rotation);
        getWorld().addObject(arrow, getX(), getY());
    }
}
