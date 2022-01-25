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
    private GreenfootSound[] gunShotSounds;
    private int soundIndex = 0;
    
    public Shotgun(Actor owner) {
        super(64, -1, 50, owner, 10, "right");
        
        // Init gunshot sound
        gunShotSounds = new GreenfootSound[20];
        for (int i = 0; i < 20; i++) {
            GreenfootSound sound = new GreenfootSound("shotgun.wav");
            sound.setVolume(90);
            gunShotSounds[i] = sound;
        }
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
        gunShotSounds[soundIndex].play();
        soundIndex = (soundIndex + 1) % gunShotSounds.length;
    }
}