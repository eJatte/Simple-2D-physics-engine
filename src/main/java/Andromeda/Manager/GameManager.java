package Andromeda.Manager;

import Andromeda.Collision.Transform;
import Andromeda.GameObject.CameraController;
import Andromeda.GameObject.MouseCaller;
import Andromeda.Input.Input;
import Andromeda.Input.KeyCode;
import Andromeda.Render.Camera;
import Andromeda.Render.RenderList;
import Andromeda.Utils.Vector2;
import Andromeda.View.Viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Classname: GameManager
 * Author: Elias Jätte
 * Date: 18/08/11
 *
 * A singelton class for managing game.
 */
public class GameManager {
    private static volatile GameManager instance;
    private static Object mutex = new Object();

    public static GameManager getInstance(){
        GameManager result = instance;
        if(result == null){
            synchronized (mutex){
                result = instance;
                if(result == null) {
                    instance = result = new GameManager();
                }
            }
        }

        return result;
    }

    private UpdateManager updateManager;
    private Scene currentScene;
    int frames = 0, ticks = 0;
    private AtomicBoolean running;
    private Viewer viewer;
    private RenderList renderList;
    private Thread thread;

    private GameManager(){
        running = new AtomicBoolean(true);
        setUpViewer();

        renderList = new RenderList();
        updateManager = new UpdateManager();
        currentScene = new Scene();

        CameraController cameraController = new CameraController();
        viewer.setCamera(cameraController.getCamera());
        getScene().addGameObject(cameraController);
        renderList.addDrawable(currentScene);
    }

    /**
     * Get current scene of the game.
     *
     * @return Scene
     */
    public Scene getScene(){
        return currentScene;
    }

    /**
     * Update game.
     */
    private void update(){
        Input.tickInput();
        Point m =  viewer.getMousePosition();
        Transform screenTransform = viewer.getScreenTransform();
        if(m != null && screenTransform != null) {
            Input.setMousePosition(new Vector2(m), screenTransform);
        }
        getScene().update();
    }

    /**
     * Render thread.
     */
    public void startThread(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                long initialTime = System.nanoTime();
                final double timeU = 1000000000 / Time.updateRate;
                final double timeF = 1000000000 / Time.updateRate;
                double deltaU = 0, deltaF = 0;
                long timer = System.currentTimeMillis();
                while (running.get()) {

                    long currentTime = System.nanoTime();
                    deltaU += (currentTime - initialTime) / timeU;
                    deltaF += (currentTime - initialTime) / timeF;
                    initialTime = currentTime;

                    if (deltaU >= 1) {
                        update();

                        Time.deltaTime = deltaU/Time.updateRate;
                        ticks++;

                        deltaU--;
                    }

                    if (deltaF >= 1) {
                        //Render
                        viewer.renderDrawables(renderList.getDrawables());
                        frames++;
                        deltaF--;
                    }

                    if (System.currentTimeMillis() - timer > 1000) {
                        //System.out.println(String.format("UPS: %s, FPS: %s", ticks, frames));
                        frames = 0;
                        ticks = 0;
                        timer += 1000;
                    }

                }
            }
        });
        thread.setName("Game");
        thread.start();


    }

    public void setUpViewer(){
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    viewer = new Viewer();
                    viewer.addRenderPanelMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {
                            Input.addKeyInput(e.getButton());
                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {
                            Input.removeKeyInput(e.getButton());
                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                        }
                    });

                    viewer.addRenderPanelKeyListener(new KeyListener() {
                        @Override
                        public void keyTyped(KeyEvent e) {

                        }

                        @Override
                        public void keyPressed(KeyEvent e) {
                            Input.addKeyInput(e.getKeyCode());
                        }

                        @Override
                        public void keyReleased(KeyEvent e) {
                            Input.removeKeyInput(e.getKeyCode());
                        }
                    });
                    viewer.addRenderPanelMouseWheelListener(new MouseWheelListener() {
                        @Override
                        public void mouseWheelMoved(MouseWheelEvent e) {
                            double s = e.getPreciseWheelRotation();
                            Input.addScrollRotation(s);
                        }
                    });
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
