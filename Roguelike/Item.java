import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An item that the player can pick up
 * 
 * @author Eddie Zhuang
 * @version Jan. 23, 2022
 */
public abstract class Item extends MomentumMover
{
    private Label promptText = new Label("Press P to pick up " + this.getClass().getSimpleName(), 20);
    
    private boolean showingPrompt = false;
    
    public Item(int width, int height) {
        super(0.9, 10.0);
        GameWorld.scaleWithAspectRatio(getImage(), width, height);
    }
    
    public void act() {
        applyPhysics();
        
        // If player can pick up the item, let them know what the item is
        if (isTouching(Player.class) && !showingPrompt) {
            showPrompt();
        } else if (!isTouching(Player.class) && showingPrompt) {
            hidePrompt();
        }
    }
    
    /**
     * Shows prompt
     */
    private void showPrompt() {
        getWorld().addObject(promptText, getX(), getY() - this.getImage().getHeight() / 2 - promptText.getImage().getHeight());
        showingPrompt = true;
    }
    
    /**
     * Hides prompt
     */
    private void hidePrompt() {
        getWorld().removeObject(promptText);
        showingPrompt = false;
    }
    
    /**
     * If used, remove from world and apply effect on player
     */
    public void use(Player player) {
        getWorld().removeObject(promptText);
        getWorld().removeObject(this);
        apply(player);
    }
    
    public abstract void apply(Player player);
}
