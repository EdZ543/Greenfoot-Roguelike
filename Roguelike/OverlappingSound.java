import greenfoot.*;
import java.util.*;

/**
 * A class to play overlapping sounds, only spawns new sounds when nessecary
 * 
 * @author Eddie Zhuang
 * @version Jan. 25, 2022
 */
public class OverlappingSound  
{
    private List<GreenfootSound> sounds;
    private int soundIndex = 0;
    private String soundPath;
    private int volume;
    
    /**
     * Constructor for objects of class OverlappingSound
     */
    public OverlappingSound(String soundPath, int volume) {
        this.soundPath = soundPath;
        this.volume = volume;
    }

    /**
     * Simpler constructor without volume, which defaults to full volume
     */
    public OverlappingSound(String soundPath) {
        this(soundPath, 100);
    }

    /**
     * Plays the sound, implements overlapping
     */
    public void play() {
        // Initialize sound list if sound not played yet
        if (sounds == null) {
            sounds = new LinkedList<GreenfootSound>();
        }
        
        // If no sounds added yet or sound is currently playing, add a new sound
        if (sounds.isEmpty() || sounds.get(soundIndex).isPlaying()) {
            GreenfootSound newSound = new GreenfootSound(soundPath);
            newSound.setVolume(volume);
            sounds.add(soundIndex, newSound);
        }
        
        // Play sound
        sounds.get(soundIndex).play();
        
        // Go to next sound in list, looping back to beginning if necessary
        soundIndex = (soundIndex + 1) % sounds.size();
    }
}