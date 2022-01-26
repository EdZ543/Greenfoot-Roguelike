import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A weapon that releases a large spread of bullets
 * 
 * @author Eddie Zhuang
 * @version Jan. 24, 2022
 */
public class Shotgun extends Weapon
{
    private int halfBullets = 5;
    private int angleOfSpread = 5;
    private OverlappingSound gunShotSound = new OverlappingSound("shotgun.wav", 90);
    private int soundIndex = 0;
    
    public Shotgun(Actor owner) {
        super(50, owner, 10, "right");
    }
    
    /**
     * Releases spread of shotgun shells
     */
    public void releaseProjectiles(int angle) {
        for (int i = -halfBullets; i < halfBullets; i++) {
            int fireAngle = angle + angleOfSpread * i;
            ShotgunShell shotGunShell = new ShotgunShell(true, fireAngle);
            getWorld().addObject(shotGunShell, getX(), getY());
        }
        
        // Play gunshot sound
        gunShotSound.play();
    }
}