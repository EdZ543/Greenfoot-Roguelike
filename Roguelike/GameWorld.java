import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * ## Credits ##
 * Dungeon Tileset https://0x72.itch.io/dungeontileset-ii
 * 
 * Procedural Map Generation Algorithm by Florian Himsl https://www.boristhebrave.com/2020/09/12/dungeon-generation-in-binding-of-isaac/
 *
 * Code from Jordan Cohen:
 * OOP code from Bug Simulation
 * Code from Space Demo
 * Statbar class
 * SuperSmoothMover class
 * 
 * Code from Danpost:
 * Animation Support Class, https://www.greenfoot.org/scenarios/14496
 * Battle (Boss) by BoxCat Games | https://freemusicarchive.org/music/BoxCat_Games Music promoted by https://www.chosic.com/free-music/all/ Creative Commons CC BY 3.0 https://creativecommons.org/licenses/by/3.0/
 *
 * Bow sound effect obtained from https://www.zapsplat.com
 * 
 * ShotGun Pixel by Fkgame
 * 
 * Rock art by FunwithPixels
 * 
 * ## Instructions ##
 * WASD to move, arrow keys to shoot
 * P to pick up items
 * 
 * This is a procedurally generated roguelike.
 * This means that each run will have a random map and rooms.
 * It also has permadeath, which means if you die, you start from the beginning!
 * Walk through doors to progress, you'll have to defeat al enemies in a room to unlock the doors.
 * Try to get as high a score as possible by defeating enemies.
 * Open chests to get items and weapons.
 * If you manage to reach the boss room and defeat the boss, you'll beat the game!
 * 
 * ## Known bugs/issues ##
 * 
 * @author Eddie Zhuang
 * @version 0.0.1
 */
public class GameWorld extends World
{
    // Static variables, so they can persist across rooms
    
    // Game dimensions and sizes
    private static int numTilesX = 15;
    private static int numTilesY = 9;
    private static int tileWidth = 64;
    private static int tileHeight = 64;
    
    // Actors that persist through rooms
    private static Player player;
    private static Minimap minimap;
    private static Label scoreText;
    private static UserInfo userInfo;
    private static GreenfootSound bgMusic;
    private static ProceduralMap map;
    
    private static int score;
    private static int highScore;
    private static String enterPos;
    private static int characterSelection = 0;
    
    private static int curRoomNum;
    private static GameWorld[] roomWorlds; // Array of room worlds, so they stay the same when returning to them
    private static String[][] roomLayoutPlan;

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public GameWorld()
    {   
        super(960, 576, 1);
            
        createRoom();
        updateRoom();
        
        roomWorlds[curRoomNum] = this;
        setPaintOrder(Item.class, Entity.class);
    }
    
    /**
     * Called when world starts running
     */    
    public void started() {
        // Plays music when unpausing world
        bgMusic.playLoop();
    }
    
    /**
     * Called when world is paused or stopped
     */
    public void stopped() {
        // Stops music when game is paused or stopped
        bgMusic.stop();
    }
    
    /**
     * Updates all objects that persist across rooms when re-entering a room
     */
    public void updateRoom() {
        // Remove projeciles
        removeObjects(getObjects(Projectile.class));
        
        int offsetX = tileWidth + (int)Math.ceil((double)player.getImage().getWidth() / 2);
        int offsetY = tileHeight + (int)Math.ceil((double)player.getImage().getHeight() / 2);
        if (enterPos == "center") addObject(player, getWidth() / 2, getHeight() / 2);
        else if (enterPos == "right") addObject(player, getWidth() - offsetX, getHeight() / 2);
        else if(enterPos == "left") addObject(player, offsetX, getHeight() / 2);
        else if(enterPos == "down") addObject(player, getWidth() / 2, getHeight() - offsetY);
        else if(enterPos == "up") addObject(player, getWidth() / 2, offsetY);
        
        addObject(minimap, getWidth() - minimap.getImage().getWidth() / 2, minimap.getImage().getHeight() / 2);
        addObject(scoreText, scoreText.getImage().getWidth() / 2, getHeight() - scoreText.getImage().getHeight() / 2);
    }
    
    /**
     * Change the selected player character
     */
    public static void setCharacter(int characterSelection) {
        GameWorld.characterSelection = characterSelection;
    }
    
