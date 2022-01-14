import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Boss here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Boss extends Enemy
{
    private GreenfootImage runningFrames[] = new GreenfootImage[4];
    private Animation animation;
    
    private int width;
    private int height;
    
    public Boss(int width, int height) {
        super(500, 1000);
        
        this.width = width;
        this.height = height;
        
        initAnimation();
    }
    
    private void initAnimation() {
        for (int i = 0; i < runningFrames.length; i++) {
            String framePath = "boss/run/big_demon_run_anim_f" + i + ".png";
            GreenfootImage frame = new GreenfootImage(framePath);
            GameWorld.scaleWithAspectRatio(frame, width, height);
            runningFrames[i] = frame;
        }
        
        animation = new Animation(this);
        animation.addState("running", runningFrames, 20, "right");
        animation.setState("running");
        
        animation.setActiveState(true);
    }
    
    /**
     * Act - do whatever the Boss wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
        animation.run();
    }
    
    private void die() {
        GameWorld g = (GameWorld)getWorld();
        g.removeObject(this);
        g.updateScore(points);
        g.gameOver(true);
    }
}
