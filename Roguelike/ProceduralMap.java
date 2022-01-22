import java.util.*;

/**
 * Class that procedurally generates a map of room layouts!
 * 
 * @author Eddie Zhuang
 * @version Jan. 22, 2022
 */
public class ProceduralMap  
{
    private int startRoomNum = 45;
    private int bossRoomNum;
    private int[] floorPlan;
    private int roomCount = 0;
    private Queue<Integer> cellQueue;
    private List<Integer> endRooms;
    private int maxRooms = 15;
    private int minRooms = 7;
    private boolean started;
    private boolean placedSpecial;

    /**
     * Constructor for objects of class ProceduralMap
     */
    public ProceduralMap()
    {
        startOver();
        visit(startRoomNum);
        fillMap();
    }
    
    public int getStartRoomNum() {
        return startRoomNum;
    }
    
    public int getBossRoomNum() {
        return bossRoomNum;
    }
    
    public boolean isRoomUp(int cell) {
        return floorPlan[cell - 10] == 1;
    }
    
    public boolean isRoomDown(int cell) {
        return floorPlan[cell + 10] == 1;
    }
    
    public boolean isRoomLeft(int cell) {
        return floorPlan[cell - 1] == 1;
    }
    
    public boolean isRoomRight(int cell) {
        return floorPlan[cell + 1] == 1;
    }
    
    public int[] getFloorPlan() {
        return floorPlan;
    }
    
    public int floorPlanLength() {
        return floorPlan.length;
    }
    
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
        started = true;
        placedSpecial = false;
    }
    
    /**
     * Fills map with positions of rooms
     */
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
                // Start over if not enough rooms
                if (roomCount < minRooms) {
                    startOver();
                    continue;
                }
                
                placedSpecial = true;
                bossRoomNum = endRooms.remove(endRooms.size() - 1);
            } else {
                started = false;
            }
        }
    }
    
    /**
     * Returns number of neighbouring rooms to a cell
     */
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
}