    /**
     * Static variables don't change when resetting game, so we have to do it manually
     */
    public static void startOver() {
        map = new ProceduralMap();
        roomWorlds = new GameWorld[101];
        curRoomNum = map.getStartRoomNum();
        roomLayoutPlan = new String[map.floorPlanLength()][];
        assignRoomLayouts();
        
        score = 0;
        enterPos = "center";
        
        bgMusic = new GreenfootSound("BoxCat-Games-Battle-Boss.mp3");
        bgMusic.setVolume(25);
        bgMusic.playLoop();
    
        if (UserInfo.isStorageAvailable()) {
            userInfo = UserInfo.getMyInfo();
        }
        
        if (userInfo != null) {
            highScore = userInfo.getScore();
        }
        
        player = new Player(-1, tileHeight - 10, characterSelection);
        minimap = new Minimap(map, curRoomNum);
        scoreText = new Label("Score: 0", 40);
    }
    
    /**
     * Assigns a random room layout to each room in the map!
     */
    private static void assignRoomLayouts() {
        // Make a randomly shuffled list of all room layout indexes
        List<Integer> randomRooms = new ArrayList();
        for (int i = 0; i < Layouts.roomLayouts.length; i++) {
            randomRooms.add(i);
        }
        Collections.shuffle(randomRooms);
        
        // Assign each room in the map to a layout
        int randomRoomIndex = 0;
        for (int i = 0; i < map.floorPlanLength(); i++) {
            if (map.isRoomAt(curRoomNum)) {
                if (i == map.getStartRoomNum()) { // Special layout for starting room
                    roomLayoutPlan[i] = Layouts.startRoomLayout;
                } else if (i == map.getBossRoomNum()) { // Special layout for boss room
                    roomLayoutPlan[i] = Layouts.bossRoomLayout;
                } else {
                    int index = randomRooms.get(randomRoomIndex++);
                    roomLayoutPlan[i] = Layouts.roomLayouts[index];
                    randomRoomIndex %= randomRooms.size();
                }
            }
        }
    }
    
    /**
     * Static method that gets the distance between the x,y coordinates of two Actors
     * using Pythagorean Theorum.
     * 
     * @param a     First Actor
     * @param b     Second Actor
     * @return float
     */
    public static float getDistance (Actor a, Actor b)
    {
        double distance;
        double xLength = a.getX() - b.getX();
        double yLength = a.getY() - b.getY();
        distance = Math.sqrt(Math.pow(xLength, 2) + Math.pow(yLength, 2));
        return (float)distance;
    }
    
    /**
     * Generates room using an array
     */
    private void createRoom(){
        String[] roomLayout = roomLayoutPlan[curRoomNum];
        
        GreenfootImage floorImage = new GreenfootImage("floor_1.png");
        floorImage.scale(tileWidth, tileHeight);
        
        // Draw the floor!
        for(int i = 1; i < numTilesY - 1; i++){
            for(int j = 1; j < numTilesX - 1; j++){
                char type = roomLayout[i - 1].charAt(j - 1);
                int x = j * tileWidth + tileWidth / 2;
                int y = i * tileHeight + tileWidth / 2;
                                    
                switch (type) {
                    case '^': addObject(new SpikedFloor(tileWidth, tileHeight), x, y); break;
                    default:
                        getBackground().drawImage(floorImage, x - tileWidth / 2, y - tileWidth / 2);
                }
            }
        }
        
        // Draw room boundary
        for(int i = 0; i < numTilesY; i++){
            for(int j = 0; j < numTilesX; j++){
                char type = Layouts.boundaryLayout[i].charAt(j);
                int x = j * tileWidth + tileWidth / 2;
                int y = i * tileHeight + tileWidth / 2;
                
                switch (type) {
                    case 'L': case 'l': addObject(new Wall("boundary-edge.png", tileWidth, tileHeight, 270), x, y); break;
                    case 'R': case 'r': addObject(new Wall("boundary-edge.png", tileWidth, tileHeight, 90), x, y); break;
                    case 'U': case 'u': addObject(new Wall("boundary-edge.png", tileWidth, tileHeight, 0), x, y); break;
                    case 'D': case 'd': addObject(new Wall("boundary-edge.png", tileWidth, tileHeight, 180), x, y); break;
                    case 'c': addObject(new Wall("boundary-corner.png", tileWidth, tileHeight, 0), x, y); break;
                    case 'C': addObject(new Wall("boundary-corner.png", tileWidth, tileHeight, 90), x, y); break;
                }
                
                switch (type) {
                    case 'L': if (map.isRoomLeft(curRoomNum)) addObject(new Door(tileWidth, tileHeight, 270, "left"), x, y); break;
                    case 'R': if (map.isRoomRight(curRoomNum)) addObject(new Door(tileWidth, tileHeight, 90, "right"), x, y); break;
                    case 'U': if (map.isRoomUp(curRoomNum))  addObject(new Door(tileWidth, tileHeight, 0, "up"), x, y); break;
                    case 'D': if (map.isRoomDown(curRoomNum)) addObject(new Door(tileWidth, tileHeight, 180, "down"), x, y); break;
                }
            }
        }
        
        // Create room interior
        for(int i = 1; i < numTilesY - 1; i++){
            for(int j = 1; j < numTilesX - 1; j++){
                char type = roomLayout[i - 1].charAt(j - 1);
                int x = j * tileWidth + tileWidth / 2;
                int y = i * tileHeight + tileWidth / 2;
                                    
                switch (type) {
                    case 'G': addObject(new Goblin(-1, tileHeight), x, y); break;
                    case '#': addObject(new Wall("wall_mid.png", tileWidth, tileHeight, 0), x, y); break;
                    case 'B': addObject(new Boss(-1, tileHeight * 2), x, y); break;
                    case 'S': addObject(new Skelebro(-1, tileHeight), x, y); break;
                    case 'C': addObject(new Chest(tileWidth, tileHeight), x, y); break;
                    case 'N': addObject(new Necromancer(-1, tileHeight), x, y); break;
                    case 'R': addObject(new Rock(tileWidth, tileHeight), x, y); break;
                }
            }
        }
        
        // If there are enemies in the room, lock doors
        if (!getObjects(Enemy.class).isEmpty()) {
            for (Door door : getObjects(Door.class)) {
                if (door.getDir() != enterPos) {
                    door.lock();
                }
            }
        }
    }
    
