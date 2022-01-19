import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Projectile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Projectile extends Actor
{
    protected int speed;
    protected boolean playerShot;
    protected int damage;
    
    public Projectile(int width, int height, boolean playerShot, int rotation, int speed, int damage) {
        this.playerShot = playerShot;
        this.speed = speed;
        this.damage = damage;
        setRotation(rotation);
        GameWorld.scaleWithAspectRatio(getImage(), width, height);
    }
    
    /**
     * Act - do whatever the Projectile wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()
    {
        // Add your action code here.
        move(speed);
        
        if (playerShot) {
            hitEnemy();
        } else {
            hitPlayer();
        }
        
        if (getWorld() == null) {
            return;
        }
        
        if (isAtEdge() || isTouching(WallTile.class)){ // When I reach the edge, remove me from the World
            getWorld().removeObject(this);
        } 
    }

    
    protected void hitEnemy () {
        Enemy e = (Enemy)getOneIntersectingObject(Enemy.class);
        if (e != null){
            GameWorld g = (GameWorld)getWorld();

            e.damageMe(damage);
            g.removeObject(this);
            g.updateDoors();
        }
    }
    
    protected void hitPlayer () {
        Player p = (Player)getOneIntersectingObject(Player.class);
        if (p != null){
            GameWorld g = (GameWorld)getWorld();

            p.damageMe(damage);
            g.removeObject(this);
        }
    }
}
