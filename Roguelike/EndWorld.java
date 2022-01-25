import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The end screen when you die/win the game!
 * 
 * @author Eddie Zhuang
 * @version 1.0.0
 */
public class EndWorld extends World
{
    /**
     * Constructor for objects of class EndWorld.
     * 
     */
    public EndWorld(int score, int highScore, boolean newHighScore, boolean won)
    {    
        // Create a new 800 x 800 world
        super(960, 576, 1);
        
        // Set black background
        setBackground(drawBackground());
        
        // Set title text based on whether you won or died
        Label titleText = new Label("", 90);
        if (won) {
            titleText.setValue("You won, congrats!");
        } else {
            titleText.setValue("You died!");
        }
        addObject(titleText, getWidth() / 2, 40);
        
        // Add text to let you know your score
        Label scoreText = new Label("Score: " + score, 40);
        addObject(scoreText, getWidth() / 2, 100);
            
        // Change text based on whether you got a new high score or not
        Label highScoreText = new Label("", 40);
        if (newHighScore) {
            highScoreText.setValue("New High Score: " + highScore + "!");
        } else {
            highScoreText.setValue("High Score: " + highScore);
        }
        addObject(highScoreText, getWidth() / 2, 130);
        
        // Add a scoreboard
        ScoreBoard scoreBoard = new ScoreBoard(getWidth(), 350);
        addObject(scoreBoard, getWidth() / 2, 350);
        
        // Let player know that they can press enter to replay
        Label retryText = new Label("Press enter to play again!", 30);
        addObject(retryText, getWidth() / 2, getHeight() - 20);
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