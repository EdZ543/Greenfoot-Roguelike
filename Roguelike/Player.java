import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Player class. This is the actor that the user controls.
 * 
 * @author Eddie Zhuang
 * @version (a version number or a date)
 */
public class Player extends Actor
{
    static GreenfootImage[] runFrames = new GreenfootImage[4];
    
    private GreenfootImage image;
    private StatBar stats;
    private Animation animation;
    
    private int speed = 6;
    private int health = 100;
    private int shootDelay = 20;
    private int shootDelayTimer = 0;
    private String facingDir = "right";
    
    public Player (int width, int height) {
        this.getImage().scale(width, height);
        
        stats = new StatBar​(health, health, null, 150, 25, 0, Color.GREEN, Color.RED, false);
        
        for (int i = 0; i < runFrames.length; i++) {
            String framePath = "player/run/knight_f_run_anim_f" + i + ".png";
            GreenfootImage frame = new GreenfootImage(framePath);
            
            frame.scale(width, height);
            runFrames[i] = frame;
        }
        
        animation = new Animation(this, runFrames);
        animation.setCycleActs(25);
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
        
        if (Greenfoot.isKeyDown("W")){
            moveInDir(0, -1);
        }
        
        if (Greenfoot.isKeyDown("A")){
            faceDir("left");
            moveInDir(-1, 0);
        }
        
        if (Greenfoot.isKeyDown("S")){
            moveInDir(0, 1);
        }
        
        if (Greenfoot.isKeyDown("D")){
            faceDir("right");
            moveInDir(1, 0);
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
    
    private void faceDir(String dir) {
        if (dir == facingDir) return;
        
        facingDir = dir;
        
        for (int i = 0; i < runFrames.length; i++) {
            runFrames[i].mirrorHorizontally();
        }
        
        animation.setFrames(runFrames);
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
