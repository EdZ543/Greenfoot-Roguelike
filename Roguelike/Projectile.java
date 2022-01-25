import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The projectile superclass. Contains code common to all projectiles.
 * 
 * @author Eddie Zhuang 
 * @version Jan. 21, 2022
 */
public abstract class Projectile extends Actor
{
    protected boolean playerShot;
    private int speed;
    private int damage;
    private double force;
    
    public Projectile(boolean playerShot, int rotation, int speed, int damage, String soundPath, int soundVolume, double force) {
        setRotation(rotation);
        
        this.playerShot = playerShot;
        this.speed = speed;
        this.damage = damage;
        this.force = force;
        
        // Plays firing sound!
        GreenfootSound firingSound = new GreenfootSound(soundPath);
        firingSound.setVolume(soundVolume);
        firingSound.play();
    }
    
    /**
     * Act - do whatever the Projectile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // move forward
        move(speed);
        
        if (getWorld() == null) {
            return;
        }
        
        boolean hitSomething = false;
        
        // check if hitting player or enemy, depending on who fired this
        if (playerShot) {
            hitSomething |= hitEnemy();
        } else {
            hitSomething |= hitPlayer();
        }
        
        // Check if hits rock
        hitSomething |= hitRock();
        
        // When I reach the edge or hit something, remove me from the World
        if (hitSomething || isAtEdge() || isTouching(Wall.class)){
            getWorld().removeObject(this);
        } 
    }

    /*
     * Check if this projectile has hit an enemy
     */
    protected boolean hitEnemy () {
        Enemy e = (Enemy)getOneIntersectingObject(Enemy.class);
        if (e != null){
            GameWorld g = (GameWorld)getWorld();

            e.damageMe(damage); // damage enemy
            e.setForce(getRotation(), force); // impact enemy
            g.updateDoors(); // if this is the last enemy in the room, unlock the doors
            return true;
        }
        return false;
    }
    
    /*
     * Check if this projectile has hit a player
     */
    protected boolean hitPlayer () {
        Player p = (Player)getOneIntersectingObject(Player.class);
        if (p != null){
            p.damageMe(damage); // damage player
            p.setForce(getRotation(), force); // impact player
            return true;
        }
        return false;
    }
    
    /*
     * Check if this projectile has hit a rock
     */
    protected boolean hitRock() {
        Rock r = (Rock)getOneIntersectingObject(Rock.class);
        if (r != null) {
            r.damage();
            return true;
        }
        return false;
    }
}
