import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Projectile extends Actor
{
    private int speed;
    private boolean playerShot;
    private int damage;
    
    public Projectile(boolean playerShot, int rotation, int speed, int damage) {
        this.playerShot = playerShot;
        this.speed = speed;
        this.damage = damage;
        setRotation(rotation);
    }
    
    /**
     * Act - do whatever the Projectile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
        move(speed);
        
        hitEnemy();
        
        if (getWorld() == null) {
            return;
        }
        
        if (isAtEdge()){ // When I reach the edge, remove me from the World
            getWorld().removeObject(this);
        } 
    }

    
    private void hitEnemy () {
        Enemy e = (Enemy)getOneIntersectingObject(Enemy.class);
        if (e != null){
            GameWorld g = (GameWorld)getWorld();

            e.damageMe(damage);
            g.removeObject(this);
        }
    }
}
