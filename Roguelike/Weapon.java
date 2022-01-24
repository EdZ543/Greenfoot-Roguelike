import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Weapon that player can pick up!
 * 
 * @author Eddie Zhuang
 * @version Jan. 24, 2022
 */
public abstract class Weapon extends Item
{
    private int fireFrequency;
    private int fireCountDown = 0;
    private double recoil;
    private String facingDir;
    
    public Weapon(int width, int height, int fireFrequency, Actor owner, double recoil, String facingDir) {
        super(width, height);
        this.fireFrequency = fireFrequency;
        this.owner = owner;
        this.recoil = recoil;
        this.facingDir = facingDir;
    }
    
    /**
     * Act - do whatever the Weapon wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        super.act();
        
        if (fireCountDown > 0) {
            fireCountDown--;
        }
        
        if (owner != null) {
            followOwner();
        }
    }
    
    private void followOwner() {
        setLocation(owner.getX(), owner.getY());
    }
    
    /**
     * Releases projectiles if cooldown is over
     */
    public void fire(int angle) {
        if (fireCountDown == 0) {
            fireCountDown = fireFrequency;
            checkFacingDir(angle);
            releaseProjectiles(angle);
            
            // Recoil, push user back when firing!
            if (owner != null) {
                ((MomentumMover)owner).addForce(angle + 180, recoil);
            }
        }
    }
    
    /**
     * Sets image orientation to match firing direction
     */
    private void checkFacingDir(int angle) {
        double radians = Math.toRadians(angle);
        if ((angle < 90 || angle > 270) && facingDir == "left") {
            getImage().mirrorHorizontally();
            facingDir = "right";
        } else if ((angle > 90 && angle < 270) && facingDir == "right") {
            getImage().mirrorHorizontally();
            facingDir = "left";
        }
    }
    
    public abstract void releaseProjectiles(int angle);
    
    /**
     * When picked up, equip player with this weapon
     */
    protected void pickUp(Player player) {
        player.equipWeapon(this);
    }
}