    /**
     * Exits room when player walks through a door
     */
    public void exitRoom(String exitPos) {
        if (exitPos == "left") {
            curRoomNum--;
            enterPos = "right";
        } else if(exitPos == "right") {
            curRoomNum++;
            enterPos = "left";
        } else if(exitPos == "up") {
            curRoomNum -= 10;
            enterPos = "down";
        } else if(exitPos == "down") {
            curRoomNum += 10;
            enterPos = "up";
        }
        minimap.updateCurRoomNum(curRoomNum);
        
        // If we've never been to this room before, create it
        if (roomWorlds[curRoomNum] == null) {
            roomWorlds[curRoomNum] = new GameWorld();
        }
        
        roomWorlds[curRoomNum].updateRoom();
        Greenfoot.setWorld(roomWorlds[curRoomNum]); // Set world to new room!
    }
    
    /**
     * Ends the game
     */
    public void gameOver (boolean won) {
        boolean newHighScore = false;
        
        if (score > highScore) {
            highScore = score;
            newHighScore = true;
        }
        
        if (userInfo != null) {
            userInfo.setScore(highScore);
            userInfo.store();
        }
        
        bgMusic.stop(); // Stop background music before switching to game over screen
        
        Greenfoot.setWorld(new EndWorld(score, highScore, newHighScore, won));
    }
    
    /**
     * Increases score
     */
    public void updateScore(int scoreChange) {
        score += scoreChange;
        scoreText.setValue("Score: " + score);
        scoreText.setLocation(scoreText.getImage().getWidth() / 2, getHeight() - scoreText.getImage().getHeight() / 2);
    }
    
    /**
     * Scales image while preserving aspect ratio
     * 
     * @param image     image to scale
     * @param width     if -1, adjust based on aspect ratio and new height
     * @param height    if -1, adjust based on aspect ratio and new width
     */
    public static void scaleWithAspectRatio(GreenfootImage image, int width, int height) {
        if (width == -1) {
            float heightToWidth = (float)image.getWidth() / image.getHeight();
            width = Math.round(height * heightToWidth);
        } else if (height == -1) {
            float widthToHeight = (float)image.getHeight() / image.getWidth();
            height = Math.round(width * widthToHeight);
        }
        
        image.scale(width, height);
    }
    
    /**
     * If all enemies in a room are defeated, unlocks doors to allow player to progress!
     */
    public void updateDoors() {
        if (getObjects(Enemy.class).isEmpty()) {
            for (Door door : getObjects(Door.class)) {
                if (door.getDir() != enterPos) {
                    door.unlock();
                }
            }
        }
    }
}
