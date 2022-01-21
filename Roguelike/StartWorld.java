import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StartWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class StartWorld extends World
{
    public static int playerSelection;
    
    private GreenfootImage bgImage;
    private Label scoreText;
    private UserInfo user;
    private Player player;
    
    /**
     * Constructor for objects of class StartWorld.
     * 
     */
    public StartWorld()
    {
        super(960, 576, 1);
        bgImage = new GreenfootImage("welcome.png");
        setBackground(bgImage);
        
        String tempText = "STORAGE NOT AVAILABLE. PLEASE ENSURE YOU ARE CONNECTED AND LOGGED IN";
        
        if (UserInfo.isStorageAvailable()) {
            user = UserInfo.getMyInfo();
        }
        if (user != null){
            playerSelection = user.getInt(0);
            tempText = "WELCOME " + user.getUserName() + "! HIGH SCORE: " + user.getScore();
        } else {
            playerSelection = 0;
            tempText = "PLEASE LOG IN TO ENJOY HIGH SCORES AND CLOUD SAVED PREFERENCES!";
        }
        
        user.setInt(0, playerSelection);
        
        scoreText = new Label(tempText, 25);
        addObject(scoreText, getWidth() / 2, 325);
        
        player = new Player(50, -1, playerSelection, true);
        addObject(player, 293, 443);
    }
    
    public void act () {
        MouseInfo m = Greenfoot.getMouseInfo();
        if (m != null){
            if (Greenfoot.mouseClicked(null)){ 
                if (m.getButton() == 1){ // left click
                    nextCharacter(); 
                }
            }
        }
        
        if (Greenfoot.isKeyDown("enter")){
            GameWorld.startOver();
            Greenfoot.setWorld (new GameWorld());
        }
    }
    
    /*
     * Cycles through character selections
     */
    private void nextCharacter() {
        playerSelection = (playerSelection + 1) % player.characters.length;
        player.setCharacter(playerSelection);
        user.setInt(0, playerSelection);
        user.store();
    }
}
