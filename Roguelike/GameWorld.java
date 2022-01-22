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
 * 
 * Code from Danpost:
 * Animation Support Class, https://www.greenfoot.org/scenarios/14496
 * Battle (Boss) by BoxCat Games | https://freemusicarchive.org/music/BoxCat_Games Music promoted by https://www.chosic.com/free-music/all/ Creative Commons CC BY 3.0 https://creativecommons.org/licenses/by/3.0/
 *
 * Bow sound effect obtained from https://www.zapsplat.com
 * 
 * ## Instructions ##
 * WASD to move, arrow keys to shoot
 * P to pick up items
 * 
 * This is a procedurally generated roguelike.
 * This means that each run will have a random map and rooms.
 * It also has permadeath, which means if you die, you start from the beginning!
 * Walk through doors to progress, you'll have to defeat al enemies in a room to unlock the doors.
 * Try to get as high a score as possible by collecting coins and defeating enemies.
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
    
    private static int score;
    private static int highScore;
    private static int curRoomNum;
    private static String enterPos;
    private static int characterSelection = 0;
    private GameWorld[] roomWorlds;

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public GameWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(960, 576, 1);
            
        createRoom();
        updateRoom();
        
        setPaintOrder(Minimap.class, Label.class, StatBar.class, Projectile.class, Player.class, Enemy.class, Door.class, Wall.class, Floor.class);
        roomWorlds[curRoomNum] = this;
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
    
    public void updateRoom() {
        int offsetX = tileWidth + (int)Math.ceil((double)player.getImage().getWidth() / 2);
        int offsetY = tileHeight + (int)Math.ceil((double)player.getImage().getHeight() / 2);
        
        if (enterPos == "center") addObject(player, getWidth() / 2, getHeight() / 2);
        else if (enterPos == "right") addObject(player, getWidth() - offsetX, player.getY());
        else if(enterPos == "left") addObject(player, offsetX, player.getY());
        else if(enterPos == "down") addObject(player, player.getX(), getHeight() - offsetY);
        else if(enterPos == "up") addObject(player, player.getX(), offsetY);
        addObject(minimap, getWidth() - minimap.getImage().getWidth() / 2, minimap.getImage().getHeight() / 2);
        addObject(scoreText, scoreText.getImage().getWidth() / 2, getHeight() - scoreText.getImage().getHeight() / 2);
    }
    
    public static void setCharacter(int characterSelection) {
        GameWorld.characterSelection = characterSelection;
    }
    
    public static void startOver() {
        score = 0;
        started = false;
        enterPos = "center";
        
        bgMusic = new GreenfootSound("BoxCat-Games-Battle-Boss.mp3");
        bgMusic.setVolume(25);
        bgMusic.playLoop();
        
        curRoomNum = startRoomNum;
    
        if (UserInfo.isStorageAvailable()) {
            userInfo = UserInfo.getMyInfo();
        }
        
        if (userInfo != null) {
            highScore = userInfo.getScore();
        }
    
        generateMap();
        
        player = new Player(-1, tileHeight, characterSelection);
        minimap = new Minimap(floorPlan, curRoomNum, bossl);
        scoreText = new Label("Score: 0", 50);
        
        roomWorlds = new GameWorld[101];
    }
    
    public Player getPlayer() {
        return player;
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
    
    private static void initMap() {
        started = true;
        placedSpecial = false;
        floorPlan = new int[101];
        for (int i = 0; i <= 100; i++) {
            floorPlan[i] = 0;
        }
        roomCount = 0;
        cellQueue = new LinkedList<Integer>();
        endRooms = new LinkedList<Integer>();
        
        visit(45);
    }
    
    private static void fillMap() {
        while (started) {
            if (cellQueue.size() > 0) {
                int cell = cellQueue.remove();
                int x = cell % 10;
                
                boolean created = false;
                if (x > 1) created |= visit(cell - 1);
                if (x < 9) created |= visit(cell + 1);
                if (cell > 20) created |= visit(cell - 10);
                if (cell < 70) created |= visit(cell + 10);
                
                if (!created) {
                    endRooms.add(cell);
                }
            } else if (!placedSpecial) {
                if (roomCount < minRooms) {
                    initMap();
                    continue;
                }
                
                placedSpecial = true;
                bossl = endRooms.remove(endRooms.size() - 1);
            } else {
                started = false;
            }
        }
    }
    
    private static void assignRoomLayouts() {
        roomLayoutPlan = new int[101];
        
        List<Integer> randomRooms = new ArrayList();
        for (int i = 0; i < Layouts.roomLayouts.length; i++) {
            randomRooms.add(i);
        }
        Collections.shuffle(randomRooms);
        
        int randomRoomIndex = 0;
        for (int i = 0; i < floorPlan.length; i++) {
            if (floorPlan[i] == 1 && i != startRoomNum && i != bossl) {
                roomLayoutPlan[i] = randomRooms.get(randomRoomIndex++);
                randomRoomIndex %= randomRooms.size();
            }
        }
    }
    
    private static void generateMap() {
        initMap();
        fillMap();
        assignRoomLayouts();
    }
    
    private static int neighbourCount(int cell) {
        return floorPlan[cell - 10] + floorPlan[cell - 1] + floorPlan[cell + 1] + floorPlan[cell + 10];
    }
    
    private static boolean visit(int cell) {
        if (floorPlan[cell] != 0) {
            return false;
        }
        
        int neighbours = neighbourCount(cell);
        
        if (neighbours > 1) {
            return false;
        }
        
        if (roomCount >= maxRooms) {
            return false;
        }
        
        Random random = new Random();
        if (random.nextBoolean() && cell != 45) {
            return false;
        }
        
        if (cellQueue != null) cellQueue.add(cell);
        floorPlan[cell] = 1;
        roomCount++;
        
        return true;
    }
    
    private void createRoom(){
        String[] roomLayout = null;
        
        if (curRoomNum == startRoomNum) {
            roomLayout = Layouts.startRoomLayout;
        } else if(curRoomNum == bossl) {
            roomLayout = Layouts.bossRoomLayout;
        } else {
            int roomLayoutIndex = roomLayoutPlan[curRoomNum];
            roomLayout = Layouts.roomLayouts[roomLayoutIndex];
        }
        
        for(int i = 0; i < numTilesY; i++){
            for(int j = 0; j < numTilesX; j++){
                char type = Layouts.boundaryLayout[i].charAt(j);
                int x = j * tileWidth + tileWidth / 2;
                int y = i * tileHeight + tileWidth / 2;
                
                switch (type) {
                    case 'L': if (floorPlan[curRoomNum - 1] == 1) addObject(new Door(tileWidth, tileHeight, 270, "left"), x, y);
                    case 'l': addObject(new Wall("boundary-edge.png", tileWidth, tileHeight, 270), x, y); break;
                    case 'R': if (floorPlan[curRoomNum + 1] == 1) addObject(new Door(tileWidth, tileHeight, 90, "right"), x, y);
                    case 'r': addObject(new Wall("boundary-edge.png", tileWidth, tileHeight, 90), x, y); break;
                    case 'U': if (floorPlan[curRoomNum - 10] == 1)  addObject(new Door(tileWidth, tileHeight, 0, "up"), x, y);
                    case 'u': addObject(new Wall("boundary-edge.png", tileWidth, tileHeight, 0), x, y); break;
                    case 'D': if (floorPlan[curRoomNum + 10] == 1) addObject(new Door(tileWidth, tileHeight, 180, "down"), x, y);
                    case 'd': addObject(new Wall("boundary-edge.png", tileWidth, tileHeight, 180), x, y); break;
                    case 'c': addObject(new Wall("boundary-corner.png", tileWidth, tileHeight, 0), x, y); break;
                    case 'C': addObject(new Wall("boundary-corner.png", tileWidth, tileHeight, 90), x, y); break;
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
                    case 'G': addObject(new Goblin(-1, tileHeight), x, y); addObject(new Floor(tileWidth, tileHeight), x, y); break;
                    case ' ': addObject(new Floor(tileWidth, tileHeight), x, y); break;
                    case '#': addObject(new Wall("wall_mid.png", tileWidth, tileHeight, 0), x, y); break;
                    case '^': addObject(new SpikedFloor(tileWidth, tileHeight), x, y);break;
                    case 'B': addObject(new Boss(-1, tileHeight * 2), x, y); addObject(new Floor(tileWidth, tileHeight), x, y); break;
                    case 'S': addObject(new Skelebro(-1, tileHeight), x, y); addObject(new Floor(tileWidth, tileHeight), x, y); break;
                }
            }
        }
        
        if (!getObjects(Enemy.class).isEmpty()) {
            for (Door door : getObjects(Door.class)) {
                if (door.getDir() != enterPos) {
                    door.lock();
                }
            }
        }
    }
    
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
        
        if (roomWorlds[curRoomNum] == null) {
            roomWorlds[curRoomNum] = new GameWorld();
        }
        
        roomWorlds[curRoomNum].updateRoom();
        Greenfoot.setWorld(roomWorlds[curRoomNum]);
    }
    
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
