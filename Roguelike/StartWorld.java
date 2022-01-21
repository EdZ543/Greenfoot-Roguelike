import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The start screen!
 * 
 * @author Eddie Zhuang
 * @version 1.0.0
 */
public class StartWorld extends World
{
    private UserInfo user;
    private Player player;
    
    private int playerSelection;
    
    /**
     * Constructor for objects of class StartWorld.
     * 
     */
    public StartWorld()
    {
        // Initalize world
        super(960, 576, 1);
        
        // Set epic background image
        setBackground(new GreenfootImage("welcome.png"));
        
        // Text showing greeting and high score
        Label scoreText = new Label("Storage not available, please make sure that you're connected and logged in", 25);
        
        // Check if user storage is available
        if (UserInfo.isStorageAvailable()) {
            user = UserInfo.getMyInfo();
        }
        
        // Load saved high score or preferences, or defaults if none detected
        if (user != null){
            playerSelection = user.getInt(0);
            scoreText.setValue("What's up,  " + user.getUserName() + "? Your high score is " + user.getScore() + "!");
        } else {
            playerSelection = 0;
            scoreText.setValue("Log in to save your high scores and settings");
        }
        
        addObject(scoreText, getWidth() / 2, 325);
        
        addPlayerPreview();
    }
    
    public void act () {
        // If left click detected, change character model
        MouseInfo m = Greenfoot.getMouseInfo();
        if (m != null){
            if (Greenfoot.mouseClicked(null)){ 
                if (m.getButton() == 1){ 
                    nextCharacter(); 
                }
            }
        }
        
        // If enter pressed, start game!
        if (Greenfoot.isKeyDown("enter")){
            GameWorld.setCharacter(playerSelection);
            GameWorld.startOver();
            Greenfoot.setWorld (new GameWorld());
        }
    }
    
    /**
     * Cycles through character selections
     */
    private void nextCharacter() {
        playerSelection = (playerSelection + 1) % player.getNumCharacters();
        user.setInt(0, playerSelection);
        user.store(); // Stores selection in user info object
        
        addPlayerPreview();
    }
    
    /**
     * Adds a preview of the player selection
     */
    private void addPlayerPreview() {
        if (player != null) {
            removeObject(player);    
        }
        
        player = new Player(50, -1, playerSelection, true);
        addObject(player, 293, 443);
    }
}
