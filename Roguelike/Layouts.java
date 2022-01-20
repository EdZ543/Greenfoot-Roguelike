/**
 * Write a description of class Layouts here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Layouts  
{
    static final String[] boundaryLayout = {
        "cuuuuuuUuuuuuuC",
        "l             r",
        "l             r",
        "l             r",
        "L             R",
        "l             r",
        "l             r",
        "l             r",
        "CddddddDddddddc",
    };
    
    static final String[] startRoomLayout = {
        "             ",
        "             ",
        "             ",
        "             ",
        "             ",
        "             ",
        "             ",
    };
    
    static final String[] bossRoomLayout = {
        "             ",
        "             ",
        "             ",
        "      B      ",
        "             ",
        "             ",
        "             ",
    };
    
    static final String[][] roomLayouts = {
        {
            "             ",
            "             ",
            "   ^     ^   ",
            "             ",
            "   ^     ^   ",
            "             ",
            "             ",
        },
        {
            "             ",
            "  #       #  ",
            "             ",
            "     E E     ",
            "             ",
            "  #       #  ",
            "             ",
        },
        {
            "             ",
            "      #      ",
            "             ",
            " #  S   S  # ",
            "             ",
            "      #      ",
            "             ",
        }
    };
}
