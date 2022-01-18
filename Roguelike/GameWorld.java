import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class GameWorld extends World
{
    // Class Variables / Objects    
    private static Player player;
    private static Minimap minimap;
    private static Label scoreText;
    private static UserInfo userInfo;
    private static GreenfootSound bgMusic;
    
    private static int score;
    private static int highScore;
    private static int curRoomNum;
    private static int startRoomNum;
    private static int tileWidth;
    private static int tileHeight;
    private static int playerSpawnX;
    private static int playerSpawnY;
    
    private static int[] floorPlan;
    private static int[] roomLayoutPlan;
    private static GameWorld[] roomWorlds;
    private static int roomCount;
    private static Queue<Integer> cellQueue;
    private static List<Integer> endRooms;
    private static int maxRooms;
    private static int minRooms;
    private static boolean started;
    private static int bossl;
    private static boolean placedSpecial;

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public GameWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 800, 1);
            
        createRoom();
        updateRoom();
        
        setPaintOrder(Minimap.class, Label.class, StatBar.class, Projectile.class, Player.class, Enemy.class, Wall.class, Floor.class);
        roomWorlds[curRoomNum] = this;
    }
    
    public void updateRoom() {
        addObject(player, playerSpawnX, playerSpawnY);
        addObject(minimap, getWidth() - minimap.getImage().getWidth() / 2, minimap.getImage().getHeight() / 2);
        addObject(scoreText, scoreText.getImage().getWidth() / 2, getHeight() - scoreText.getImage().getHeight() / 2);
    }
    
    public static void startOver() {
        score = 0;
        startRoomNum = 45;
        tileWidth = 80;
        tileHeight = 80;
        maxRooms = 15;
        minRooms = 7;
        started = false;
        playerSpawnX = 400;
        playerSpawnY = 400;
        
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
        
        player = new Player(-1, 80);
        minimap = new Minimap(floorPlan, curRoomNum, bossl);
        scoreText = new Label("Score: 0", 50);
        
        roomWorlds = new GameWorld[101];
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
        
        int numTilesX = roomLayout[0].length() + 2;
        int numTilesY = roomLayout.length + 2;
        int tileWidth = getWidth() / numTilesX;
        int tileHeight = getHeight() / numTilesY;
        
        for(int i = 0; i < numTilesY; i++){
            for(int j = 0; j < numTilesX; j++){
                char type = Layouts.boundaryLayout[i].charAt(j);
                int x = j * tileWidth + tileWidth / 2;
                int y = i * tileHeight + tileWidth / 2;
                
                Actor object = null;
                                    
                switch (type) {
                    case 'l': 
                        object = new Wall("boundary-edge.png", tileWidth, tileHeight, 270); 
                        break;
                    case 'r': 
                        object = new Wall("boundary-edge.png", tileWidth, tileHeight, 90); 
                        break;
                    case 'u': 
                        object = new Wall("boundary-edge.png", tileWidth, tileHeight, 0); 
                        break;
                    case 'd':
                        object = new Wall("boundary-edge.png", tileWidth, tileHeight, 180);
                        break;
                    case 'c':
                        object = new Wall("boundary-corner.png", tileWidth, tileHeight, 0);
                        break;
                    case 'C':
                        object = new Wall("boundary-corner.png", tileWidth, tileHeight, 90);
                        break;
                    case '1':
                        if (floorPlan[curRoomNum - 10] == 1)  object = new Door("door-left.png", tileWidth, tileHeight, 0, "up");
                        else object = new Wall("boundary-edge.png", tileWidth, tileHeight, 0);
                        break;
                    case '2':
                        if (floorPlan[curRoomNum - 10] == 1) object = new Door("door-right.png", tileWidth, tileHeight, 0, "up");
                        else object = new Wall("boundary-edge.png", tileWidth, tileHeight, 0);
                        break;
                    case '3':
                        if (floorPlan[curRoomNum + 1] == 1) object = new Door("door-left.png", tileWidth, tileHeight, 90, "right");
                        else object = new Wall("boundary-edge.png", tileWidth, tileHeight, 90);
                        break;
                    case '4':
                        if (floorPlan[curRoomNum + 1] == 1) object = new Door("door-right.png", tileWidth, tileHeight, 90, "right");
                        else object = new Wall("boundary-edge.png", tileWidth, tileHeight, 90);
                        break;
                    case '5':
                        if (floorPlan[curRoomNum + 10] == 1) object = new Door("door-left.png", tileWidth, tileHeight, 180, "down");
                        else object = new Wall("boundary-edge.png", tileWidth, tileHeight, 180);
                        break;
                    case '6':
                        if (floorPlan[curRoomNum + 10] == 1) object = new Door("door-right.png", tileWidth, tileHeight, 180, "down");
                        else object = new Wall("boundary-edge.png", tileWidth, tileHeight, 180);
                        break;
                    case '7':
                        if (floorPlan[curRoomNum - 1] == 1) object = new Door("door-left.png", tileWidth, tileHeight, 270, "left");
                        else object = new Wall("boundary-edge.png", tileWidth, tileHeight, 270);
                        break;
                    case '8':
                        if (floorPlan[curRoomNum - 1] == 1) object = new Door("door-right.png", tileWidth, tileHeight, 270, "left");
                        else object = new Wall("boundary-edge.png", tileWidth, tileHeight, 270);
                        break;
                }
                
                if (object != null) addObject(object, x, y);
            }
        }
        
        // Create room interior
        for(int i = 1; i < numTilesY - 1; i++){
            for(int j = 1; j < numTilesX - 1; j++){
                char type = roomLayout[i - 1].charAt(j - 1);
                int x = j * tileWidth + tileWidth / 2;
                int y = i * tileHeight + tileWidth / 2;
                
                Actor object = null;
                Floor floor;
                                    
                switch (type) {
                    case ' ':
                        object = new Floor(tileWidth, tileHeight);
                        break;
                    case '#':
                        object = new Wall("wall_mid.png", tileWidth, tileHeight, 0);
                        break;
                    case 'E':
                        object = new Goblin(-1, tileHeight);
                        
                        floor = new Floor(tileWidth, tileHeight);
                        addObject(floor, x, y);
                        break;
                    case '^':
                        object = new SpikedFloor(tileWidth, tileHeight);
                        break;
                    case 'B':
                        object = new Boss(-1, tileHeight * 2);
                        
                        floor = new Floor(tileWidth, tileHeight);
                        addObject(floor, x, y);
                        break;
                }
                
                addObject(object, x, y);
            }
        }
    }
    
    public void exitRoom(String exitPos) {
        if (exitPos == "left") {
            curRoomNum--;
            playerSpawnX = getWidth() - 130;
            playerSpawnY = player.getY();
        } else if(exitPos == "right") {
            curRoomNum++;
            playerSpawnX = 130;
            playerSpawnY = player.getY();
        } else if(exitPos == "up") {
            curRoomNum -= 10;
            playerSpawnX = player.getX();
            playerSpawnY = getHeight() - 130;
        } else if(exitPos == "down") {
            curRoomNum += 10;
            playerSpawnX = player.getX();
            playerSpawnY = 130;
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
}
