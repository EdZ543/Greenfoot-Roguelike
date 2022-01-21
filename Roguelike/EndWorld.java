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
    public EndWorld(int score, int highScore, boolean newHighScore, boolean won)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 800, 1);
        
        setBackground(drawBackground());
        
        if (won) {
            titleText = new Label("You won, congrats!", 100);
        } else {
            titleText = new Label("You died, nice!", 100);
        }
        addObject(titleText, 400, 50);
        
        scoreText = new Label("Score: " + score, 50);
        addObject(scoreText, 400, 125);
            
        Label highScoreText = new Label("", 50);
        if (newHighScore) {
            highScoreText.setValue("New High Score: " + highScore + "!!");
        } else {
            highScoreText = new Label("High Score: " + highScore, 50);
        }
        
        addObject(highScoreText, 400, 175);
        
        ScoreBoard scoreBoard = new ScoreBoard(600, 600);
        addObject(scoreBoard, 400, 500);
    }
    
    /**
     * Paints background!
     */
    private GreenfootImage drawBackground() {
        GreenfootImage background = new GreenfootImage(getWidth(), getHeight());
        background.setColor(Color.BLACK);
        background.fillRect(0, 0, getWidth(), getHeight());
        return background;
    }
    
    public void act() {
        // Restarts game if enter is pressed!
        if (Greenfoot.isKeyDown("enter")){
            GameWorld.startOver();
            Greenfoot.setWorld (new GameWorld());
        }
    }
}