import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class for more precise movement, momentum based movement and collision
 * 
 * @author Eddie Zhuang
 * @version Jan. 23, 2022
 */
public class Physics extends Actor
{
    // Physics variables
    private double decay = 0.8;
    private double maxSpeed = 4.0;
    private double minSpeed = 0.1;
    private double xVel = 0, yVel = 0;
    
    private double exactX;
    private double exactY;
    
    public void act() {
        // Move according to current velocity
        setLocation(exactX + xVel, exactY + yVel);
        
        xVel *= decay;
        yVel *= decay;
    }
    
    /**
     * Add force to object
     */
    public void addForce(double angle, double force) {
        double dx = Math.cos(angle) * force;
        double dy = Math.sin(angle) * force;
        xVel += dx;
        yVel += dy;
    }
    
    /**
     * Set the location using exact coordinates.
     * 
     * @param x the new x location
     * @param y the new y location
     */
    public void setLocation(double x, double y) 
    {
        exactX = x;
        exactY = y;
        super.setLocation((int) (x + 0.5), (int) (y + 0.5));
    }

    /**
     * Set the location using integer coordinates.
     * (Overrides the method in Actor.)    
     * 
     * @param x the new x location
     * @param y the new y location
     */
    @Override
    public void setLocation(int x, int y) 
    {
        exactX = x;
        exactY = y;
        super.setLocation(x, y);
    }
}
