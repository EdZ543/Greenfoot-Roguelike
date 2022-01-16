import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Boss here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Boss extends Enemy
{
    private GreenfootImage runningFrames[];
    
    public Boss(int width, int height) {
        super(width, height, 5, 1000, 69420);
    }
    
    public void act()
    {
        animation.run();
        spreadShot();
    }
    
    protected void initAnimations() {
        runningFrames = new GreenfootImage[4];
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
    
    private void spreadShot() {
        for (int i = 0; i < 360; i += 45) {
            Bullet bullet = new Bullet(false, i);
            getWorld().addObject(bullet, getX(), getY());
        }
    }
}
