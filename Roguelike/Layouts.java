/**
 * Rooms in this game are created from arrays for convienience. This class stores all of them!
 * 
 * Here's what each symbol represents:
 * 'c': top left/bottom right corner wall
 * 'C': top right/bottom left corner wall
 * 'u': top boundary wall
 * 'd': bottom boundary wall
 * 'l': left boundary wall
 * 'r': right boundary wall
 * 'U': upper door
 * 'D': lower door
 * 'L': left door
 * 'R': right door
 * 'P': player
 * ' ': empty tile
 * '^': spike tile
 * '#': wall
 * 'G': goblin
 * 'S': skelebro
 * 'B': boss
 * 'T': treasure chest
 * 'N': necromancer
 * 
 * @author Eddie Zhuang
 * @version 1.0.0
 */
public class Layouts  
{
    // Layout for walls at edge of each level
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
    
    // Layout of starting room
    static final String[] startRoomLayout = {
        "             ",
        "             ",
        "    #   #    ",
        "      S      ",
        "    #   #    ",
        "     ###     ",
        "             ",
    };
    
    // Layout of boss room
    static final String[] bossRoomLayout = {
        "             ",
        "             ",
        "             ",
        "             ",
        "             ",
        "             ",
        "             ",
    };
    
    // Layouts for all other rooms
    static final String[][] roomLayouts = {
        {
            "             ",
            "             ",
            "             ",
            "    C C C    ",
            "             ",
            "             ",
            "             ",
        },
        {
            "             ",
            "      #      ",
            "   # S S #   ",
            "   #C   C#   ",
            "   # S S #   ",
            "      #      ",
            "             ",
        },
        {
            "G           G",
            "  G          ",
            "           G ",
            "      G      ",
            "             ",
            "    G        ",
            "G          G ",
        },
        {
            "N           N",
            "             ",
            "             ",
            "      C      ",
            "             ",
            "             ",
            "N           N",
        },
        {
            "             ",
            "   S  #      ",
            "             ",
            " #    N    # ",
            "             ",
            "      #  G   ",
            "             ",
        },
        {
            "     # #     ",
            "  S       N  ",
            " ##       ## ",
            "             ",
            " ##       ## ",
            "  S       S  ",
            "     # #     ",
        },
        {
            "G^         ^G",
            "^           ^",
            "    ^   ^    ",
            "      S      ",
            "    ^   ^    ",
            "^          C^",
            "G^         ^G",
        },
        {
            "             ",
            "    #   #    ",
            "   #     #   ",
            "      N      ",
            "   #     #   ",
            "    #   #    ",
            "             ",
        },
        {
            "             ",
            "^          ^ ",
            "       ^     ",
            "  ^          ",
            "    ^       ^",
            "        ^    ",
            "  ^     ^   C",
        },
        {
            "             ",
            "  #       #  ",
            "             ",
            "   G G G G   ",
            "             ",
            "  #       #  ",
            "             ",
        },
        {
            "             ",
            "      #      ",
            "             ",
            " #  S C S  # ",
            "             ",
            "      #      ",
            "             ",
        }
    };
}
