import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Player class. This is the actor that the user controls.
 * 
 * @author Eddie Zhuang
 * @version (a version number or a date)
 */
public class Player extends Actor
{
    private GreenfootImage image;
    
    private int speed = 6;
    private int health = 100;
    
    public Player (int width, int height) {
        this.getImage().scale(width, height);
    }
    
    /**
     * Act - do whatever the Player wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
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
            setRotation(270);
            move();
        }
        
        if (Greenfoot.isKeyDown("A")){
            setRotation(180);
            move();
        }
        
        if (Greenfoot.isKeyDown("S")){
            setRotation(90);
            move();
        }
        
        if (Greenfoot.isKeyDown("D")){
            setRotation(0);
            move();
        }
        
        if (Greenfoot.isKeyDown("Up")){
            shoot(270);
        } else if (Greenfoot.isKeyDown("Down")){
            shoot(180);
        } else if (Greenfoot.isKeyDown("Left")){
            shoot(90);
        } else if (Greenfoot.isKeyDown("Right")){
            shoot(0);
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
        Projectile projectile = new Projectile(true, rotation, 50, 5);
        getWorld().addObject(projectile, getX(), getY());
    }
    
    /**
     * Attack the player.
     */
    public void damageMe(int attackDamage) {
        health = Math.max(0, health - attackDamage);
        if (health == 0) {
            GameWorld g = (GameWorld)getWorld();
            g.gameOver();
        }
    }
}
