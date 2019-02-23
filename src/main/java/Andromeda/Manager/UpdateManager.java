package Andromeda.Manager;

import Andromeda.Updatable.Updatable;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Classname: UpdateManager
 * Author: Elias Jätte
 * Date: 2018-06-01
 *
 * Manager for updatable objects.
 */
public class UpdateManager implements Updatable {
    private class Element {
        public Updatable updatable;
        public int currentTick;

        public Element(Updatable updatable){
            this.updatable = updatable;
            currentTick = 0;
        }
    }
    private ConcurrentLinkedQueue<Updatable> updatablesBuffer;
    private ConcurrentLinkedQueue<Updatable> updatablesRemoveBuffer;
    private ArrayList<Element> updatables;

    private int maxTickInterval = 1;
    private int currentTickOffset = 0;
    private int tick = 0;

    public UpdateManager(){
        updatablesBuffer = new ConcurrentLinkedQueue<>();
        updatablesRemoveBuffer = new ConcurrentLinkedQueue<>();
        updatables = new ArrayList<>();
    }

    public void addUpdatable(Updatable updatable){
        updatablesBuffer.add(updatable);
    }

    public void removeUpdatable(Updatable updatable){
        updatablesRemoveBuffer.add(updatable);
    }

    @Override
    public void update(){
        while(!updatablesRemoveBuffer.isEmpty()){
            for (int i = 0; i < updatables.size(); i++) {
                Updatable u = updatablesRemoveBuffer.poll();
                Element e = updatables.get(i);
                if(e.updatable == u){
                    updatables.remove(i);
                    break;
                }
            }
        }
        while(!updatablesBuffer.isEmpty()) {
            Updatable u = updatablesBuffer.poll();
            Element e = new Element(u);

            if(u.getTickInterval() > maxTickInterval)
                maxTickInterval = u.getTickInterval();
            e.currentTick = currentTickOffset%(u.getTickInterval());
            currentTickOffset++;
            if(currentTickOffset == maxTickInterval)
                currentTickOffset = 0;
            updatables.add(e);
        }
        tick++;
        for (Element e:updatables) {
            e.currentTick++;
            if(e.currentTick == e.updatable.getTickInterval()){
                e.updatable.update();
                e.currentTick = 0;
            }
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void awake() {

    }

    @Override
    public int getTickInterval() {
        return 1;
    }
}
