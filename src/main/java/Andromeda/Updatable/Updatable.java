package Andromeda.Updatable;

/**
 * Classname: Updatable
 * Author: Elias Jätte
 * Date: 18/08/12
 */
public interface Updatable {
    int getTickInterval();

    void update();

    void start();

    void awake();
}

