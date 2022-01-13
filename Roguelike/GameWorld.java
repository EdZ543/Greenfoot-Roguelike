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
    private Player player;
    private Minimap minimap;
    
    private int score = 0;
    private int curRoomNum;
    private int startRoomNum = 45;
    private int tileWidth = 80;
    private int tileHeight = 80;
    
    // Procedural map generation
    private int[] floorPlan;
    private int[] roomLayoutPlan;
    private int roomCount;
    private Queue<Integer> cellQueue;
    private Queue<Integer> endRooms;
    private int maxRooms = 15;
    private int minRooms = 7;
    private boolean started = false;
    private int bossl;
    private boolean placedSpecial;

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public GameWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 800, 1);
        
        curRoomNum = startRoomNum;
        
        generateMap();
        
        createRoom();
        
        player = new Player(tileWidth, tileHeight);
        addObject(player, getWidth() / 2, getHeight() / 2);
        
        minimap = new Minimap(floorPlan, curRoomNum, bossl);
        addObject(minimap, getWidth() - minimap.getImage().getWidth() / 2, minimap.getImage().getHeight() / 2);
        
        setPaintOrder(Minimap.class, StatBar.class, Projectile.class, Player.class, Enemy.class, Wall.class, Floor.class);
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
    
    private void initMap() {
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
    
    private void fillMap() {
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
                bossl = endRooms.remove();
            } else {
                started = false;
            }
        }
    }
    
    private void assignRoomLayouts() {
        roomLayoutPlan = new int[101];
        
        List<Integer> randomRooms = new ArrayList();
        for (int i = 0; i < Layouts.roomLayouts.length; i++) {
            randomRooms.add(i);
        }
        Collections.shuffle(randomRooms);
        
        int randomRoomIndex = 0;
        for (int i = 0; i < floorPlan.length; i++) {
            if (floorPlan[i] == 1) {
                roomLayoutPlan[i] = randomRooms.get(randomRoomIndex++);
                randomRoomIndex %= randomRooms.size();
            }
        }
    }
    
    private void generateMap() {
        initMap();
        fillMap();
        assignRoomLayouts();
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
    
    private void createRoom(){
        String[] roomLayout = null;
        
        if (curRoomNum == startRoomNum) {
            roomLayout = Layouts.startRoomLayout;
        } else {
            int roomLayoutIndex = roomLayoutPlan[curRoomNum];
            roomLayout = Layouts.roomLayouts[roomLayoutIndex];
        }
        
        int numTilesX = roomLayout[0].length() + 2;
        int numTilesY = roomLayout.length + 2;
        int tileWidth = getWidth() / numTilesX;
        int tileHeight = getHeight() / numTilesY;
        
        // Draw background
        for(int i = 0; i < numTilesY; i++){
            for(int j = 0; j < numTilesX; j++){
                int x = j * tileWidth + tileWidth / 2;
                int y = i * tileHeight + tileHeight / 2;
                
                Floor floor = new Floor(tileWidth, tileHeight);
                addObject(floor, x, y);
            }
        }
        
        // Create boundaries
        for(int i = 0; i < numTilesY; i++){
            for(int j = 0; j < numTilesX; j++){
                if (floorPlan[curRoomNum - 1] == 1 && j == 0 && (i == 4 || i == 5)) continue;
                if (floorPlan[curRoomNum + 1] == 1 && j == numTilesX - 1 && (i == 4 || i == 5)) continue;
                if (floorPlan[curRoomNum - 10] == 1 && i == 0 && (j == 4 || j == 5)) continue;
                if (floorPlan[curRoomNum + 10] == 1 && i == numTilesY - 1 && (j == 4 || j == 5)) continue;
                
                int x = j * tileWidth + tileWidth / 2;
                int y = i * tileHeight + tileWidth / 2;
                
                if (i == 0 || i == numTilesY - 1 || j == 0 || j == numTilesX - 1) {
                    Wall wall = new Wall(tileWidth, tileHeight);
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
                                    
                switch (type) {
                    case '#':
                        Wall wall = new Wall(tileWidth, tileHeight);
                        addObject(wall, x, y);
                        break;
                    case 'E':
                        Goblin goblin = new Goblin(tileWidth, tileHeight);
                        addObject(goblin, x, y);
                }
            }
        }
    }
    
    private void clearRoom() {
        List<Actor> actors = getObjects(null);
        for (Actor a : actors) {
            Class c = a.getClass();
            if (c != Player.class && c != Minimap.class && c != StatBar.class) {
                removeObject(a);
            }
        }
        
        getBackground().fill();
    }
    
    public void exitRoom(String exitPos) {
        int spaceFromExit = 5;
        
        if (exitPos == "left") {
            curRoomNum--;
            player.setLocation(getWidth() - spaceFromExit, player.getY());
        } else if(exitPos == "right") {
            curRoomNum++;
            player.setLocation(spaceFromExit, player.getY());
        } else if(exitPos == "up") {
            curRoomNum -= 10;
            player.setLocation(player.getX(), getHeight() - spaceFromExit);
        } else if(exitPos == "down") {
            curRoomNum += 10;
            player.setLocation(player.getX(), spaceFromExit);
        }
        
        minimap.updateCurRoomNum(curRoomNum);
        
        clearRoom();
        createRoom();
    }
    
    public void gameOver () {
        Greenfoot.setWorld(new EndWorld());
    }
}
