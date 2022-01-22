import java.util.*;

/**
 * Class that procedurally generates a map of room layouts!
 * Represents map with an integer array, where 1 means there's a room and 0 means there isn't
 * Tens digit of index represents y index and ones digit represents x index
 * 
 * @author Eddie Zhuang
 * @version Jan. 22, 2022
 */
public class ProceduralMap  
{
    private int startRoomNum = 45; // Starting room
    private int bossRoomNum; // Room with boss
    private int[] floorPlan; // Array representing position of rooms
    private int roomCount = 0; // Number of rooms created so far
    private Queue<Integer> cellQueue; // Queue of rooms to visit
    private List<Integer> endRooms; // Rooms with only 1 neighbour, for placing rooms like the boss room
    private int maxRooms = 15; // Max number of rooms allowed
    private int minRooms = 7; // Min number of rooms allowed
    private boolean placedSpecial; // Whether all special rooms have been placed yet

    /**
     * Constructor for objects of class ProceduralMap
     */
    public ProceduralMap()
    {
        startOver();
        visit(startRoomNum);
        fillMap();
    }
    
    /**
     * Returns index of start room
     */
    public int getStartRoomNum() {
        return startRoomNum;
    }
    
    /**
     * Returns index of boss room
     */
    public int getBossRoomNum() {
        return bossRoomNum;
    }
    
    /**
     * Returns whether a room is above a given cell
     */
    public boolean isRoomUp(int cell) {
        return floorPlan[cell - 10] == 1;
    }
    
    /**
     * Returns whether a room is below a given cell
     */
    public boolean isRoomDown(int cell) {
        return floorPlan[cell + 10] == 1;
    }
    
    /**
     * Returns whether a room is to the left of a given cell
     */
    public boolean isRoomLeft(int cell) {
        return floorPlan[cell - 1] == 1;
    }
    
    /**
     * Returns whether a room is to the right of a given cell
     */
    public boolean isRoomRight(int cell) {
        return floorPlan[cell + 1] == 1;
    }
    
    /**
     * Returns length of floor plan array
     */
    public int floorPlanLength() {
        return floorPlan.length;
    }
    
    /**
     * Returns whether a room exists at an index
     */
    public boolean isRoomAt(int cell) {
        return floorPlan[cell] == 1;
    }
    
    /**
     * Restarts map creation process
     */
    private void startOver() {
        floorPlan = new int[101];
        cellQueue = new LinkedList<Integer>();
        endRooms = new LinkedList<Integer>();
        placedSpecial = false;
    }
    
    /**
     * Fills map with positions of rooms
     */
    private void fillMap() {
        // Generates layout with sort of a breadth-first search
        while (true) {
            if (cellQueue.size() > 0) {
                int cell = cellQueue.remove();
                int x = cell % 10;
                
                // Visit all adjacent cells to see if they can be made into rooms
                boolean created = false;
                if (x > 1) created |= visit(cell - 1);
                if (x < 9) created |= visit(cell + 1);
                if (cell > 20) created |= visit(cell - 10);
                if (cell < 70) created |= visit(cell + 10);
                
                // If no other neighbours, this is a room at the end of the map
                if (!created) {
                    endRooms.add(cell);
                }
            } else if (!placedSpecial) {
                // Start over if not enough rooms
                if (roomCount < minRooms) {
                    startOver();
                    continue;
                }
                
                placedSpecial = true;
                bossRoomNum = endRooms.remove(endRooms.size() - 1); // Choose an endroom to place boss room
            } else {
                break;
            }
        }
    }
    
    /**
     * Returns number of neighbouring rooms to a cell
     */
    private int neighbourCount(int cell) {
        return floorPlan[cell - 10] + floorPlan[cell - 1] + floorPlan[cell + 1] + floorPlan[cell + 10];
    }
    
    /**
     * Function for determining whether to make cell into a room
     * Returns whether or not it can
     */
    private boolean visit(int cell) {
        // If cell already has a room, has too many neighbours, or there's already too many rooms, abort
        if (floorPlan[cell] != 0 || neighbourCount(cell) > 1 || roomCount >= maxRooms) {
            return false;
        }
        
        // 50% chance of becoming a room, for increased randomness
        Random random = new Random();
        if (random.nextBoolean() && cell != 45) {
            return false;
        }
        
        if (cellQueue != null) cellQueue.add(cell);
        floorPlan[cell] = 1;
        roomCount++;
        
        return true;
    }
}
