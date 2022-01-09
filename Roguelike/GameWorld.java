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
    
    private String[] emptyLevel = {
        "        ",
        "        ",
        "        ",
        "        ",
        "        ",
        "        ",
        "        ",
        "        ",
    };
    
    private String[] startRoomLayout = {
        "        ",
        "        ",
        "        ",
        "        ",
        "        ",
        "        ",
        "        ",
        "        ",
    };
    
    private String[][] levels = {
        {
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
        },
        {
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
            "             ",
        },
    };
    
    private static Player player;
    
    private String wallImagePath = "wall_corner_front_left.png";
    
    private int[] floorPlan = new int[101];
    private int roomCount = 0;
    private Queue<Integer> cellQueue = new LinkedList<Integer>();
    private int maxRooms = 15;
    private int minRooms = 7;
    private int startRoomNum = 45;
    private int curRoomNum;
    private int playerSpawnX;
    private int playerSpawnY;

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public GameWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 800, 1);
        
        curRoomNum = startRoomNum;
        playerSpawnX = getWidth() / 2;
        playerSpawnY = getHeight() / 2;
        
        generateMap();
        
        createRoom();
    }
    
    private void createRoom() {
        createRoomlayout(curRoomNum);
        spawnPlayer();
        
        Minimap minimap = new Minimap(floorPlan, startRoomNum, curRoomNum);
        addObject(minimap, 50, 50);
    }
    
    private void spawnPlayer() {
        player = new Player(50, 50);
        addObject(player, playerSpawnX, playerSpawnY);
    }
    
    private void generateMap() {
        for (int i = 0; i <= 100; i++) {
            floorPlan[i] = 0;
        }
        
        visit(45);
        
        while (cellQueue.size() > 0) {
            int cell = cellQueue.remove();
            int x = cell % 10;
            
            if (x > 1) visit(cell - 1);
            if (x < 9) visit(cell + 1);
            if (cell > 20) visit(cell - 10);
            if (cell < 70) visit(cell + 10);
        }
    }
    
    private int neighbourCount(int cell) {
        return floorPlan[cell - 10] + floorPlan[cell - 1] + floorPlan[cell + 1] + floorPlan[cell + 10];
    }
    
    private boolean visit(int cell) {
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
    
    private void createRoomlayout(int roomNum){
        String[] roomLayout = null;
        
        roomLayout = startRoomLayout;
        
        int numTilesX = roomLayout[0].length() + 2;
        int numTilesY = roomLayout.length + 2;
        int tileWidth = getWidth() / numTilesX;
        int tileHeight = getHeight() / numTilesY;
        
        GreenfootImage floorImage = new GreenfootImage("floor_1.png");
        floorImage.scale(tileWidth, tileHeight);
        
        // Draw background
        for(int i = 0; i < numTilesY; i++){
            for(int j = 0; j < numTilesX; j++){
                int x = j * tileWidth;
                int y = i * tileHeight;
                
                getBackground().drawImage(floorImage, x, y);
            }
        }
        
        // Create boundaries
        for(int i = 0; i < numTilesY; i++){
            for(int j = 0; j < numTilesX; j++){
                if (floorPlan[roomNum - 1] == 1 && j == 0 && (i == 4 || i == 5)) continue;
                if (floorPlan[roomNum + 1] == 1 && j == numTilesX - 1 && (i == 4 || i == 5)) continue;
                if (floorPlan[roomNum - 10] == 1 && i == 0 && (j == 4 || j == 5)) continue;
                if (floorPlan[roomNum + 10] == 1 && i == numTilesY - 1 && (j == 4 || j == 5)) continue;
                
                int x = j * tileWidth + tileWidth / 2;
                int y = i * tileHeight + tileWidth / 2;
                
                if (i == 0 || i == numTilesY - 1 || j == 0 || j == numTilesX - 1) {
                    GreenfootImage wallImage = new GreenfootImage(wallImagePath);
                    wallImage.scale(tileWidth, tileHeight);
                    Wall wall = new Wall(wallImage);
                    addObject(wall, x, y);
                }
            }
        }
        
        // Create room interior
        for(int i = 1; i < numTilesY - 1; i++){
            for(int j = 1; j < numTilesX - 1; j++){
                char type = roomLayout[i - 1].charAt(j - 1);
                int x = j * tileWidth + tileWidth / 2;
                int y = i * tileHeight + tileWidth / 2;
                                    
                if (type == '#') {
                    GreenfootImage wallImage = new GreenfootImage(wallImagePath);
                    wallImage.scale(tileWidth, tileHeight);
                    Wall wall = new Wall(wallImage);
                    addObject(wall, x, y);
                }
            }
        }
    }
    
    private void clearRoom() {
        removeObjects(getObjects(null));
        getBackground().fill();
    }
    
    public void exitRoom(String exitPos) {
        int spaceFromExit = 5;
        
        if (exitPos == "left") {
            curRoomNum--;
            playerSpawnX = getWidth() - spaceFromExit;
            playerSpawnY = player.getY();
        } else if(exitPos == "right") {
            curRoomNum++;
            playerSpawnX = spaceFromExit;
            playerSpawnY = player.getY();
        } else if(exitPos == "up") {
            curRoomNum -= 10;
            playerSpawnX = player.getX();
            playerSpawnY = getHeight() - spaceFromExit;
        } else if(exitPos == "down") {
            curRoomNum += 10;
            playerSpawnX = player.getX();
            playerSpawnY = spaceFromExit;
        }
        
        clearRoom();
        createRoom();
    }
}
