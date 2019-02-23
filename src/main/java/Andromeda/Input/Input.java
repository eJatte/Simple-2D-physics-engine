package Andromeda.Input;

import Andromeda.Collision.Transform;
import Andromeda.Utils.Matrix_2x2;
import Andromeda.Utils.Vector2;

import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Classname: Input
 * Author: Elias Jätte
 * Date: 18/08/26
 *
 * Class for managing input.
 */
public class Input {
    private static ConcurrentLinkedQueue<Integer> inputQueue        = new ConcurrentLinkedQueue<>();
    private static ConcurrentLinkedQueue<Integer> inputRemoveQueue  = new ConcurrentLinkedQueue<>();
    private static HashSet<Integer> keyMap      = new HashSet<>();
    private static HashSet<Integer> keyDownMap  = new HashSet<>();
    private static HashSet<Integer> keyUpMap    = new HashSet<>();
    private static Vector2 mouseScreenPosition = new Vector2();
    private static Vector2 mouseWorldPosition  = new Vector2();
    private static double scrollRotation;
    private static double preScrollRotation;

    public static void tickInput(){
        keyDownMap.clear();
        keyUpMap.clear();

        scrollRotation = preScrollRotation;
        preScrollRotation = 0;

        while(!inputQueue.isEmpty()) {
            int keyCode = inputQueue.poll();
            if(!keyMap.contains(keyCode)){
                keyDownMap.add(keyCode);
                keyMap.add(keyCode);
            }
        }

        while(!inputRemoveQueue.isEmpty()) {
            int keyCode = inputRemoveQueue.poll();
            if(keyMap.remove(keyCode)){
                keyUpMap.add(keyCode);
            }
        }
    }

    public static void setMousePosition(Vector2 mousePosition, Transform screenTransform){
        mouseScreenPosition = mousePosition;
        mouseWorldPosition = mousePosition.multiply(screenTransform.inverse());
    }

    public static Vector2 getMouseScreenPosition() {
        return mouseScreenPosition;
    }

    public static Vector2 getMouseWorldPosition() {
        return mouseWorldPosition;
    }

    public static double getScrollRotation(){
        return scrollRotation;
    }

    public static void addKeyInput(int keyCode){
        inputQueue.add(keyCode);
    }

    public static void removeKeyInput(int keyCode){
        inputRemoveQueue.add(keyCode);
    }

    public static void addScrollRotation(double s){
        preScrollRotation += s;
    }

    public static  boolean getKey(int keyCode){
        return keyMap.contains(keyCode);
    }

    public static boolean getKeyDown(int keyCode){
        return keyDownMap.contains(keyCode);
    }

    public static boolean getKeyUp(int keyCode){
        return keyUpMap.contains(keyCode);
    }
}
