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
    private int[] floorPlan;
    private int[] roomLayoutPlan;
    private int roomCount;
    private Queue<Integer> cellQueue;
    private List<Integer> endRooms;
    private int maxRooms = 15;
    private int minRooms = 7;
    private boolean started;
    private int bossl;
    private static boolean placedSpecial;

    /**
     * Constructor for objects of class ProceduralMap
     */
    public ProceduralMap()
    {
    }

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y   a sample parameter for a method
     * @return     the sum of x and y 
     */
    public int sampleMethod(int y)
    {
        // put your code here
        return x + y;
    }
}
