import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Tile here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Wall extends Actor
{
    private GreenfootImage image;
    
    public Wall (int tileWidth, int tileHeight) {
        this.getImage().scale(tileWidth, tileHeight);
    }
}