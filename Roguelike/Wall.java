import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Tile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wall extends WallTile
{
    private GreenfootImage image;
    
    public Wall (String imagePath, int width, int height, int rotation) {
        super(imagePath, width, height, rotation);
    }
}