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
        GreenfootImage playerImage = new GreenfootImage("elf_f_run_anim_f1.png");
        playerImage.scale(width, height);
        setImage(playerImage);
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
        
        if (Greenfoot.isKeyDown("up")){
            move(0, -1);
        }
        
        if (Greenfoot.isKeyDown("down")){
            move(0, 1);
        }
        
        if (Greenfoot.isKeyDown("left")){
            move(-1, 0);
        }
        
        if (Greenfoot.isKeyDown("right")){
            move(1, 0);
        }
    }
    
    /**
     * Moves in increments so object will stop if it touches a wall
     */
    private void move(int xDir, int yDir){
        for(int i = 0; i < speed; i++){
                setLocation(getX() + xDir, getY() + yDir);
                
                if(isTouching(Wall.class)){
                    setLocation(getX() - xDir, getY() - yDir);
                    break;
                }
            }
    }
    
    /**
     * Attack the player.
     */
    public void getAttacked(int attackDamage) {
        health = Math.max(0, health - attackDamage);
        if (health == 0) {
            GameWorld g = (GameWorld)getWorld();
            g.gameOver();
        }
    }
}
