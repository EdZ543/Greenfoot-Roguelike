import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class FloorTile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FloorTile extends Actor
{
    public FloorTile (int tileWidth, int tileHeight) {
        getImage().scale(tileWidth, tileHeight);
    }
}
