import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * The Player class. This is the actor that the user controls.
 * 
 * @author Eddie Zhuang
 * @version (a version number or a date)
 */
public class Player extends Entity
{
    // Frames for animation, static so they only need to be generated once
    private static GreenfootImage idleFrames[][];
    private static GreenfootImage runFrames[][];
    
    // File paths to animation frames
    private static String[] framePrefixes = new String[]{"knight/knight", "female elf/elf_female", "male elf/elf_male"};
    
    private static Weapon weapon; // Weapon player is currently holding
    private static Label promptText; // Text prompting player to pick up an item

    private String key;
    private int pickupFrequency = 50; // Makes sure you don't spam picking up weapons
    private int pickupCountDown = 0;
    
    // If true, doesn't spawn health bar and isn't controlled by keys
    // For previewing player, like on the start screen
    private boolean previewMode;
    
    public Player (int characterSelection) {
        this(characterSelection, false);
    }
    
    /**
     * Extra constructor for creating a preview player
     */
    public Player (int characterSelection, boolean previewMode) {
        super(100, 1, 6.0);
        
        this.previewMode = previewMode;
        stats = new StatBar​(maxHealth, health, null, 150, 25, 0, Color.GREEN, Color.RED, false);
        weapon = new Bow(this);
        promptText = new Label("", 20);

        initAnimations(characterSelection);
    }
    
    /**
     * Returns number of character models available
     */
    public int getNumCharacters() {
        return framePrefixes.length;
    }
    
    /**
     * Initializes animations
     */
    private void initAnimations(int characterSelection) {
        if (idleFrames == null) {
            idleFrames = new GreenfootImage[getNumCharacters()][];
        }
        
        if (runFrames == null) {
            runFrames = new GreenfootImage[getNumCharacters()][];
        }
        
        if (idleFrames[characterSelection] == null) {
            idleFrames[characterSelection] = Animation.generateFrames(0, 4, framePrefixes[characterSelection], ".png");
        }
        
        if (runFrames[characterSelection] == null) {
            runFrames[characterSelection] = Animation.generateFrames(4, 4, framePrefixes[characterSelection], ".png");
        }
            
        animation = new Animation(this, "idle", idleFrames[characterSelection], 40, "right");
        animation.addState("running", runFrames[characterSelection], 15, "right");
        animation.setActiveState(true);
    }
    
    public void addedToWorld(World w) {
        if (!previewMode) {
            // Spawns health bar
            w.addObject(stats, stats.getImage().getWidth() / 2, stats.getImage().getHeight() / 2);
            
            // Add weapon
            w.addObject(weapon, getX(), getY());
            
            // Add prompt text to world
            w.addObject(promptText, 0, 0);
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
        
        if (pickupCountDown > 0) {
            pickupCountDown--;
        }
        checkItem();
        
        // Check if entered door
        checkDoor();
    }
    
    // Make weapon follow player
    private void weaponFollow() {
        weapon.setLocation(getX(), getY());
    }
    
    /**
     * Check for items
     */
    private void checkItem() {
        Chest chest = (Chest)getOneIntersectingObject(Chest.class);
        if (chest != null && chest.isClosed()) {
            chest.open();
        }
        
        // If pressing P, pick up nearest item
        List<Item> items = getIntersectingObjects(Item.class);
        Item focusItem = null;
        for (Item item : items) {
            if (item.getOwner() != this && (focusItem == null || GameWorld.getDistance(this, item) < GameWorld.getDistance(this, focusItem))) {
                focusItem = item;
            }
        }
        
        if (focusItem != null) {
            showPrompt(focusItem);
            
            if (Greenfoot.isKeyDown("P") && pickupCountDown == 0) {
                pickupCountDown = pickupFrequency;
                focusItem.pickUp(this);
            }
        } else {
            hidePrompt();
        }
    }
    
    /**
     * Show item pickup prompt
     */
    private void showPrompt(Item item) {
        promptText.setValue("Press P to pick up " + item.getClass().getSimpleName());
        promptText.setLocation(item.getX(), item.getY() - item.getImage().getHeight() / 2 - getImage().getHeight() / 2); // Display text above item
    }
    
    /**
     * Hide item pickup prompt
     */
    private void hidePrompt() {
        promptText.setValue("");
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
        if (Greenfoot.isKeyDown("Up")){
            weapon.fire(270);
        } else if (Greenfoot.isKeyDown("Down")){
            weapon.fire(90);
        } else if (Greenfoot.isKeyDown("Left")){
            weapon.fire(180);
        } else if (Greenfoot.isKeyDown("Right")){
            weapon.fire(0);
        }
    }
    
    /**
     * Shoot a projectile in specified direction
     */
    private void shoot(int rotation) {
        Arrow arrow = new Arrow(true, rotation);
        getWorld().addObject(arrow, getX(), getY());
    }
    
    /**
     * Equips new weapon, drops old one
     */
    public void equipWeapon(Weapon weapon) {
        this.weapon.setOwner(null);
        
        this.weapon = weapon;
        this.weapon.setOwner(this);
    }
}
