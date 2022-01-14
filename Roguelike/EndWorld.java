import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class EndWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EndWorld extends World
{
    private Label titleText;
    private Label scoreText;

    /**
     * Constructor for objects of class EndWorld.
     * 
     */
    public EndWorld(int score, boolean won)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 800, 1); 
        
        if (won) {
            titleText = new Label("You won, congrats!", 100);
        } else {
            titleText = new Label("You died, nice!", 100);
        }
        addObject(titleText, getWidth() / 2, getHeight() / 2);
        
        scoreText = new Label("Score: " + score, 50);
        addObject(scoreText, getWidth() / 2, getHeight() / 2 + 50);
    }
}