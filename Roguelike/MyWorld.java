import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World
{
    // Class Variables / Objects
    
    String[] emptyLevel = {
        "####UU####",
        "#        #",
        "#        #",
        "#        #",
        "L        R",
        "L        R",
        "#        #",
        "#        #",
        "#        #",
        "####DD####",
    };
    
    String[] startLevel = {
        "####UU####",
        "#        #",
        "#        #",
        "#        #",
        "L        R",
        "L        R",
        "#        #",
        "#        #",
        "#        #",
        "####DD####",
    };
    
    String[][] levels = {
        {
            "####UU####",
            "#        #",
            "#        #",
            "#    ##  #",
            "L        R",
            "L        R",
            "#        #",
            "#        #",
            "#        #",
            "####DD####",
        },
        {
            "####UU####",
            "#        #",
            "#        #",
            "#        #",
            "L        R",
            "L        R",
            "#        #",
            "#  #     #",
            "#  #     #",
            "####DD####",
        },
    };
    
    private Player player;
    
    String wallImagePath = "wall_corner_front_left.png";
    
    int[] floorPlan = new int[101];
    int roomCount = 0;
    Queue<Integer> cellQueue = new LinkedList<Integer>();
    int maxRooms = 15;
    int minRooms = 7;
    int startRoom = 45;
    int curRoom;
    String roomPos;

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 800, 1); 
        
        curRoom = startRoom;
        roomPos = "center";
        
        generateMap();
        
        createLevel(curRoom);
        
        Minimap minimap = new Minimap(floorPlan);
        addObject(minimap, 50, 50);
        
        setPaintOrder(Minimap.class, Wall.class, Player.class, Floor.class);
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
    
    private void createLevel(int room){
        String[] level = null;
        
        if (room == startRoom) {
            level = startLevel;
        }
        
        int numTilesX = level[0].length();
        int numTilesY = level.length;
        int tileWidth = getWidth() / numTilesX;
        int tileHeight = getHeight() / numTilesY;
        
        Player player = new Player(tileWidth, tileHeight);
        if (roomPos == "center") {
            addObject(player, getWidth() / 2, getHeight() / 2);
        }
        
        // Create level
        for(int i = 0; i < numTilesY; i++){
            for(int j = 0; j < numTilesX; j++){
                char type = level[i].charAt(j);
                int x = j * tileWidth + (tileWidth / 2);
                int y = i * tileHeight + (tileHeight / 2);
                
                Floor floor = new Floor(tileWidth, tileHeight);
                addObject(floor, x, y);
                                    
                if (type == '#') {
                    GreenfootImage wallImage = new GreenfootImage(wallImagePath);
                    wallImage.scale(tileWidth, tileHeight);
                    Wall wall = new Wall(wallImage);
                    addObject(wall, x, y);
                } else if (type == 'L') {
                    if (floorPlan[room - 1] != 1) {
                        GreenfootImage wallImage = new GreenfootImage(wallImagePath);
                        wallImage.scale(tileWidth, tileHeight);
                        Wall wall = new Wall(wallImage);
                        addObject(wall, x, y);
                    }
                } else if (type == 'R') {
                    if (floorPlan[room + 1] != 1) {
                        GreenfootImage wallImage = new GreenfootImage(wallImagePath);
                        wallImage.scale(tileWidth, tileHeight);
                        Wall wall = new Wall(wallImage);
                        addObject(wall, x, y);
                    }
                } else if (type == 'U') {
                    if (floorPlan[room - 10] != 1) {
                        GreenfootImage wallImage = new GreenfootImage(wallImagePath);
                        wallImage.scale(tileWidth, tileHeight);
                        Wall wall = new Wall(wallImage);
                        addObject(wall, x, y);
                    }
                } else if (type == 'D') {
                    if (floorPlan[room + 10] != 1) {
                        GreenfootImage wallImage = new GreenfootImage(wallImagePath);
                        wallImage.scale(tileWidth, tileHeight);
                        Wall wall = new Wall(wallImage);
                        addObject(wall, x, y);
                    }
                }
            }
        }
    }
    
    public void exitRoom(String exitPos) {
        
    }
}
