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
    
    public Projectile(int width, int height, boolean playerShot, int rotation, int speed, int damage, String soundPath, int soundVolume, double force) {
        GameWorld.scaleWithAspectRatio(getImage(), width, height);
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
        
        // check if hitting player or enemy, depending on who fired this
        if (playerShot) {
            hitEnemy();
        } else {
            hitPlayer();
        }
        
        if (getWorld() == null) {
            return;
        }
        
        // When I reach the edge or hit a wall, remove me from the World
        if (isAtEdge() || isTouching(Wall.class)){
            getWorld().removeObject(this);
        } 
    }

    /*
     * Check if this projectile has hit an enemy
     */
    protected void hitEnemy () {
        Enemy e = (Enemy)getOneIntersectingObject(Enemy.class);
        if (e != null){
            GameWorld g = (GameWorld)getWorld();

            e.damageMe(damage); // damage enemy
            e.setForce(getRotation(), force); // impact enemy
            g.removeObject(this);
            g.updateDoors(); // if this is the last enemy in the room, unlock the doors
        }
    }
    
    /*
     * Check if this projectile has hit a player
     */
    protected void hitPlayer () {
        Player p = (Player)getOneIntersectingObject(Player.class);
        if (p != null){
            GameWorld g = (GameWorld)getWorld();

            p.damageMe(damage); // damage player
            p.setForce(getRotation(), force); // impact player
            g.removeObject(this);
        }
    }
}
