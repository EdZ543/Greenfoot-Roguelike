import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Class that implements momentum-based movement
 * Also includes collision detection, and extra precise movement
 * Makes rotation seperate from image because the sprites for my game aren't meant to rotate
 * 
 * @author Eddie Zhuang
 * @version Jan. 23, 2022
 */
public class MomentumMover extends Actor
{
    // Physics variables 
    private double decay;
    private double minSpeed = 0.1;
    private double maxSpeed;
    private double speed = 0;
    private double xSpeed = 0, ySpeed = 0;
    
    // Exact coordinates
    private double exactX;
    private double exactY;
    private double exactRotation;
    
    public MomentumMover(double decay, double maxSpeed) {
        this.decay = decay;
        this.maxSpeed = maxSpeed;
    }
    
    public void updateMaxSpeed(double change) {
        this.maxSpeed += change;
    }
    
    /**
     * Called during run to move according to speed
     * Includes wall collision
     */
    protected void applyPhysics() {
        // Apply x and y collision detection seperately to not stick to walls
        if (speed != 0) {
            double dx = xSpeed / speed;
            double dy = ySpeed / speed;
            
            for (int i = 0; i < speed; i++) {
                setLocation(exactX + dx, exactY);
                if (isTouching(Wall.class)) { // Move in increments for wall collision
                    setLocation(exactX - dx, exactY);
                }
                
                setLocation(exactX, exactY + dy);
                if (isTouching(Wall.class)) {
                    setLocation(exactX, exactY - dy);
                }
            }
        }
        
        // Slow down over time
        setSpeed(xSpeed * decay, ySpeed * decay);
    }
    
    /**
     * Adds force in a particular direction
     */
    public void addForce(double angle, double force) {
        double radians = Math.toRadians(angle);
        double dx = Math.cos(radians) * force;
        double dy = Math.sin(radians) * force;
        setSpeed(xSpeed + dx, ySpeed + dy);
    }
    
    /**
     * Adds force in direction of facing
     */
    public void addForce(double force) {
        addForce(exactRotation, force);
    }
    
    /**
     * Sets force in particular direction
     */
    public void setForce(double angle, double force) {
        double radians = Math.toRadians(angle);
        double dx = Math.cos(radians) * force;
        double dy = Math.sin(radians) * force;
        setSpeed(dx, dy);
    }
    
    /**
     * Sets x and y speed, while enforcing max and min speeds
     */
    protected void setSpeed(double xSpeed, double ySpeed) {
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        speed = Math.sqrt(Math.pow(this.xSpeed, 2) + Math.pow(this.ySpeed, 2));
        
        if (speed < minSpeed) {
            this.xSpeed = 0;
            this.ySpeed = 0;
        }
        
        if (speed > maxSpeed) {
            this.xSpeed *= maxSpeed / speed;
            this.ySpeed *= maxSpeed / speed;
        }
    }
    
    /**
     * Move forward by the specified distance.
     * (Overrides the method in Actor).
     * 
     * @param distance  the distance to move in the current facing direction
     */
    @Override
    public void move(int distance)
    {
        move((double)distance);
    }

    /**
     * Move forward by the specified exact distance.
     * 
     * @param distance the precise distance to move in the current facing direction
     */
    public void move(double distance)
    {
        double radians = Math.toRadians(exactRotation);
        double dx = Math.cos(radians) * distance;
        double dy = Math.sin(radians) * distance;
        setLocation(exactX + dx, exactY + dy);
    }

    /**
     * Set the internal rotation value to a new value.
     * 
     * @param rotation the precise new angle
     */
    public void setRotation (double rotation){
        exactRotation = rotation;
    }

    /**
     * Set the internal rotation value to a new value. This will override the method from Actor.
     * 
     * @param rotation the new angle
     */
    @Override
    public void setRotation (int rotation){
        exactRotation = rotation;
    }

    /**
     * Set the internal rotation to face towards a given point. This will override the method from Actor.
     * 
     * @param x the x coordinate to face
     * @param y the y coordinate to face
     */
    @Override
    public void turnTowards (int x, int y){
        setRotation( Math.toDegrees(Math.atan2(y - getY() , x - getX())));
    }

    /**
     * A short-cut method that I (Jordan Cohen) always thought Greenfoot should have - use the
     * tuntToward method above to face another Actor instead of just a point. Keeps calling code
     * cleaner. 
     * 
     * @param a     The Actor to turn towards. 
     */
    public void turnTowards (Actor a){
        turnTowards (a.getX(), a.getY());
    }

    /**
     * Turn a specified number of degrees.
     * 
     * @param angle     the number of degrees to turn.
     */
    @Override
    public void turn (int angle){
        exactRotation += angle;
    }

    /**
     * Turn a specified number of degrees with precision.
     * 
     * @param angle     the precise number of degrees to turn
     */
    public void turn (double angle){
        exactRotation += angle;
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

    /**
     * Return the exact x-coordinate (as a double).
     * 
     * @return double   the exact x coordinate, as a double
     */
    public double getExactX() 
    {
        return exactX;
    }

    /**
     * Return the exact y-coordinate (as a double).
     * 
     * @return double   the exact x coordinate, as a double
     */
    public double getExactY() 
    {
        return exactY;
    }
    
    public double getExactRotation (){
        return exactRotation;
    }
}