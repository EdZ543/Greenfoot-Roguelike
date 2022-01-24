import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An item that the player can pick up
 * 
 * @author Eddie Zhuang
 * @version Jan. 23, 2022
 */
public abstract class Item extends MomentumMover
{
    protected Actor owner = null;
    
    public Item(int width, int height) {
        super(0.9, 10.0);
        GameWorld.scaleWithAspectRatio(getImage(), width, height);
    }
    
    public void act() {
        applyPhysics();
    }
    
    protected abstract void pickUp(Player player);
    
    /**
     * Set owner, for dropping and picking up
     */
    public void setOwner(Actor owner) {
        this.owner = owner;
    }
    
    /**
     * Return owner of weapon
     */
    public Actor getOwner() {
        return owner;
    }
}
