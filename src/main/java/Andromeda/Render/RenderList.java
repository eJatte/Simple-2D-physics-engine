/*
 * Classname: RenderList.java
 * Author: Elias Jätte
 * Date: 2017-12-10
 */

package Andromeda.Render;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A class for managing drawables. Order is decided by the drawables layer
 * value. Small to big.
 * @author Elias Jätte, c16eje
 */
public class RenderList implements Iterable<Drawable>{
    private ArrayList<Drawable> drawables;

    /**
     * Creates a new RenderList
     */
    public RenderList(){
        drawables = new ArrayList<Drawable>();
    }

    /**
     * Adds a new drawable to the RenderList.
     * Position of the drawable in the list is decided by its layer value.
     *
     * @param drawable          The drawable to add.
     */
    public synchronized void addDrawable(Drawable drawable){
        if(drawables.size() == 0) {
            drawables.add(drawable);
            return;
        }
        for (int i = 0; i < drawables.size(); i++) {
            if(drawable.getLayer() < drawables.get(i).getLayer()){
                drawables.add(i,drawable);
                return;
            }
        }
        drawables.add(drawable);
    }

    /**
     * Removes a drawable from the RenderList
     *
     * @param drawable      The drawable to remove
     */
    public synchronized void removeDrawable(Drawable drawable){
        drawables.remove(drawable);
    }

    /**
     * Get a drawable at a certain index.
     *
     * @param i     index
     * @return      Drawable
     */
    public synchronized Drawable get(int i){
        return drawables.get(i);
    }

    /**
     * Returns the size of the RenderList. Number of drawables.
     *
     * @return          size
     */
    public synchronized int size(){
        return drawables.size();
    }

    /**
     * Iterator of the RenderList
     *
     * @return          Iterator
     */
    public synchronized Iterator iterator(){
        return drawables.iterator();
    }

    /**
     * Get drawables. Will create a new list with the drawables
     * @return
     */
    public synchronized ArrayList<Drawable> getDrawables(){
        return new ArrayList<Drawable>(drawables);
    }
}
