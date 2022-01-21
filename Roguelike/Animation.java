import greenfoot.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.*;

/**
 * CLASS: Animation (subclass of Object)
 * AUTHOR: danpost (greenfoot.org username)
 * DATE: June 22, 2015
 * 
 * DESCRIPTION: this class creates an object that is used to control the animation of a World or Actor object;
 * it is intended that each object that is to be animated create its own Animation object and also run it;
 * it is also intended that each object that is to be animated have only one Animation object assigned to it 
 * 
 * Modified by Eddie a bit to add switching between different states and facing directions.
 */

// Helper class to store different animation states
class AnimationState {
    public GreenfootImage imgSet[];
    public int acts;
    public String originalDir;
    
    public AnimationState(GreenfootImage[] imgSet, int acts, String originalDir) {
        this.imgSet = imgSet;
        this.acts = acts;
        this.originalDir = originalDir;
    }
}

public class Animation
{
    private Object animated; // the World or Actor object that to be animated
    private GreenfootImage[] frames; // the image set currently being used for this animated object
    private int cycleActs; // number of acts to complete a cycle of images
    private boolean active; // on-off switch
    private int timer; // internal timer
    
    private Hashtable<String, AnimationState> animationStates = new Hashtable<String, AnimationState>(); // hashtable from name to animation state
    private String originalDir; // direction the original frames are facing
    private String dir; // direction the animation should face
    private String stateName;
    
    /**
     * creates an animation object that uses the given images for the given object;
     * 
     * @param object the object to be animated
     * @param stateName the name of the starting state
     * @param imgSet the images to be used in the animation; a 'null' value or an empty array will invalidate the animation
     * @param acts speed of animation
     * @param originalDir the direction the raw images are facing originally
     */ 
    public Animation(Object object, String stateName, GreenfootImage imgSet[], int acts, String originalDir)
    {
        animated = object;
        dir = originalDir;
        
        addState(stateName, imgSet, acts, originalDir);
        setState(stateName);
    }
    
    /**
     * Adds an animation state
     */
    public void addState(String stateName, GreenfootImage imgSet[], int acts, String originalDir) {
        AnimationState animationState = new AnimationState(imgSet, acts, originalDir);
        animationStates.put(stateName, animationState);
    }
    
    /**
     * Sets to an animation state
     */
    public void setState(String state) {
        // Stop if state doesn't exist or already set to state
        if (stateName == state || !animationStates.containsKey(state)) {
            return;
        }
        
        AnimationState animationState = animationStates.get(state);
        originalDir = animationState.originalDir;
        stateName = state;
        setFrames(animationState.imgSet);
        setCycleActs(animationState.acts);
    }
    
    /**
     * Set direction for animation to face
     */
    public void setDir(String dir) {
        this.dir = dir;
    }

    /**
     * Method for quickly generating animation frames, given that all images have the same path except for a number.
     * @param numFrames number of frames
     * @param prefix filepath before the number
     * @param suffix filepath after the number
     * @param width the width of the frames
     * @param height the height of the frames
     * @return an array of GreenfootImage frames
     */
    public static GreenfootImage[] generateFrames(int numFrames, String prefix, String suffix, int width, int height) {
        GreenfootImage[] frames = new GreenfootImage[numFrames];
        
        for (int i = 0; i < numFrames; i++) {
            String framePath = prefix + i + suffix;
            GreenfootImage frame = new GreenfootImage(framePath);
            GameWorld.scaleWithAspectRatio(frame, width, height);
            frames[i] = frame;
        }
        
        return frames;
    }
    
    /**
     * internal method that updates the image of the animation
     */
    private void setFrame(int index)
    {
        GreenfootImage frame = new GreenfootImage(frames[index]);
        
        if (dir != originalDir) {
            frame.mirrorHorizontally();
        }
        
        if (animated instanceof Actor) ((Actor)animated).setImage(frame);
        else if (animated instanceof World) ((World)animated).setBackground(frame);
    }
    
    /**
     * if animating, runs the timer and updates the image when needed
     * 
     * @return a flag indicating the current animation cycle has completed
     */
    public boolean run()
    {
        if (cycleActs == 0 || !active) return false;
        int inFrame = timer*frames.length/cycleActs;
        timer = (timer+1)%cycleActs;
        int outFrame = timer*frames.length/cycleActs;
        if (inFrame != outFrame) setFrame(outFrame);
        return timer == 0;
    }
    
    /**
     * sets the images and speed regulator value (the time, in act cycles, it takes for the set of images to be shown once)
     * 
     * @param imgSet the images to be used in the animation
     * @param actsInAnimation number of act cycles to complete one animation cycle in
     */
    public void setAnimation(GreenfootImage[] imgSet, int actsInAnimation)
    {
        setCycleActs(actsInAnimation);
        setFrames(imgSet);
    }
    
    /**
     * sets the images to be used in the animation
     * 
     * @param imgSet the images to be used in the animation
     */
    public void setFrames(GreenfootImage[] imgSet)
    {
        frames = imgSet;
        timer = 0;
        if (imgSet != null && imgSet.length != 0) setFrame(0); else active = false;
    }
    
    /**
     * returns the images currently set to be used in the animation
     * 
     * @return the array of images currently set to the animation
     */
    public GreenfootImage[] getFrames() { return frames; }
    
    /**
     * sets the speed regulator value (the time, in act cycles, it takes for the set of images to be shown once)
     * 
     * @param acts the number of act cycles to complete one animation cycle in
     */
    public void setCycleActs(int acts) { cycleActs = (acts < 0 ? 0 : acts); }
    
    /**
     * returns the speed regulator value
     * 
     * @return the number of act cycles that one animation cycle is currently set to run in
     */
    public int getCycleActs() { return cycleActs; }
    
    /**
     * sets the active state of the animation; it is only set active if field values are set properly
     * 
     * @param state a true/false value indicating which active state to set the animation in
     */
    public void setActiveState(boolean state)
    {
        if (!(animated instanceof Actor || animated instanceof World)) return;
        if (frames == null || frames.length == 0) return;
        active = state;
    }
    
    /**
     * returns the active state of the animation
     * 
     * @return the current active state the animation is currrently in
     */
    public boolean isActive() { return active; }
    
    /**
     * reverses the order of images in the animation set (allows the animation to be run backwards)
     */
    public void reverseImageOrder() { Collections.reverse(Arrays.asList(frames)); }
}