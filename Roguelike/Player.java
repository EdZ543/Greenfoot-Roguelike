import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Player class. This is the actor that the user controls.
 * 
 * @author Eddie Zhuang
 * @version (a version number or a date)
 */
public class Player extends Entity
{
    public String[] characters;
    public String[] characterPrefixes;
    
    private GreenfootImage[] idleFrames;
    private GreenfootImage[] runningFrames;
    private GreenfootSound[] shootSounds;
    
    private int shootDelay = 20;
    private int shootDelayTimer = 0;
    private int shootSoundsIndex = 0;
    private boolean preview = false;
    private int characterIndex;
    
    public Player (int width, int height, int characterIndex, boolean preview) {
        super(width, height, 5, 100);
        this.characterIndex = characterIndex;
        this.preview = preview;
        
        shootSounds = new GreenfootSound[20];
        for (int i = 0; i < shootSounds.length; i++) {
            shootSounds[i] = new GreenfootSound("bow_fire.wav");
        }
        
        setCharacter(characterIndex);
    }
    
    protected void initAnimations() {
        characters = new String[]{"knight", "female elf", "male elf"};
        characterPrefixes = new String[]{"knight_f", "elf_f", "elf_m"};
        
        idleFrames = new GreenfootImage[4];
        for (int i = 0; i < idleFrames.length; i++) {
            String framePath = characters[characterIndex] + "/idle/" + characterPrefixes[characterIndex] + "_idle_anim_f" + i + ".png";
            GreenfootImage frame = new GreenfootImage(framePath);
            
            GameWorld.scaleWithAspectRatio(frame, width, height);
            idleFrames[i] = frame;
        }
        
        runningFrames = new GreenfootImage[4];
        for (int i = 0; i < runningFrames.length; i++) {
            String framePath = characters[characterIndex] + "/run/" + characterPrefixes[characterIndex] + "_run_anim_f" + i + ".png";
            GreenfootImage frame = new GreenfootImage(framePath);
            
            GameWorld.scaleWithAspectRatio(frame, width, height);
            runningFrames[i] = frame;
        }
            
        animation = new Animation(this, characters[characterIndex] + " idle", idleFrames, 40, "right");
        animation.addState(characters[characterIndex] + " running", runningFrames, 15, "right");
        animation.setActiveState(true);
    }
    
    public void setCharacter(int characterIndex) {
        this.characterIndex = characterIndex;
        initAnimations();
    }
    
    public void addedToWorld(World w) {
        if (preview) return;
        stats = new StatBar​(maxHealth, health, null, 150, 25, 0, Color.GREEN, Color.RED, false);
        
        w.addObject(stats, stats.getImage().getWidth() / 2, stats.getImage().getHeight() / 2);
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
        if (!preview) checkKeys();
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
            animation.setState(characters[characterIndex] + " running");
        } else {
            animation.setState(characters[characterIndex] + " idle");
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
