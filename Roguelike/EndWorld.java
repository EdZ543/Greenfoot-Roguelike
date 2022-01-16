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
    private Label highScoreText;
    private ScoreBoard scoreBoard;

    /**
     * Constructor for objects of class EndWorld.
     * 
     */
    public EndWorld(int score, int highScore, boolean newHighScore, boolean won)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 800, 1); 
        
        if (won) {
            titleText = new Label("You won, congrats!", 100);
        } else {
            titleText = new Label("You died, nice!", 100);
        }
        addObject(titleText, 400, 50);
        
        scoreText = new Label("Score: " + score, 50);
        addObject(scoreText, 400, 125);
            
        if (newHighScore) {
            highScoreText = new Label("New High Score: " + highScore + "!!", 50);
        } else {
            highScoreText = new Label("High Score: " + highScore, 50);
        }
        
        addObject(highScoreText, 400, 175);
        
        scoreBoard = new ScoreBoard(600, 600);
        addObject(scoreBoard, 400, 500);
    }
}