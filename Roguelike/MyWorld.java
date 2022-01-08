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
    
    String[] startLevel = {
        "# #  # #  ",
        "#         ",
        "          ",
        "       #  ",
        "  P       ",
        "          ",
        "   #      ",
        "          ",
        "#        #",
        "#       ##",
    };
    
    String[][] levels = {
        {
            "# #  # #  ",
            "#         ",
            "          ",
            "       #  ",
            "  P       ",
            "          ",
            "   #      ",
            "          ",
            "#        #",
            "#       ##",
        }
    };
    
    private Player player;
    
    int[] floorPlan = new int[101];
    int roomCount = 0;
    Queue<Integer> cellQueue = new LinkedList<Integer>();
    int maxRooms = 15;
    int minRooms = 7;
    int startRoom = 45;
    int curRoom;

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 800, 1); 
        
        curRoom = startRoom;
        
        generateMap();
        
        GreenfootImage tilemap = new GreenfootImage("0x72_16x16DungeonTileset.v3");
        processTilemap(tilemap);
        
        createLevel(curRoom);
        
        setPaintOrder(Wall.class, Player.class, Floor.class);
        
        //Minimap minimap = new Minimap(floorPlan);
        //addObject(minimap, 400, 400);
    }
    
    private void processTilemap(GreenfootImage tilemap) {
        
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
        
        int tileHeight = getHeight() / level.length;
        int tileWidth = getWidth() / level[0].length();
        
        for(int i = 0; i < level.length; i++){
            for(int j = 0; j < level[i].length(); j++){
                char type = level[i].charAt(j);
                int x = j * tileWidth + (tileWidth / 2);
                int y = i * tileHeight + (tileHeight / 2);
                
                Floor floor = new Floor(tileWidth, tileHeight);
                addObject(floor, x, y);
                
                switch(type){
                    case 'P':
                        player = new Player(tileWidth, tileHeight);
                        setPaintOrder(Player.class, Floor.class);
                        addObject(player, x, y);
                        break;
                    case '#':
                        GreenfootImage wallImage = new GreenfootImage("v1.1 dungeon crawler 16x16 pixel pack/tiles/wall/wall_1.png");
                        wallImage.scale(tileWidth, tileHeight);
                        Wall wall = new Wall(wallImage);
                        addObject(wall, x, y);
                        break;
                }
            }
        }
    }
}
