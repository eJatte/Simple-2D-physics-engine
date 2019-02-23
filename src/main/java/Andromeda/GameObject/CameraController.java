package Andromeda.GameObject;

import Andromeda.Input.Input;
import Andromeda.Input.KeyCode;
import Andromeda.Render.Camera;
import Andromeda.Utils.Vector2;

/**
 * Classname: CameraController
 * Author: Elias Jätte
 * Date: 18/08/27
 *
 * Simple camera controller.
 */
public class CameraController extends GameObject{
    private Camera camera;
    public double speed = 0.3;

    public CameraController() {
        this.camera = new Camera();
        camera.setTransform(transform);
        camera.scale(20);
        camera.translate(new Vector2(10,10));
    }

    @Override
    public void update() {
        double speed = this.speed*(camera.getScale()/10);

        if(Input.getKey(KeyCode.W)){
            transform.translate(new Vector2(0,-1.0*speed));
        }
        if(Input.getKey(KeyCode.S)){
            transform.translate(new Vector2(0,1.0*speed));
        }
        if(Input.getKey(KeyCode.D)){
            transform.translate(new Vector2(1.0*speed,0));
        }
        if(Input.getKey(KeyCode.A)){
            transform.translate(new Vector2(-1.0*speed,0));
        }

        camera.addScale(Input.getScrollRotation()*(camera.getScale()/10));
    }

    public Camera getCamera() {
        return camera;
    }
}
